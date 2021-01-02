package com.kneelawk.marionette;

import com.kneelawk.marionette.api.CurrentThread;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlockingCurrentThreadExecutor implements ExecutorService, Runnable, CurrentThread {
    private final AtomicBoolean shutdown = new AtomicBoolean(false);
    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);
    private final BlockingQueue<FutureTask<?>> futureTasks = new LinkedBlockingQueue<>();
    private final CountDownLatch awaiter = new CountDownLatch(1);
    private boolean running = false;

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                FutureTask<?> task = futureTasks.take();
                task.run();
            } catch (InterruptedException e) {
                running = false;
            }
        }
        awaiter.countDown();
    }

    @Override
    public void shutdown() {
        if (!shuttingDown.getAndSet(true)) {
            futureTasks.add(new FutureTask<>(() -> {
                running = false;
                shutdown.set(true);
            }, null));
        }
    }

    @NotNull
    @Override
    public List<Runnable> shutdownNow() {
        if (!shuttingDown.getAndSet(true)) {
            futureTasks.add(new FutureTask<>(() -> {
                running = false;
                shutdown.set(true);
            }, null));
        }
        return new ArrayList<>();
    }

    @Override
    public boolean isShutdown() {
        return shutdown.get();
    }

    @Override
    public boolean isTerminated() {
        return shutdown.get();
    }

    @Override
    public boolean isExpired() {
        return shutdown.get();
    }

    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return awaiter.await(timeout, unit);
    }

    @Override
    public void execute(@NotNull Runnable command) {
        if (shuttingDown.get()) {
            throw new RejectedExecutionException("This executor is being shut down");
        }

        futureTasks.add(new FutureTask<>(command, null));
    }

    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        if (shuttingDown.get()) {
            throw new RejectedExecutionException("This executor is being shut down");
        }

        FutureTask<T> f = new FutureTask<>(task);
        futureTasks.add(f);
        return f;
    }

    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Runnable task, T result) {
        if (shuttingDown.get()) {
            throw new RejectedExecutionException("This executor is being shut down");
        }

        FutureTask<T> f = new FutureTask<>(task, result);
        futureTasks.add(f);
        return f;
    }

    @NotNull
    @Override
    public Future<?> submit(@NotNull Runnable task) {
        if (shuttingDown.get()) {
            throw new RejectedExecutionException("This executor is being shut down");
        }

        FutureTask<?> f = new FutureTask<>(task, null);
        futureTasks.add(f);
        return f;
    }

    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        if (shuttingDown.get()) {
            throw new RejectedExecutionException("This executor is being shut down");
        }

        List<Future<T>> futures = new ArrayList<>();

        try {
            for (Callable<T> task : tasks) {
                FutureTask<T> future = new FutureTask<>(task);
                futureTasks.put(future);
                futures.add(future);
            }

            for (Future<?> future : futures) {
                if (!future.isDone()) {
                    try {
                        future.get();
                    } catch (CancellationException | ExecutionException ignored) {
                    }
                }
            }
        } catch (Throwable t) {
            cancelAll(futures);
            throw t;
        }

        return futures;
    }

    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout,
                                         @NotNull TimeUnit unit) throws InterruptedException {
        if (shuttingDown.get()) {
            throw new RejectedExecutionException("This executor is being shut down");
        }

        final long nanos = unit.toNanos(timeout);
        final long deadline = System.nanoTime() + nanos;

        List<Future<T>> futures = new ArrayList<>();
        List<FutureTask<T>> invokeTasks = new ArrayList<>();

        int currentFuture = 0;

        timedOut:
        try {
            for (Callable<T> task : tasks) {
                FutureTask<T> futureTask = new FutureTask<>(task);
                futures.add(futureTask);
                invokeTasks.add(futureTask);
            }

            final int size = invokeTasks.size();

            for (int i = 0; i < size; i++) {
                if (((i == 0) ? nanos : deadline - System.nanoTime()) <= 0L) {
                    break timedOut;
                }
                futureTasks.put(invokeTasks.get(i));
            }

            for (; currentFuture < size; currentFuture++) {
                Future<T> future = futures.get(currentFuture);
                if (!future.isDone()) {
                    try {
                        future.get(deadline, TimeUnit.NANOSECONDS);
                    } catch (CancellationException | ExecutionException ignored) {
                    } catch (TimeoutException e) {
                        break timedOut;
                    }
                }
            }

            return futures;
        } catch (Throwable t) {
            cancelAll(futures);
            throw t;
        }

        cancelAll(futures, currentFuture);

        return futures;
    }

    @NotNull
    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        if (shuttingDown.get()) {
            throw new RejectedExecutionException("This executor is being shut down");
        }

        try {
            return doInvokeAny(tasks, false, 0);
        } catch (TimeoutException cannotHappen) {
            assert false;
            return null;
        }
    }

    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (shuttingDown.get()) {
            throw new RejectedExecutionException("This executor is being shut down");
        }

        return doInvokeAny(tasks, true, unit.toNanos(timeout));
    }

    /**
     * the main mechanics of invokeAny.
     * <p>
     * Copied from java.util.concurrent.AbstractExecutorService.
     */
    private <T> T doInvokeAny(Collection<? extends Callable<T>> tasks,
                              boolean timed, long nanos)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (tasks == null) {
            throw new NullPointerException();
        }
        int ntasks = tasks.size();
        if (ntasks == 0) {
            throw new IllegalArgumentException();
        }
        ArrayList<Future<T>> futures = new ArrayList<>(ntasks);
        ExecutorCompletionService<T> ecs =
                new ExecutorCompletionService<>(this);

        // For efficiency, especially in executors with limited
        // parallelism, check to see if previously submitted tasks are
        // done before submitting more of them. This interleaving
        // plus the exception mechanics account for messiness of main
        // loop.

        try {
            // Record exceptions so that if we fail to obtain any
            // result, we can throw the last exception we got.
            ExecutionException ee = null;
            final long deadline = timed ? System.nanoTime() + nanos : 0L;
            Iterator<? extends Callable<T>> it = tasks.iterator();

            // Start one task for sure; the rest incrementally
            futures.add(ecs.submit(it.next()));
            --ntasks;
            int active = 1;

            for (; ; ) {
                Future<T> f = ecs.poll();
                if (f == null) {
                    if (ntasks > 0) {
                        --ntasks;
                        futures.add(ecs.submit(it.next()));
                        ++active;
                    } else if (active == 0) {
                        break;
                    } else if (timed) {
                        f = ecs.poll(nanos, TimeUnit.NANOSECONDS);
                        if (f == null) {
                            throw new TimeoutException();
                        }
                        nanos = deadline - System.nanoTime();
                    } else {
                        f = ecs.take();
                    }
                }
                if (f != null) {
                    --active;
                    try {
                        return f.get();
                    } catch (ExecutionException eex) {
                        ee = eex;
                    } catch (RuntimeException rex) {
                        ee = new ExecutionException(rex);
                    }
                }
            }

            if (ee == null) {
                ee = new ExecutionException(null);
            }
            throw ee;

        } finally {
            cancelAll(futures);
        }
    }

    private static <T> void cancelAll(List<Future<T>> futures) {
        cancelAll(futures, 0);
    }

    /**
     * Cancels all futures with index at least j.
     */
    private static <T> void cancelAll(List<Future<T>> futures, int j) {
        for (int size = futures.size(); j < size; j++) {
            futures.get(j).cancel(true);
        }
    }
}

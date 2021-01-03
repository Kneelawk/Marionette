package com.kneelawk.marionette;

import com.kneelawk.marionette.api.CurrentThread;

public interface CallbackConsumer<C> {
    void accept(C callback, CurrentThread currentThread) throws Exception;
}

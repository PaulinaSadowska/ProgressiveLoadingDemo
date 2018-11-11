package com.nekodev.paulina.sadowska.progressiveloadingdemo

import io.reactivex.exceptions.UndeliverableException
import io.reactivex.functions.Consumer

class RxJavaErrorHandler : Consumer<Throwable> {

    override fun accept(throwable: Throwable) {
        if (throwable is UndeliverableException) {
            //ignore or log exception
        } else {
            throw throwable
        }
    }
}

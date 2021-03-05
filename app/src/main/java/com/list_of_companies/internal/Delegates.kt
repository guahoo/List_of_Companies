package com.list_of_companies.internal

import kotlinx.coroutines.*

fun <T> lazyDeferred (block: suspend CoroutineScope.()->T):Lazy<Deferred<T>>{
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}
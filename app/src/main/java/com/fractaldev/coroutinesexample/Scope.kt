package com.fractaldev.coroutinesexample

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class Scope : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Default// + CoroutineExceptionHandler { coroutineContext, throwable ->
//            println("error occurred: $throwable")
//        }

}
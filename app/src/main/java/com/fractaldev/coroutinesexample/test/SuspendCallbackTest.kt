package com.fractaldev.coroutinesexample.test

import com.fractaldev.coroutinesexample.Scope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

fun main() {
    suspendCallback()
}

fun suspendCallback() {
    val scope = Scope()
    scope.launch {
        val res = getAsyncResult(AsyncOperation())
        println("result: $res")
    }
    println("waiting for result")
    readLine()
}

//test
suspend fun getAsyncResult(operation: AsyncOperation): Int {
    return suspendCancellableCoroutine { continuation ->
        operation.addOnCompleteListener {
            println("continuation active: ${continuation.isActive}, completed: ${continuation.isCompleted}, canceled: ${continuation.isCancelled}")
            continuation.resume(it)
            println("continuation active: ${continuation.isActive}, completed: ${continuation.isCompleted}, canceled: ${continuation.isCancelled}")
            continuation.resume(it)
        }.start()
    }
}

class AsyncOperation : Thread() {

    private var onComplete: (Int) -> Unit = {}

    override fun run() {
        super.run()
        println("operation started")
        sleep(3000)
        onComplete(Random.nextInt())
    }

    fun addOnCompleteListener(onComplete: (Int) -> Unit): AsyncOperation {
        this.onComplete = onComplete
        return this
    }

}
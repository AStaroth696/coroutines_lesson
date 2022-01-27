package com.fractaldev.coroutinesexample.test

import com.fractaldev.coroutinesexample.Scope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

fun main() {
//    sharedFlowSubscriptionsTest()
    realExample()
}

private fun sharedFlowSubscriptionsTest() {
    var counter = 0
    val flow = MutableSharedFlow<Int>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val scope = Scope()
    val job1 = scope.launch {
        flow.onSubscription {
            counter++
            println("new subscriber: $counter")
        }.onCompletion {
            counter--
            println("removed subscriber: $counter")
        }.collect {
            println("first collector: $it")
        }
    }

    val job2 = scope.launch {
        flow.onSubscription {
            counter++
            println("new subscriber: $counter")
        }.onCompletion {
            counter--
            println("removed subscriber: $counter")
        }.collect {
            println("second collector: $it")
        }
    }

    scope.launch {
        (0 until 5).forEach {
            flow.emit(it)
            delay(500)
        }
    }
    readLine()
    job1.cancel()
    readLine()
    job2.cancel()
    readLine()
}

private fun realExample() {
    val scope = Scope()

    val provider = FlowProvider()
    val job1 = scope.launch {
        provider.getDataFlow().collect {
            println("first subscriber: $it")
        }
    }

    val job2 = scope.launch {
        provider.getDataFlow().collect {
            println("second subscriber: $it")
        }
    }

    scope.launch {
        provider.start()
    }
    readLine()
    job1.cancel()
    readLine()
    job2.cancel()
    readLine()

}


class FlowProvider {

    private var counter = 0
    private val flow = MutableSharedFlow<Int>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun getDataFlow(): Flow<Int> {
        return flow.onSubscription {
            counter++
            if (counter == 1) {
                startReceiver()
            }
            println("new subscriber: $counter")
        }.onCompletion {
            counter--
            if (counter == 0) {
                stopReceiver()
            }
            println("subscriber removed: $counter")
        }
    }

    suspend fun start() {
        (0 until 5).forEach {
            flow.emit(it)
            delay(500)
        }
    }

    private fun startReceiver() {
        println("starting receiver")
    }

    private fun stopReceiver() {
        println("stopping receiver")
    }

}
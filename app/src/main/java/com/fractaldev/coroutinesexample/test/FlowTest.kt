package com.fractaldev.coroutinesexample.test

import com.fractaldev.coroutinesexample.Scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
//    flowTest()
//    stateFlowTest()
//    stateFlowOverflowTest()
//    sharedFlowTest()
    sharedFlowTest2()
}

private fun flowTest() {
    val scope = Scope()
    val flow = flow {
        (0..5).forEach {
            println("flow thread: ${Thread.currentThread()}")
            emit(it)
        }
    }.flowOn(Dispatchers.Unconfined)
    val j1 = scope.launch(Dispatchers.IO) {
        flow.collect {
            println("first collect: $it, ${Thread.currentThread()}")
        }
    }
    readLine()
    val j2 = scope.launch(Dispatchers.IO) {
        flow.collect {
            println("second collect: $it, ${Thread.currentThread()}")
        }
    }
    runBlocking {
        j1.join()
        j2.join()
    }
}

private fun stateFlowTest() {
    val flow = MutableStateFlow("")
    val scope = Scope()
    scope.launch {
        flow.collect {
            println("value: $it")
        }
    }
    scope.launch {
        flow.collect {
            println("value2: $it")
        }
    }
    var input = readLine()
    while (input != "quit") {
        flow.value = input ?: ""
        input = readLine()
    }
    println("finish")
}

private fun stateFlowOverflowTest() {
    val flow = MutableStateFlow("")
    val scope = Scope()
    scope.launch {
        flow.collect {
            println("value: $it")
        }
    }
    (0..100).onEach {
        flow.value = it.toString()
    }
    readLine()
}

private fun sharedFlowTest() {
    val flow = MutableSharedFlow<String>()
    val scope = Scope()
    scope.launch {
        flow.collect {
            println("value: $it")
        }
    }
    scope.launch {
        (0..100).onEach {
            flow.emit(it.toString())
        }
    }
    readLine()
}

private fun sharedFlowTest2() {
    val flow = MutableSharedFlow<String>(
        replay = 5,
        extraBufferCapacity = 5,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val scope = Scope()
    scope.launch {
        flow.collect {
            println("value: $it")
        }
    }
    scope.launch {
        (0..100).onEach {
            flow.emit(it.toString())
        }
    }
//    (0..100).onEach {
//        flow.tryEmit(it.toString())
//    }
    readLine()
    scope.launch {
        flow.collect {
            println("value2: $it")
        }
    }
    readLine()
}
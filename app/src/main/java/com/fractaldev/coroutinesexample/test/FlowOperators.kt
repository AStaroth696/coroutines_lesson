package com.fractaldev.coroutinesexample.test

import com.fractaldev.coroutinesexample.Scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

fun main() {
//    flowOperatorsTest()
//    mergeFlow()
//    mergeFlow2()
//    mapLatest()
//    flatMap()
    flatMapMerge()
}

private fun flowOperatorsTest() {
    val flow = MutableSharedFlow<Any>(
        replay = 3,
        extraBufferCapacity = 3,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val targetFlow = flow.filterIsInstance<Int>()
        .filter {
            it % 2 == 0
        }.distinctUntilChanged()
        .map {
            it.toString()
        }
//        .debounce(500)
        .flowOn(Dispatchers.IO)
    val scope = Scope()
    scope.launch {
        targetFlow.collect {
            println("res: $it")
        }
    }
    scope.launch {
        flow.emit("")
        flow.emit(false)
        flow.emit(1)
        flow.emit(2)
        flow.emit(2)
        flow.emit(4)
        flow.emit(10)
    }
    readLine()
}

private fun mergeFlow() {
    val flow1 = flow {
        (0..5).forEach {
            emit(it)
            delay(10)
        }
    }
    val flow2 = flow {
        (0..5).forEach {
            emit("number $it")
            delay(5)
        }
    }
    val resFlow = merge(
        flow1.map { it.toString() },
        flow2
    )
    val scope = Scope()
    scope.launch {
        resFlow.collect {
            println("res: $it")
        }
    }
    readLine()
}

private fun mergeFlow2() {
    val flow1 = flow {
        (0..5).forEach {
            emit(it)
        }
    }
    val flow2 = flow<String> {
        (0..5).forEach {
            emit("number $it")
        }
    }
    val res = flow1.combine(flow2) { first, second ->
        println("combine $first with $second")
        "$first : $second"
    }
    val scope = Scope()
    scope.launch {
        res.collect {
            println("res: $it")
        }
    }
    readLine()
}

private fun mapLatest() {
    val flow = flow {
        (0..5).forEach {
            emit(it)
        }
    }
    val scope = Scope()
    scope.launch {
        flow.mapLatest {
            delay(500)
            it
        }.collect {
            println("received: $it")
        }
    }
    readLine()
}

private fun flatMap() {
    val flow = flow {
        (0..5).forEach {
            emit(it)
        }
    }
    val scope = Scope()
    scope.launch {
        flow.flatMapConcat {
            delay(500)
            println("emit $it")
            flow {
                (0..it).forEach {
                    emit(it)
                }
            }
        }.collect {
            println("received: $it")
        }
    }
    readLine()
}

private fun flatMapMerge() {
    val flow = flow {
        (0..5).forEach {
            emit(it)
        }
    }
    val scope = Scope()
    scope.launch {
        flow.flatMapMerge(concurrency = 5) { emit ->
            delay(500)
            flow {
                (0..emit).forEach { digit ->
                    emit("emit from $emit: $digit")
                }
            }
        }.collect {
            println("received: $it")
        }
    }
    readLine()
}
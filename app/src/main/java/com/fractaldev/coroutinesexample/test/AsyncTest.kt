package com.fractaldev.coroutinesexample.test

import com.fractaldev.coroutinesexample.Scope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
//    combineAsyncResults()
    combineListResults()
}

private fun combineAsyncResults() {
    val scope = Scope()
    val start = System.currentTimeMillis()
    val def1 = scope.async {
        var counter = 0
        while (counter < 5) {
            delay(1000)
            println("def1: $counter")
            counter++
        }
        1
    }
    val def2 = scope.async {
        var counter = 0
        while (counter < 5) {
            delay(500)
            println("def2: $counter")
            counter++
        }
        "deferred"
    }
    runBlocking {
        println("awaiting result")
        println("res1: ${def1.await()}, res2: ${def2.await()}")
        val end = System.currentTimeMillis()
        println("elapsed time: ${end - start}")
    }
}

private fun combineListResults() {
    val scope = Scope()
    val list = (0..10).toList()
    val start = System.currentTimeMillis()
    val def = list.map { digit ->
        scope.async {
            delay(500)
            digit.toString()
        }
    }
    runBlocking {
        val res = def.map { it.await() }
        val end = System.currentTimeMillis()
        println("res: ${res.joinToString(",")}")
        println("elapsed time: ${end - start}")
    }
}
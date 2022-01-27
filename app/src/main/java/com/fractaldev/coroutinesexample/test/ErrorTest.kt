package com.fractaldev.coroutinesexample.test

import kotlinx.coroutines.*

fun main() {
    errorWithoutJob()
//    errorWithScopeSupervisorJob()
//    errorWithJob()
}

private fun errorWithoutJob() {
    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {
        repeat(2) {
            try {
                val result = async {
//                val result = async(Job()) {
                    try {
                        if (it == 0) {
                            throw IllegalStateException("test")
                        } else {
                            0
                        }
                    } catch (e: Exception) {
                        println("error: $e")
                        -1
                    }
                }.await()
                println("result: $result")
            } catch (e: Exception) {
                println("error: $e")
            }
        }
    }
    readLine()
    scope.launch {
        println("launch")
    }
}

private fun errorWithScopeSupervisorJob() {
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    scope.launch {
        repeat(2) {
            try {
                val result = async {
                    if (it == 0) {
                        throw IllegalStateException("test")
                    } else {
                        0
                    }
                }.await()
                println("result: $result")
            } catch (e: Exception) {
                println("error: $e")
            }
        }
    }
    readLine()
    scope.launch {
        println("launch")
    }
}

private fun errorWithJob() {
    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {
        repeat(2) {
            try {
                val result = async(Job()) {
//                try {
                    if (it == 0) {
                        throw IllegalStateException("test")
                    } else {
                        0
                    }
//                } catch (e: Exception) {
//                    println("error: $e")
//                    0
//                }
                }.await()
                println("result: $result")
            } catch (e: Exception) {
                println("error: $e")
            }
        }
    }
    readLine()
    scope.launch {
        println("launch")
    }
}
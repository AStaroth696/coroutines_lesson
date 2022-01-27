package com.fractaldev.coroutinesexample.test

import com.fractaldev.coroutinesexample.Scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    val scope = Scope()
    println("before launch")
    scope.launch {
        var count = 0
        while (count < 5) {
            println("count: $count (${Thread.currentThread()})")
            delay(1000)
            count++
        }
    }
    println("after launch")
    readLine()
}
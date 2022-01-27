package com.fractaldev.coroutinesexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.fractaldev.coroutinesexample.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonStart.setOnClickListener {
            lifecycleScope.launch {
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
        }
    }

}

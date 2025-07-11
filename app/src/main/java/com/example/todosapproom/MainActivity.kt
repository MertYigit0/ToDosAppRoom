package com.example.todosapproom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todosapproom.databinding.ActivityMainBinding
import com.example.todosapproom.ui.ListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ListFragment())
                .commit()
        }
    }
}
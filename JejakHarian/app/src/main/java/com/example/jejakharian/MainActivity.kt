package com.example.jejakharian

import Beranda
import Kalender
import Tambah_Kalender
import android.icu.util.Calendar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jejakharian.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationView

        fun replaceFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        replaceFragment(Beranda())

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(Beranda())
                    true
                }
                
                R.id.navigation_calendar -> {
                    replaceFragment(Tambah_Kalender())
                    true
                }
                else -> false
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

    }

    fun reloadBeranda() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is Beranda) {
            fragment.loadKalenderData() // Pastikan loadKalenderData() bersifat public
        }
    }

}
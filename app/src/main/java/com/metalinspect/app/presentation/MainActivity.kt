package com.metalinspect.app.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.metalinspect.app.R
import com.metalinspect.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigation()
        setupToolbar()
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        binding.bottomNavigation.setupWithNavController(navController)
        
        // Handle navigation item selection
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inspections -> {
                    navController.navigate(R.id.inspectionListFragment)
                    true
                }
                R.id.nav_camera -> {
                    navController.navigate(R.id.inspectionCameraFragment)
                    true
                }
                R.id.nav_reports -> {
                    navController.navigate(R.id.reportListFragment)
                    true
                }
                R.id.nav_settings -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }
}
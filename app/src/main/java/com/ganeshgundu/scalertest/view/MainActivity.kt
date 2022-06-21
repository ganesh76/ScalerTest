package com.ganeshgundu.scalertest.view


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ganeshgundu.scalertest.R
import com.ganeshgundu.scalertest.app.ScalerTest
import com.ganeshgundu.scalertest.databinding.ActivityMainBinding
import com.ganeshgundu.scalertest.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory((application as ScalerTest).repository)
        ).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_videos, R.id.navigation_history
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        binding.navView.setOnItemSelectedListener {
            if (it.getItemId() != binding.navView.getSelectedItemId())
            NavigationUI.onNavDestinationSelected(it, navController);
            true
        }


    }

    override fun onBackPressed() {
        finish()
    }
}
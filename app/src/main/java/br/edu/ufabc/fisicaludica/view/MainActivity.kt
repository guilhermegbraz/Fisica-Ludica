package br.edu.ufabc.fisicaludica.view

import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

import androidx.lifecycle.ViewModelProvider
import br.edu.ufabc.fisicaludica.R
import br.edu.ufabc.fisicaludica.databinding.ActivityMainBinding
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel

/**
 * Física Lúdica's main activity.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        supportActionBar?.hide()
        //binding.appBarMain.visibility = View.GONE

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


        bindAppBar()

    }

    private fun bindAppBar() {
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.main_activity_pause_button->{
                    val dialog = DialogPause()
                    dialog.show(supportFragmentManager, "pause dialog")
                    true
                }
                else -> false
            }
        }
    }

}

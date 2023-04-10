package br.edu.ufabc.fisicaludica.view

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

import androidx.lifecycle.ViewModelProvider
import br.edu.ufabc.fisicaludica.databinding.ActivityMainBinding
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        supportActionBar?.hide()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        bindEvents()
    }

    fun bindEvents() {
        binding.mainActivityPauseButton.setOnClickListener {
            val dialog = DialogPause()
            dialog.show(supportFragmentManager, "pause dialog")
        }
        binding.mainActivityBackButton.setOnClickListener { onBackPressed() }
    }
}

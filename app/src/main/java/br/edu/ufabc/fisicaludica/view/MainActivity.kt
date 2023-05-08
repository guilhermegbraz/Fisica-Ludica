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
import br.edu.ufabc.fisicaludica.viewmodel.FragmentWindow
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
                    when(viewModel.currentFragmentWindow.value) {

                        FragmentWindow.ListFragment -> {
                            val dialog = DialogPause(listOf(getString(R.string.list_fragment_hint)),0 )
                            dialog.show(supportFragmentManager, "pause dialog")
                        }
                        FragmentWindow.HomeFragment -> {
                            val dialog = DialogPause(listOf(getString(R.string.home_fragment_hint)),0 )
                            dialog.show(supportFragmentManager, "pause dialog")
                        }
                        FragmentWindow.InputGameWindow -> {
                            viewModel.currentHintCollection.value?.let {gameHint->
                                val dialog = DialogPause(gameHint.hints,0 )
                                dialog.show(supportFragmentManager, "pause dialog")
                            }

                        }
                        null -> TODO()
                    }
                    true
                }
                else -> false
            }
        }

        this.viewModel.isAppBarVisible.observe(this){
            if(it) binding.appBarMain.visibility = View.VISIBLE
            else binding.appBarMain.visibility = View.GONE
        }
    }

}

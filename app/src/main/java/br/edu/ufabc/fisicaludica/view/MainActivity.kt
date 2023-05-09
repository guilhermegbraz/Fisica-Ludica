package br.edu.ufabc.fisicaludica.view

import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import br.edu.ufabc.fisicaludica.R
import br.edu.ufabc.fisicaludica.databinding.ActivityMainBinding
import br.edu.ufabc.fisicaludica.viewmodel.FragmentWindow
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel
import com.firebase.ui.auth.AuthUI

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
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        bindAppBar()
        initComponent()

    }
    
    private fun initComponent() {
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE


        getNavController().addOnDestinationChangedListener {_, destination, _ ->
            when(destination.id) {
                R.id.destination_home -> binding.topAppBar.navigationIcon =
                    ContextCompat.getDrawable(this,R.drawable.baseline_logout_24)
                
                else-> binding.topAppBar.navigationIcon =
                    ContextCompat.getDrawable(this,R.drawable.baseline_arrow_back_40)
            }

        }
    }

    private fun getNavController(): NavController {
        val fragment = supportFragmentManager.findFragmentById(binding.mainFragmentContainer.id)
        return (fragment as NavHostFragment).navController
    }


    private fun bindAppBar() {
        binding.topAppBar.setNavigationOnClickListener {
            if (viewModel.currentFragmentWindow.value == FragmentWindow.HomeFragment) AuthUI.getInstance().signOut(this).addOnCompleteListener {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build()
                getNavController().navigate(R.id.authFragment,null, navOptions)
            }
            else if (viewModel.currentFragmentWindow.value == FragmentWindow.AuthenticationFragment) finishAffinity()
            else onBackPressed()
        }

        binding.topAppBar.setOnMenuItemClickListener {
            hintButtonClick(it.itemId)
        }

        this.viewModel.isAppBarVisible.observe(this){
            if(it) binding.appBarMain.visibility = View.VISIBLE
            else binding.appBarMain.visibility = View.GONE
        }
    }

    private fun hintButtonClick(itemId: Int): Boolean {
        return when (itemId) {
            R.id.main_activity_pause_button -> {
                when (viewModel.currentFragmentWindow.value) {
                    FragmentWindow.ListFragment -> {
                        val dialog = DialogPause(listOf(getString(R.string.list_fragment_hint)), 0)
                        dialog.show(supportFragmentManager, "pause dialog")
                    }
                    FragmentWindow.InputGameWindow -> {
                        viewModel.currentHintCollection.value?.let { gameHint ->
                            val dialog = DialogPause(gameHint.hints, 0)
                            dialog.show(supportFragmentManager, "pause dialog")
                        }
                    }
                    else -> {
                        val dialog = DialogPause(listOf(getString(R.string.home_fragment_hint)), 0)
                        dialog.show(supportFragmentManager, "pause dialog")
                    }
                }
                true
            }

            else -> false
        }
    }

}

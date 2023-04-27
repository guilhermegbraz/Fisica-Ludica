package br.edu.ufabc.fisicaludica.view.korge

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.ufabc.fisicaludica.databinding.FragmentKorgeGameViewBinding
import com.soywiz.korge.android.KorgeAndroidView

class KorgeGameViewFragment : Fragment() {
    lateinit var binding: FragmentKorgeGameViewBinding
    private lateinit var korgeAndroidView: KorgeAndroidView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentKorgeGameViewBinding.inflate(inflater,container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        korgeAndroidView = KorgeAndroidView(requireContext())
        binding.toolContainer.addView(korgeAndroidView)

        binding.loadViewButton.setOnClickListener {
            binding.loadViewButton.isEnabled = false
            binding.unloadViewButton.isEnabled = true
            loadToolModule()
        }

        binding.unloadViewButton.setOnClickListener {
            binding.loadViewButton.isEnabled = true
            binding.unloadViewButton.isEnabled = false
            unloadToolModule()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.loadViewButton.isEnabled = true
        binding.unloadViewButton.isEnabled = false

    }

    private fun loadToolModule() {
        val display = DisplayMetrics()

        activity?.windowManager?.defaultDisplay?.getMetrics(display)
        val width = display.widthPixels
        val height = display.heightPixels
        Log.d("tela frag", "As dimensoes no fragment são (${width}, ${height})")
        korgeAndroidView.loadModule(CustomModule(width = width, height = height, callback = {
            println("Callback from android app")
        }))
    }

    private fun unloadToolModule() {
        korgeAndroidView.unloadModule()
    }

}
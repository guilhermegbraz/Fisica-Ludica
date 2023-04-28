package br.edu.ufabc.fisicaludica.view.korge

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.fisicaludica.databinding.FragmentKorgeGameViewBinding
import br.edu.ufabc.fisicaludica.domain.GameGuess
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.android.KorgeAndroidView
import com.soywiz.korio.async.delay

class KorgeGameViewFragment : Fragment() {
    lateinit var binding: FragmentKorgeGameViewBinding
    private lateinit var korgeAndroidView: KorgeAndroidView
    private val viewModel: MainViewModel by activityViewModels()
    val args: KorgeGameViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentKorgeGameViewBinding.inflate(inflater,container, false)
        korgeAndroidView = KorgeAndroidView(requireContext())
        binding.toolContainer.addView(korgeAndroidView)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        bindEvents()

        loadToolModule()
    }
    private fun bindEvents() {
        binding.fragmentGameWindowContinueButton.setOnClickListener {
            KorgeGameViewFragmentDirections.showGameResult(true).let {
                findNavController().navigate(it)
            }
        }
    }

    private fun loadToolModule() {
        val display = DisplayMetrics()

        activity?.windowManager?.defaultDisplay?.getMetrics(display)
        val width = display.widthPixels
        val height = display.heightPixels
        Log.d("tela frag", "As dimensoes no fragment são (${width}, ${height})")

        viewModel.clickedMapId.value?.let {
            val guess = GameGuess(args.initialVelocity.toDouble(), args.initialAngle.toDouble())
            val gameMap =   this.viewModel.getMapById(it)

            val myModule = CustomModule(width = width, height = height, gameMap, guess, callback = {
                println("Callback from android app") },
            )
            korgeAndroidView.loadModule(myModule)
        }
    }

}

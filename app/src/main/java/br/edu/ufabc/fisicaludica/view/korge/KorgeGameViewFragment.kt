package br.edu.ufabc.fisicaludica.view.korge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.fisicaludica.databinding.FragmentKorgeGameViewBinding
import br.edu.ufabc.fisicaludica.model.domain.GameGuess
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel
import com.soywiz.korge.android.KorgeAndroidView

class KorgeGameViewFragment : Fragment() {
    lateinit var binding: FragmentKorgeGameViewBinding
    private lateinit var korgeAndroidView: KorgeAndroidView
    private val viewModel: MainViewModel by activityViewModels()
    val args: KorgeGameViewFragmentArgs by navArgs()
      var buttonClicked = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentKorgeGameViewBinding.inflate(inflater,container, false)
        korgeAndroidView = KorgeAndroidView(requireContext())
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        bindEvents()
    }

    override fun onResume() {
        super.onResume()
        hideAppBar()
        binding.toolContainer.addView(korgeAndroidView)
        viewModel.clickedMapId.value?.let {
            viewModel.getGameLevelById(it).observe(viewLifecycleOwner) { status ->
                when (status) {
                    is MainViewModel.Status.Loading -> {

                    }

                    is MainViewModel.Status.Success -> {
                        val gameLevel = (status.result as MainViewModel.Result.SingleGameLevel).value
                        loadToolModule(gameLevel)
                    }

                    is MainViewModel.Status.Failure -> TODO()
                }
            }
        }

    }

    private fun bindEvents() {
        binding.fragmentGameWindowContinueButton.setOnClickListener {
            this.buttonClicked = true
            KorgeGameViewFragmentDirections.showGameResult(true).let {
                findNavController().navigate(it)
            }
        }
    }

    private fun loadToolModule(gameMap: GameLevel) {
        requireView().post {
            val fragmentHeight = requireView().height //height is ready
            val fragmentWidth = requireView().width

            viewModel.clickedMapId.value?.let {
                val guess = GameGuess(args.initialVelocity.toDouble(), args.initialAngle.toDouble())
                val myModule = CustomModule(
                    width = fragmentWidth, height = fragmentHeight, gameMap, guess,
                    callback = {
                        println("Callback from android app")
                    },
                )
                korgeAndroidView.loadModule(myModule)
            }
        }
    }

    private fun hideAppBar() {
        viewModel.isAppBarVisible.value = false
    }

    override fun onPause() {
        super.onPause()
        korgeAndroidView.unloadModule()
        if (!buttonClicked) binding.fragmentGameWindowContinueButton.performClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.isAppBarVisible.value = true
    }

}

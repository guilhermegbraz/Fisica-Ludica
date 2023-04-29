package br.edu.ufabc.fisicaludica.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.fisicaludica.databinding.FragmentGameInputWindowBinding
import br.edu.ufabc.fisicaludica.domain.Map
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel

/**
 * Fragment for the game input data stage.
 */
class GameInputWindowFragment : Fragment() {
    private lateinit var binding: FragmentGameInputWindowBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameInputWindowBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackground()
        setElementsState()
        bindEvents()
    }

    private fun setBackground() {
        viewModel.clickedMapId.value?.let {
            val gameMap: Map = viewModel.getMapById(it)
            binding.fragmentGameMapLayout.background =
                Drawable.createFromStream(viewModel.getMapBackgroundInputStream(gameMap),gameMap.backgroud)
        }
    }
    private fun setElementsState() {
        viewModel.clickedMapId.value?.let {
            val gameMap: Map = viewModel.getMapById(it)

            binding.gameFragmentSlideBarVelocity.value = gameMap.initialVelocity.toFloat()
            binding.gameFragmentSlideBarTheta.value = gameMap.initialAngleDegrees.toFloat()

            binding.gameFragmentSlideBarVelocity.isEnabled = gameMap.isVelocityVariable
            binding.gameFragmentSlideBarTheta.isEnabled = gameMap.isAngleVariable
            val layoutParams = binding.gameFragmentCannonball.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.horizontalBias =  gameMap.posXLauncher.toFloat() / gameMap.widthMeters.toFloat()

        }
    }

    private fun bindEvents() {
        binding.gameFragmentPlayBotton.setOnClickListener {
            GameInputWindowFragmentDirections.showGameScene(binding.gameFragmentSlideBarVelocity.value,
                binding.gameFragmentSlideBarTheta.value)
                .let {
                    findNavController().navigate(it)
                }
        }
        binding.gameFragmentSlideBarVelocity.addOnChangeListener { _, value, _ ->
            val velocity = "$value m/s"
            binding.gameFragmentTextviewVelocity.text = velocity
        }
        binding.gameFragmentSlideBarTheta.addOnChangeListener { _, value, _ ->
            val angle = "$value m/s"
            binding.gameFragmentTextviewTheta.text = angle
        }
    }
}

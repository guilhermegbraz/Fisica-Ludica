package br.edu.ufabc.fisicaludica.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.fisicaludica.databinding.FragmentMapGameBinding
import br.edu.ufabc.fisicaludica.domain.Map
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel

/**
 * Fragment for the game input data stage.
 */
class MapGameFragment : Fragment() {
    private lateinit var binding: FragmentMapGameBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapGameBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackground()
        bindEvents()

    }

    private fun setBackground() {
        viewModel.clickedMapId.value?.let {
            val gameMap: Map = viewModel.getMapById(it)
            binding.fragmentGameMapLayout.background =
                Drawable.createFromStream(viewModel.getMapBackgroundInputStream(gameMap),gameMap.backgroud)
        }
    }

    private fun bindEvents() {
        binding.gameFragmentPlayBotton.setOnClickListener {
            MapGameFragmentDirections.gameResult(binding.gameFragmentSlideBarVelocity.value == 2.0F)
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

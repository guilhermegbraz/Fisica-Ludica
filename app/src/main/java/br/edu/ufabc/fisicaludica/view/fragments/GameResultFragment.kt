package br.edu.ufabc.fisicaludica.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.fisicaludica.R
import br.edu.ufabc.fisicaludica.databinding.FragmentGameResultBinding
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel

/**
 * Fragment for the game result.
 */
class GameResultFragment : Fragment() {

    private lateinit var binding: FragmentGameResultBinding
    private val args: GameResultFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        createView()
        bindEvents()

    }

    override fun onResume() {
        super.onResume()
        hideAppBar()
    }

    private fun createView() {
        context?.let {context->
            if(args.gameplayResult) {
                binding.gameResultFragmentLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.green_dark))
                binding.gameResultFragmentCard.setBackgroundColor(ContextCompat.getColor(context, R.color.green_light))
                binding.resultGameFragmentImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.trofeu_vencedor))
            }
            else{
                binding.gameResultFragmentLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.red_dark))
                binding.gameResultFragmentCard.setBackgroundColor(ContextCompat.getColor(context, R.color.red_light))
                binding.resultGameFragmentImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.newton_perdedor))
            }
        }
    }

    private fun bindEvents() {
        binding.resultGameFragmentReDoButton.setOnClickListener {
            GameResultFragmentDirections.resultGameTryAgain().let {
                findNavController().navigate(it)
            }
        }

        binding.resultGameFragmentBackToListButton.setOnClickListener {
            GameResultFragmentDirections.resultGameMapList().let {
                findNavController().navigate(it)
            }
        }
    }

    private fun hideAppBar() {
        viewModel.isAppBarVisible.value = false
        Log.d("app bar", "${viewModel.isAppBarVisible.value}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.isAppBarVisible.value = true
    }

}
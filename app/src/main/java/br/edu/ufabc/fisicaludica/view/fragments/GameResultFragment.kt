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
import com.google.android.material.snackbar.Snackbar

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
                binding.resultGameFragmentMainDefaultMessage.text = getString(R.string.default_main_message_for_result_game)
                binding.resultGameFragmentSecundaryDefaultMessage.text = getString(R.string.default_secundary_message_for_result_game)
                callEnableNextLevel()
            }
            else{
                binding.gameResultFragmentLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.red_dark))
                binding.gameResultFragmentCard.setBackgroundColor(ContextCompat.getColor(context, R.color.red_light))
                binding.resultGameFragmentImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.newton_perdedor))
                binding.resultGameFragmentMainDefaultMessage.text = getString(R.string.default_main_message_for_result_game_bad)
                binding.resultGameFragmentSecundaryDefaultMessage.text = getString(R.string.default_secundary_message_for_result_game_bad)
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

    fun callEnableNextLevel() {
        viewModel.enableNextGameLevel(viewModel.clickedMapId.value!!).observe(viewLifecycleOwner) {status->
            when(status) {
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to unlock level next level for player", status.e)
                    Snackbar.make(binding.root, "Failed unlock you the next level", Snackbar.LENGTH_LONG)
                        .show()
                }
                MainViewModel.Status.Loading -> {
                    binding.progressHorizontal.visibility = View.VISIBLE
                }
                is MainViewModel.Status.Success -> {
                    binding.progressHorizontal.visibility = View.GONE
                }
            }
        }
    }

}
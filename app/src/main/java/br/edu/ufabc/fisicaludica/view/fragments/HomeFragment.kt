package br.edu.ufabc.fisicaludica.view.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.fisicaludica.databinding.FragmentHomeBinding
import br.edu.ufabc.fisicaludica.viewmodel.FragmentWindow
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel


/**
 * Home fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.homeFragmentStartImageButton.setOnClickListener {
            HomeFragmentDirections.showMapList().let {
                findNavController().navigate(it)
            }
        }
        viewModel.currentFragmentWindow.value = FragmentWindow.HomeFragment
    }

    override fun onResume() {
        super.onResume()
        requireView().viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                viewModel.fragmentResolutionWidth =view!!.width
                viewModel.fragmentResolutionHeight = view!!.height
            }
        })

    }



}
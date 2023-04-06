package br.edu.ufabc.fisicaludica.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.fisicaludica.databinding.FragmentHomeBinding



class HomeFragment() : Fragment() {

    private lateinit var binding: FragmentHomeBinding
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
    }


}
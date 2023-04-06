package br.edu.ufabc.fisicaludica.view.maplistfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import br.edu.ufabc.fisicaludica.databinding.FragmentMapListBinding
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel


class MapListFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding:FragmentMapListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentMapListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerviewMapList.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerviewMapList.apply {
            adapter = MapAdapter(viewModel.getAllMaps(), viewModel)
        }
    }

}
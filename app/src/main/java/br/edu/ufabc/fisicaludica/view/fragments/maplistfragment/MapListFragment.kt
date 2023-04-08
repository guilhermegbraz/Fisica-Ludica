package br.edu.ufabc.fisicaludica.view.fragments.maplistfragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.fisicaludica.databinding.FragmentMapListBinding
import br.edu.ufabc.fisicaludica.databinding.MapListItemBinding
import br.edu.ufabc.fisicaludica.domain.Map
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel

class MapListFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding:FragmentMapListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentMapListBinding.inflate(inflater, container, false)

        return binding.root
    }

    inner class MapAdapter(private val maps:List<Map>, val viewModel: MainViewModel): RecyclerView.Adapter<MapHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapHolder =
            MapHolder(
                MapListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            )

        override fun getItemCount(): Int = maps.size

        override fun onBindViewHolder(holder: MapHolder, position: Int) {
            val map = maps[position]
            holder.title.text = map.title
            holder.imageView.setImageDrawable(Drawable.createFromStream(viewModel.getMapBackgroundInputStream(map), map.backgroud))

            holder.card.setOnClickListener{
                viewModel.clickedMapId.value = map.id
                MapListFragmentDirections.goToGameFragment().let { action ->
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerviewMapList.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerviewMapList.apply {
            adapter = MapAdapter(viewModel.getAllMaps(), viewModel)
        }

    }
}

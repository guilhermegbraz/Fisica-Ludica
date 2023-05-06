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
import br.edu.ufabc.fisicaludica.databinding.FragmentGameLevelListBinding
import br.edu.ufabc.fisicaludica.databinding.MapListItemBinding
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel

/**
 * Fragment for list the game maps.
 */
class GameLevelListFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding:FragmentGameLevelListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentGameLevelListBinding.inflate(inflater, container, false)

        return binding.root
    }

    /**
     * RecyclerView adapter.
     */
    inner class GameLevelAdapter(private val gameLevels:List<GameLevel>): RecyclerView.Adapter<GameLevelHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameLevelHolder =
            GameLevelHolder(
                MapListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            )

        override fun getItemCount(): Int = gameLevels.size

        override fun onBindViewHolder(holder: GameLevelHolder, position: Int) {
            val map = gameLevels[position]
            holder.title.text = map.title
            holder.imageView.setImageDrawable(
                Drawable.createFromStream(viewModel.getMapBackgroundInputStream(map), map.backgroud)
            )

            holder.card.setOnClickListener{
                viewModel.clickedMapId.value = map.id
                GameLevelListFragmentDirections.goToGameFragment().let { action ->
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerviewMapList.layoutManager = GridLayoutManager(context, 3)
        viewModel.isDataReady.observe(viewLifecycleOwner) {
            if(it) {
                binding.progressHorizontal.visibility = View.GONE
                binding.recyclerviewMapList.apply {
                    adapter = GameLevelAdapter(viewModel.getAllMaps())
                }
            } else {
                binding.progressHorizontal.visibility = View.VISIBLE
            }
        }


    }
}

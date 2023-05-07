package br.edu.ufabc.fisicaludica.view.fragments.maplistfragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
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
                Drawable.createFromStream(viewModel.getMapBackgroundInputStream(map), map.backgroudUrl)
            )

            holder.card.setOnClickListener{
                viewModel.clickedMapId.value = map.id
                Log.d("list fragment", "o usuario clicou no level de id: ${viewModel.clickedMapId.value}")
                GameLevelListFragmentDirections.goToGameFragment().let { action ->
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerviewMapList.layoutManager = GridLayoutManager(context, 3)
        viewModel.getAllMaps().observe(viewLifecycleOwner) { status->
            when(status) {
                is MainViewModel.Status.Loading -> {
                    binding.progressHorizontal.visibility = View.VISIBLE
                }
                is MainViewModel.Status.Success -> {

                    val gameLevelList = (status.result as MainViewModel.Result.GameLevelList).value
                    binding.progressHorizontal.visibility = View.INVISIBLE
                    binding.recyclerviewMapList.apply {
                        adapter = GameLevelAdapter(gameLevelList.sortedBy { it.id })
                    }
                }

                is MainViewModel.Status.Failure -> {
                    Log.d("Status", "Obtivemos Status Failure ")
                    //status.e.message?.let { Log.d("erro db", it) }
                }
            }
        }

    }
}

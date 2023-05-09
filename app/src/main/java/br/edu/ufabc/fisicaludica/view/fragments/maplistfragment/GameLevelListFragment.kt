package br.edu.ufabc.fisicaludica.view.fragments.maplistfragment

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.fisicaludica.databinding.FragmentGameLevelListBinding
import br.edu.ufabc.fisicaludica.databinding.MapListItemBinding
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import br.edu.ufabc.fisicaludica.viewmodel.FragmentWindow
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

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
            val gameLevel = gameLevels[position]
            holder.title.text = gameLevel.title

            val originalDrawable = Drawable.createFromStream(viewModel.getMapBackgroundInputStream(gameLevel), gameLevel.backgroudUrl)!!

            if(gameLevel.isEnable.not()) {
                val colorMatrix = ColorMatrix().apply {
                    setSaturation(0f)
                }
                val blackAndWhiteDrawable = DrawableCompat.wrap(originalDrawable).mutate()
                blackAndWhiteDrawable.colorFilter = ColorMatrixColorFilter(colorMatrix)
                holder.imageView.setImageDrawable(
                    blackAndWhiteDrawable
                )
            } else holder.imageView.setImageDrawable(originalDrawable)

            if(gameLevel.isEnable) {
                holder.card.setOnClickListener{
                    viewModel.clickedMapId.value = gameLevel.id
                    Log.d("list fragment", "o card eh clickavel? ${it.isClickable}")
                    GameLevelListFragmentDirections.goToGameFragment().let { action ->
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerviewMapList.layoutManager = GridLayoutManager(context, 3)
        viewModel.currentFragmentWindow.value = FragmentWindow.ListFragment
        viewModel.getAllGameLevels().observe(viewLifecycleOwner) { status->
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
                    Log.e("FRAGMENT", "Failed list itens", status.e)
                    Snackbar.make(binding.root, "Failed to list your game levels", Snackbar.LENGTH_LONG)
                        .show()
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
            }
        }

    }
}

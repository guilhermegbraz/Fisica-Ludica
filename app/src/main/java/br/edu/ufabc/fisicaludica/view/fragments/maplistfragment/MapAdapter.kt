package br.edu.ufabc.fisicaludica.view.fragments.maplistfragment
/*
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.fisicaludica.databinding.MapListItemBinding
import br.edu.ufabc.fisicaludica.domain.Map
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel

class MapAdapter(private val maps:List<Map>, val viewModel: MainViewModel): RecyclerView.Adapter<MapHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapHolder =
        MapHolder(MapListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        )


    override fun getItemCount(): Int = maps.size

    override fun onBindViewHolder(holder: MapHolder, position: Int) {
        val map = maps[position]
        holder.title.text = map.title
        holder.imageView.setImageDrawable(Drawable.createFromStream(viewModel.getMapBackgroundInputStream(map), map.backgroud))
        holder.card.setOnClickListener{
            viewModel.clickedMapId.value = map.id
        }
    }


}


 */
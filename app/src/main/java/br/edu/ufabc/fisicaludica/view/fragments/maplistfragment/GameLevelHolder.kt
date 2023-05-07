package br.edu.ufabc.fisicaludica.view.fragments.maplistfragment

import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.fisicaludica.databinding.MapListItemBinding

/**
 * RecyclerView holder.
 */
class GameLevelHolder(itemBinding: MapListItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    /**
     * card view.
     */
    val card = itemBinding.mapListItemCard

    /**
     * title.
     */
    val title = itemBinding.mapListItemCardTitle

    /**
     * Game map image preview.
     */
    val imageView = itemBinding.mapListItemCardImage

}
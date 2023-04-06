package br.edu.ufabc.fisicaludica.view.maplistfragment

import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.fisicaludica.databinding.MapListItemBinding

class MapHolder(itemBinding: MapListItemBinding) :
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
     * image preview.
     */
    val imageView = itemBinding.mapListItemCardImage

}
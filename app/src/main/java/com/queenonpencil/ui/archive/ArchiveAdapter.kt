package com.queenonpencil.ui.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.queenonpencil.data.entity.Grafting
import com.queenonpencil.databinding.ItemArchiveBinding
import com.queenonpencil.util.toDisplayDate

class ArchiveAdapter(
    private val onClick: (Long) -> Unit,
    private val onDelete: (Long) -> Unit
) : ListAdapter<Grafting, ArchiveAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Grafting>() {
            override fun areItemsTheSame(a: Grafting, b: Grafting) = a.id == b.id
            override fun areContentsTheSame(a: Grafting, b: Grafting) = a == b
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemArchiveBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val b: ItemArchiveBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(g: Grafting) {
            b.tvDate.text = g.dt.toDisplayDate()
            b.tvDesc.text = g.desc.ifBlank { "Без описания" }
            b.root.setOnClickListener { onClick(g.id) }
            b.btnDelete.setOnClickListener { onDelete(g.id) }
        }
    }
}

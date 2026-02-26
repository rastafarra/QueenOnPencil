package com.queenonpencil.ui.journal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.queenonpencil.data.dao.CalendarEvent
import com.queenonpencil.databinding.ItemJournalEntryBinding
import com.queenonpencil.databinding.ItemJournalHeaderBinding
import com.queenonpencil.util.toDisplayDate

sealed class JournalItem {
    data class Header(val graftingDt: String, val graftingDesc: String) : JournalItem()
    data class Entry(val event: CalendarEvent) : JournalItem()
}

class JournalAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = listOf<JournalItem>()

    fun submitList(events: List<CalendarEvent>) {
        val grouped = events.groupBy { it.graftingId }
        val result = mutableListOf<JournalItem>()
        for ((_, evts) in grouped) {
            val first = evts.first()
            result.add(JournalItem.Header(first.graftingDt, first.graftingDesc))
            evts.forEach { result.add(JournalItem.Entry(it)) }
        }
        items = result
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = when (items[position]) {
        is JournalItem.Header -> 0
        is JournalItem.Entry -> 1
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            HeaderVH(ItemJournalHeaderBinding.inflate(inflater, parent, false))
        } else {
            EntryVH(ItemJournalEntryBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is JournalItem.Header -> (holder as HeaderVH).bind(item)
            is JournalItem.Entry -> (holder as EntryVH).bind(item)
        }
    }

    inner class HeaderVH(private val b: ItemJournalHeaderBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(item: JournalItem.Header) {
            b.tvHeader.text = "Прививка ${item.graftingDt.toDisplayDate()}" +
                    if (item.graftingDesc.isNotBlank()) " — ${item.graftingDesc}" else ""
        }
    }

    inner class EntryVH(private val b: ItemJournalEntryBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(item: JournalItem.Entry) {
            b.tvEventInfo.text = "${item.event.eventDt.toDisplayDate()} · ${item.event.eventDesc}"
            b.tvNote.text = item.event.eventNote
        }
    }
}

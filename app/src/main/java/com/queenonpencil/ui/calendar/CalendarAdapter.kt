package com.queenonpencil.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.queenonpencil.data.dao.CalendarEvent
import com.queenonpencil.databinding.ItemCalendarEventBinding
import com.queenonpencil.databinding.ItemCalendarHeaderBinding
import com.queenonpencil.util.toDisplayDate

sealed class CalendarItem {
    data class Header(val date: String) : CalendarItem()
    data class EventItem(val event: CalendarEvent) : CalendarItem()
}

class CalendarAdapter(
    private val onEventClick: (Long) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = listOf<CalendarItem>()

    fun submitList(events: List<CalendarEvent>) {
        val grouped = events.groupBy { it.eventDt }
        val result = mutableListOf<CalendarItem>()
        for ((date, evts) in grouped) {
            result.add(CalendarItem.Header(date))
            evts.forEach { result.add(CalendarItem.EventItem(it)) }
        }
        items = result
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = when (items[position]) {
        is CalendarItem.Header -> 0
        is CalendarItem.EventItem -> 1
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            HeaderVH(ItemCalendarHeaderBinding.inflate(inflater, parent, false))
        } else {
            EventVH(ItemCalendarEventBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is CalendarItem.Header -> (holder as HeaderVH).bind(item)
            is CalendarItem.EventItem -> (holder as EventVH).bind(item)
        }
    }

    inner class HeaderVH(private val b: ItemCalendarHeaderBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(item: CalendarItem.Header) {
            b.tvDate.text = item.date.toDisplayDate()
        }
    }

    inner class EventVH(private val b: ItemCalendarEventBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(item: CalendarItem.EventItem) {
            b.tvEventDesc.text = item.event.eventDesc
            b.tvGraftInfo.text = "Прививка ${item.event.graftingDt.toDisplayDate()}" +
                    if (item.event.graftingDesc.isNotBlank()) " — ${item.event.graftingDesc}" else ""
            b.root.setOnClickListener { onEventClick(item.event.graftingId) }
        }
    }
}

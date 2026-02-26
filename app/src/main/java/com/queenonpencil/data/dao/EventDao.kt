package com.queenonpencil.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.queenonpencil.data.entity.Event

data class CalendarEvent(
    val eventId: Long,
    val eventDt: String,
    val eventDesc: String,
    val graftingId: Long,
    val graftingDt: String,
    val graftingDesc: String,
    val graftingShift: Int
)

@Dao
interface EventDao {
    @Insert
    suspend fun insertAll(events: List<Event>)

    @Query("DELETE FROM events WHERE grafting_id = :graftingId")
    suspend fun deleteByGraftingId(graftingId: Long)

    @Query("""
        SELECT events.id AS eventId, events.dt AS eventDt, events.desc AS eventDesc,
               grafting.id AS graftingId, grafting.dt AS graftingDt,
               grafting.desc AS graftingDesc, grafting.shift AS graftingShift
        FROM events
        INNER JOIN grafting ON events.grafting_id = grafting.id
        WHERE events.dt BETWEEN date('now','-1 day') AND date('now','+14 days')
        ORDER BY events.dt, grafting.dt
    """)
    fun getUpcomingEvents(): LiveData<List<CalendarEvent>>

    @Query("SELECT * FROM events WHERE dt >= date('now') ORDER BY dt")
    suspend fun getFutureEvents(): List<Event>

    @Query("SELECT id FROM events WHERE grafting_id = :graftingId")
    suspend fun getIdsByGraftingId(graftingId: Long): List<Long>
}

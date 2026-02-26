package com.queenonpencil.ui.archive

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.queenonpencil.data.AppDatabase
import com.queenonpencil.notification.AlarmScheduler
import kotlinx.coroutines.launch

class ArchiveViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.getInstance(app)
    val graftings = db.graftingDao().getAllDesc()

    fun delete(id: Long) {
        viewModelScope.launch {
            val eventIds = db.eventDao().getIdsByGraftingId(id)
            eventIds.forEach { AlarmScheduler.cancelEvent(getApplication(), it) }
            db.graftingDao().deleteById(id)
        }
    }
}

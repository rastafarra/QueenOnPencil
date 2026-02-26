package com.queenonpencil.ui.journal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.queenonpencil.data.AppDatabase

class JournalViewModel(app: Application) : AndroidViewModel(app) {
    private val eventDao = AppDatabase.getInstance(app).eventDao()
    val events = eventDao.getEventsWithNotes()
}

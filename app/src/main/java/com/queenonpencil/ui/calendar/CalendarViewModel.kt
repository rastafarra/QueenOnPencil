package com.queenonpencil.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.queenonpencil.data.AppDatabase

class CalendarViewModel(app: Application) : AndroidViewModel(app) {
    private val eventDao = AppDatabase.getInstance(app).eventDao()
    val events = eventDao.getUpcomingEvents()
}

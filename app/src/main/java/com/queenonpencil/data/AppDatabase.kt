package com.queenonpencil.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.queenonpencil.data.dao.EventDao
import com.queenonpencil.data.dao.GraftingDao
import com.queenonpencil.data.entity.Event
import com.queenonpencil.data.entity.Grafting

@Database(entities = [Grafting::class, Event::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun graftingDao(): GraftingDao
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bqc.db"
                ).build().also { INSTANCE = it }
            }
    }
}

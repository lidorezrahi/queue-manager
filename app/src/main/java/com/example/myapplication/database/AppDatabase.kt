package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.database.dao.QueuedRequestDao
import com.example.myapplication.database.entities.QueuedRequest

@Database(
    entities = [QueuedRequest::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun queuedRequestDao(): QueuedRequestDao

    companion object {
        private const val DB_NAME = "app_database"

        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}

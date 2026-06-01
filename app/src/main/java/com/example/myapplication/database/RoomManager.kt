package com.example.myapplication.database

import androidx.room.withTransaction
import com.example.myapplication.database.dao.QueuedRequestDao

interface RoomManager {
    fun queuedRequests(): QueuedRequestDao
    suspend fun runInTransaction(block: suspend () -> Unit)
    suspend fun clearDatabase()
}

class RoomManagerImpl(private val db: AppDatabase) : RoomManager {
    override fun queuedRequests(): QueuedRequestDao = db.queuedRequestDao()

    override suspend fun runInTransaction(block: suspend () -> Unit) {
        db.withTransaction { block() }
    }

    override suspend fun clearDatabase() {
        db.clearAllTables()
    }
}

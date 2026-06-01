package com.example.myapplication.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.database.entities.QueuedRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface QueuedRequestDao : BaseDao<QueuedRequest> {
    @Query("SELECT * FROM queued_requests ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<QueuedRequest>>

    @Query("SELECT * FROM queued_requests WHERE id = :id")
    suspend fun getById(id: Int): QueuedRequest?

    @Query("DELETE FROM queued_requests WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM queued_requests")
    suspend fun deleteAll()

    suspend fun create(url: String, method: String, body: String? = null): Long =
        insert(QueuedRequest(url = url, method = method, body = body, createdAt = System.currentTimeMillis()))
}

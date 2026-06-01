package com.example.myapplication.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queued_requests")
data class QueuedRequest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String,
    val method: String,
    val body: String? = null,
    val retryCount: Int = 0,
    val createdAt: Long
)

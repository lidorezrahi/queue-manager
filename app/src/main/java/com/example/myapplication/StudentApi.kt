package com.example.myapplication

import kotlinx.serialization.Serializable

// The Data Model
@Serializable
data class Student(
    val id: String,
    val name: String,
    val grade: Int
)

@Serializable
data class StudentResponse(
    val students: List<Student>
)

// The API Interface
interface StudentApi {
    suspend fun getStudents(): NetworkResult<StudentResponse>
    suspend fun updateStudent(student: Student): NetworkResult<Unit>
}

// The API Implementation
class StudentApiImpl(
    private val networkManager: NetworkManager
) : StudentApi {

    override suspend fun getStudents(): NetworkResult<StudentResponse> {
        return networkManager.get(path = "/students")
    }

    override suspend fun updateStudent(student: Student): NetworkResult<Unit> {
        return networkManager.put(
            path = "/students/${student.id}",
            body = student
        )
    }
}
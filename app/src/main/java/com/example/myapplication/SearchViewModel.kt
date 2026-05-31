package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class StudentViewModel : ViewModel() {
    data class ScreenState(
        val students: List<Student> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val isOfflineMode: Boolean = false
    )

    abstract val state: StateFlow<ScreenState>

    abstract fun loadStudents()
    abstract fun updateStudentGrade(student: Student, newGrade: Int)
}

class StudentViewModelImpl(
    private val studentApi: StudentApi,
) : StudentViewModel() {

    private val _state = MutableStateFlow(ScreenState())
    override val state: StateFlow<ScreenState> = _state

    init {
        loadStudents()
    }

    override fun loadStudents() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = studentApi.getStudents()) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            students = result.data.students,
                            isLoading = false,
                            error = null
                        )
                    }
                }

                is NetworkResult.NetworkError -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Offline: Showing cached data",
                            isOfflineMode = true
                        )
                    }
                }

                else -> {
                    _state.update { it.copy(isLoading = false, error = "Server Error") }
                }
            }
        }
    }

    override fun updateStudentGrade(student: Student, newGrade: Int) {
        val updatedStudent = student.copy(grade = newGrade)

        viewModelScope.launch {
            // Optimistic UI update: Change the UI immediately
            updateLocalStudentList(updatedStudent)

            // Attempt to sync with server
            when (studentApi.updateStudent(updatedStudent)) {
                is NetworkResult.Success -> {
                    // Successfully updated on server
                }

                is NetworkResult.NetworkError -> {
                    // TODO: Enqueue via QueueManager when implemented
                    _state.update { it.copy(error = "Offline: Update queued") }
                }

                else -> {
                    _state.update { it.copy(error = "Failed to update student") }
                }
            }
        }
    }

    private fun updateLocalStudentList(updatedStudent: Student) {
        _state.update { currentState ->
            val newList = currentState.students.map {
                if (it.id == updatedStudent.id) updatedStudent else it
            }
            currentState.copy(students = newList)
        }
    }
}
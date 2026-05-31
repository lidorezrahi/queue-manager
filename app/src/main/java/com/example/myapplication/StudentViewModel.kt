package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class StudentViewModel : ViewModel() {
    data class ScreenState(
        val students: List<Student> = emptyList()
    )

    abstract val state: StateFlow<ScreenState>

    abstract fun loadStudents()
}

class StudentViewModelImpl(
    private val studentApi: StudentApi,
) : StudentViewModel() {

    private val _state = MutableStateFlow(ScreenState())
    override val state: StateFlow<ScreenState> = _state

    override fun loadStudents() {
        viewModelScope.launch {
            when (val result = studentApi.getStudents()) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(students = result.data.students) }
                }

                else -> {}
            }
        }
    }
}

package com.sloth.proactify.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sloth.proactify.data.remote.response.Results
import com.sloth.proactify.domain.usecase.TaskUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val useCase: TaskUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _stateSave = MutableStateFlow(MainState())
    val stateSave = _stateSave.asStateFlow()

    private val _stateIsEdit = MutableStateFlow(MainState())
    val stateIsEdit = _stateIsEdit.asStateFlow()

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.GetData -> loadData(event)
            is MainEvent.SaveData -> saveData(event)
            is MainEvent.IsEdit -> isEdit(event)
        }
    }

    private fun isEdit(event: MainEvent.IsEdit) {
        if (_stateIsEdit.value.isLoading) return

        _stateIsEdit.update {
            it.copy(isLoading = true)
        }

        _stateIsEdit.update {
            it.copy(isEdit = true, isReadOnly = event.readOnly, editData = event.data)
        }

        viewModelScope.launch {
            delay(200)
            _stateIsEdit.update { it.copy(isLoading = false, isEdit = false, isReadOnly = false) }
        }
    }

    private fun loadData(event: MainEvent.GetData) {

        if (_state.value.isLoading) return

        _state.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            useCase[event.id].collect { result ->
                when(result) {
                    is Results.Success -> {
                        _state.update {
                            it.copy(
                                data = result.data,
                                isLoading = false,
                                isSuccess = true,
                            )
                        }
                    }
                    is Results.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isFailure = true,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun saveData(event: MainEvent.SaveData) {

        if (_stateSave.value.isLoading) return

        _stateSave.update {
            it.copy(isLoading = true)
        }

        if (event.data.id != 0L) {
            viewModelScope.launch {
                Log.d("Edit", event.data.toString())
                useCase.update(event.data).collect { result ->
                    when (result) {
                        is Results.Success -> {
                            _stateSave.update {
                                it.copy(
                                    message = result.data,
                                    isLoading = false,
                                    isSuccess = true,
                                )

                            }
                            loadData(MainEvent.GetData(""))
                        }

                        is Results.Error -> {
                            _stateSave.update {
                                it.copy(
                                    isLoading = false,
                                    isFailure = true,
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Log.d("Save", "Save")
            viewModelScope.launch {
                useCase.save(event.data).collect { result ->
                    when (result) {
                        is Results.Success -> {
                            _stateSave.update {
                                it.copy(
                                    message = result.data,
                                    isLoading = false,
                                    isSuccess = true,
                                )

                            }
                            loadData(MainEvent.GetData(""))
                        }

                        is Results.Error -> {
                            _stateSave.update {
                                it.copy(
                                    isLoading = false,
                                    isFailure = true,
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

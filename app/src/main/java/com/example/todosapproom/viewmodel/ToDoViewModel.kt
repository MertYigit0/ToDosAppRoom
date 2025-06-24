package com.example.todosapproom.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todosapproom.R
import com.example.todosapproom.data.ToDo
import com.example.todosapproom.repository.ToDoRepository
import kotlinx.coroutines.launch

class ToDoViewModel(
    private val repository: ToDoRepository,
    private val context: Context
) : ViewModel() {
    
    val toDos: LiveData<List<ToDo>> = repository.allToDos
    
    private val _searchResults = MutableLiveData<List<ToDo>>()
    val searchResults: LiveData<List<ToDo>> = _searchResults
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    
    fun searchToDos(query: String) {
        if (query.isBlank()) {
            _searchResults.value = toDos.value ?: emptyList()
            return
        }
        
        viewModelScope.launch {
            try {
                val currentTodos = toDos.value ?: emptyList()
                val filteredTodos = currentTodos.filter { 
                    it.name.contains(query, ignoreCase = true) 
                }
                _searchResults.value = filteredTodos
            } catch (e: Exception) {
                _message.value = context.getString(R.string.error_searching_todos, e.message)
            }
        }
    }
    
    fun addToDo(name: String) {
        if (name.isBlank()) {
            _message.value = context.getString(R.string.error_empty_todo_name)
            return
        }
        
        viewModelScope.launch {
            try {
                val toDo = ToDo(name = name.trim())
                repository.insertToDo(toDo)
                _message.value = context.getString(R.string.todo_added_successfully)
            } catch (e: Exception) {
                _message.value = context.getString(R.string.error_adding_todo, e.message)
            }
        }
    }
    
    fun updateToDo(toDo: ToDo) {
        if (toDo.name.isBlank()) {
            _message.value = context.getString(R.string.error_empty_todo_name)
            return
        }
        
        viewModelScope.launch {
            try {
                repository.updateToDo(toDo)
                _message.value = context.getString(R.string.todo_updated_successfully)
            } catch (e: Exception) {
                _message.value = context.getString(R.string.error_updating_todo, e.message)
            }
        }
    }
    
    fun deleteToDo(toDo: ToDo) {
        viewModelScope.launch {
            try {
                repository.deleteToDo(toDo)
                _message.value = context.getString(R.string.todo_deleted_successfully)
            } catch (e: Exception) {
                _message.value = context.getString(R.string.error_deleting_todo, e.message)
            }
        }
    }
    
    fun getToDoById(id: Int): ToDo? {
        return toDos.value?.find { it.id == id }
    }
    
    fun clearMessage() {
        _message.value = ""
    }
} 
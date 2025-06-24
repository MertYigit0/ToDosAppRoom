package com.example.todosapproom.repository

import androidx.lifecycle.LiveData
import com.example.todosapproom.data.ToDo
import com.example.todosapproom.data.ToDoDao

class ToDoRepository(private val toDoDao: ToDoDao) {
    
    val allToDos: LiveData<List<ToDo>> = toDoDao.getAllToDos()
    
    fun searchToDos(searchQuery: String): LiveData<List<ToDo>> {
        return toDoDao.searchToDos(searchQuery)
    }
    
    suspend fun insertToDo(toDo: ToDo) {
        toDoDao.insertToDo(toDo)
    }
    
    suspend fun updateToDo(toDo: ToDo) {
        toDoDao.updateToDo(toDo)
    }
    
    suspend fun deleteToDo(toDo: ToDo) {
        toDoDao.deleteToDo(toDo)
    }
    
    suspend fun getToDoById(id: Int): ToDo? {
        return toDoDao.getToDoById(id)
    }
} 
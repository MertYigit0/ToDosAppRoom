package com.example.todosapproom.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoDao {
    
    @Query("SELECT * FROM toDos ORDER BY id DESC")
    fun getAllToDos(): LiveData<List<ToDo>>
    
    @Query("SELECT * FROM toDos WHERE name LIKE '%' || :searchQuery || '%' ORDER BY id DESC")
    fun searchToDos(searchQuery: String): LiveData<List<ToDo>>
    
    @Insert
    suspend fun insertToDo(toDo: ToDo)
    
    @Update
    suspend fun updateToDo(toDo: ToDo)
    
    @Delete
    suspend fun deleteToDo(toDo: ToDo)
    
    @Query("SELECT * FROM toDos WHERE id = :id")
    suspend fun getToDoById(id: Int): ToDo?
} 
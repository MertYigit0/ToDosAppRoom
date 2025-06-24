package com.example.todosapproom.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "toDos")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
) 
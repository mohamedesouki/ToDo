package org.first.todo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.first.todo.data.ToDoDataBase
import org.first.todo.data.models.ToDoData
import org.first.todo.data.repository.ToDoRepository

class ToDoViewModel(application: Application ):AndroidViewModel(application) {

    private val todoDao = ToDoDataBase.getDataBase(application).todoDao()
    private val repository :ToDoRepository
    val sortByHighPriority :LiveData<List<ToDoData>>
    val sortByLowPriority :LiveData<List<ToDoData>>

     val getAllData : LiveData<List<ToDoData>>

    init {
        repository = ToDoRepository(todoDao)
        getAllData = repository.getAllData
        sortByHighPriority = repository.sortByHighPriority
        sortByLowPriority = repository.sortByLowPriority
    }
    fun insertData(toDoData: ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertData(toDoData)
        }
    }
    fun updateData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }
    fun deleteData(toDoData: ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteData(toDoData)
        }
    }
    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
    fun searchDatabase(searchQuery : String):LiveData<List<ToDoData>>{
        return repository.searchDatabase(searchQuery)
    }

}
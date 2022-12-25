package org.first.todo.data.viewmodel

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.first.todo.R
import org.first.todo.data.models.Priority
import org.first.todo.data.models.ToDoData

class SharedViewModel(application: Application) :AndroidViewModel(application) {
    val emptyDataBase :MutableLiveData<Boolean> = MutableLiveData(false)
    fun checkIfDatabaseEmpty(toDoData: List<ToDoData>){
        emptyDataBase.value = toDoData.isEmpty()
    }


    val listener : AdapterView.OnItemSelectedListener = object :
    AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(position){
                0->{(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))}
                1->{(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green))}
                2->{(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow))}
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
    }
     fun verifyDataFromUser(title : String,description :String) :Boolean{
       return !(title.isEmpty() || description.isEmpty())

    }
     fun parsePriority(priority: String): Priority {
        return when(priority){
            "High Priority"->{
                Priority.HIGH}
            "Medium Priority"->{
                Priority.MEDIUM}
            "Low Priority"->{
                Priority.LOW}
            else -> Priority.LOW
        }
    }
     fun parsePriorityToInt(priority : Priority) : Int{
        return when(priority){
            Priority.HIGH ->0
            Priority.MEDIUM ->1
            Priority.LOW ->2
        }
    }
}
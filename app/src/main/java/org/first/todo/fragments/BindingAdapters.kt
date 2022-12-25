package org.first.todo.fragments

import android.view.View
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.first.todo.R
import org.first.todo.data.models.Priority
import org.first.todo.data.models.ToDoData
import org.first.todo.fragments.list.ListFragmentDirections

class BindingAdapters {
    companion object{

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view :FloatingActionButton,navigate:Boolean){
            view.setOnClickListener{
                if(navigate){
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }
        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase :MutableLiveData<Boolean>){
            when(emptyDatabase.value){
                true ->view.visibility = View.VISIBLE
                false ->view.visibility = View.INVISIBLE
            }
        }
        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(cardView: CardView,priority: Priority){
            when(priority){
                Priority.HIGH ->{cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))}
                Priority.MEDIUM ->{cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))}
                Priority.LOW ->{cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))}
            }
        }
        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: ConstraintLayout,currentItem :ToDoData){
            view.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }

    }
}
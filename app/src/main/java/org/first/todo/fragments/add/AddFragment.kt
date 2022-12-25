package org.first.todo.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import org.first.todo.R
import org.first.todo.data.models.Priority
import org.first.todo.data.models.ToDoData
import org.first.todo.data.viewmodel.SharedViewModel
import org.first.todo.data.viewmodel.ToDoViewModel

class AddFragment : Fragment() {
    private val mToDoViewModel :ToDoViewModel by viewModels()
    private val mSharedViewModel :SharedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        setHasOptionsMenu(true)
        view.priorities_spinner.onItemSelectedListener = mSharedViewModel.listener
        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_fragment,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add){
            insertDataToDB()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDB() {
        val title = title_editText.text.toString()
        val mPriority = priorities_spinner.selectedItem.toString()
        val description = description_editText.text.toString()
        val validation = mSharedViewModel.verifyDataFromUser(title,description)
        if (validation){
            val newData = ToDoData(
                 0,
                title,
                description,
                mSharedViewModel.parsePriority(mPriority)
            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(),"Successfully added!",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Please fill out all fields",Toast.LENGTH_SHORT).show()
        }
    }


}
package org.first.todo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import org.first.todo.R
import org.first.todo.data.models.Priority
import org.first.todo.data.models.ToDoData
import org.first.todo.data.viewmodel.SharedViewModel
import org.first.todo.data.viewmodel.ToDoViewModel

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel :SharedViewModel by viewModels()
    private val mToDoViewModel :ToDoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_update, container, false)
        setHasOptionsMenu(true)
        view.current_title_editText.setText(args.currentItem.title)
        view.current_description_editText.setText(args.currentItem.description)
        view.current_priorities_spinner.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))
        view.current_priorities_spinner.onItemSelectedListener = mSharedViewModel.listener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_update_fragment,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save-> updateItem()
            R.id.menu_delete ->confirmRemoval()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemoval() {
       val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            mToDoViewModel.deleteData(args.currentItem)
            Toast.makeText(requireContext(),"Successfully Removed! :${args.currentItem.title}",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_ -> }
        builder.setTitle("Delete '${args.currentItem.title}'?")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.title}'")
        builder.create().show()
    }

    private fun updateItem() {
        val title = current_title_editText.text.toString()
        val description = current_description_editText.text.toString()
        val getPriority = current_priorities_spinner.selectedItem.toString()
        val validation = mSharedViewModel.verifyDataFromUser(title,description)
        if (validation){
            val updateItem = ToDoData(
                args.currentItem.id,
                title,
                description,
                mSharedViewModel.parsePriority(getPriority)
            )
            mToDoViewModel.updateData(updateItem)
            Toast.makeText(requireContext(),"Successfully Updated!",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Please fill out all fields",Toast.LENGTH_SHORT).show()
        }
    }

}
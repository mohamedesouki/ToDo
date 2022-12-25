package org.first.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.first.todo.R
import org.first.todo.data.models.ToDoData
import org.first.todo.data.utils.hideKeyBoard
import org.first.todo.data.viewmodel.SharedViewModel
import org.first.todo.data.viewmodel.ToDoViewModel
import org.first.todo.databinding.FragmentListBinding
import org.first.todo.fragments.list.adapter.ListAdapter

class ListFragment : Fragment(),SearchView.OnQueryTextListener {

    val mToDoViewModel :ToDoViewModel by viewModels()
    val msharedViewModel :SharedViewModel by viewModels()
    val adapter : ListAdapter by lazy { ListAdapter() }

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list, container, false)
        _binding = FragmentListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = msharedViewModel


     setUpRecyclerView()
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data->
            msharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)

        })
       /* msharedViewModel.emptyDataBase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseViews(it)
        })*/



        setHasOptionsMenu(true)
        hideKeyBoard(requireActivity())
        return binding.root

    }

    private fun setUpRecyclerView() {
        val recyclerView =binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = LandingAnimator().apply {
            addDuration = 300
        }
        swipeToDelete(recyclerView)
    }
    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallBack =object :SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                mToDoViewModel.deleteData(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                restoreDeletedItem(viewHolder.itemView,deletedItem,viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun restoreDeletedItem(view :View,deletedItem :ToDoData,position:Int){
        val snackBar =Snackbar.make(
            view,"Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo"){
            mToDoViewModel.insertData(deletedItem)

        }
        snackBar.show()
    }

  /*  private fun showEmptyDatabaseViews(emptyDatabase : Boolean) {
        if (emptyDatabase){
            view?.no_data_imageView?.visibility = View.VISIBLE
            view?.no_data_textView2?.visibility = View.VISIBLE
        }else{
            view?.no_data_imageView?.visibility = View.INVISIBLE
            view?.no_data_textView2?.visibility = View.INVISIBLE
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_fragment,menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_deleteAll-> confirmAllRemoval()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, Observer {
                adapter.setData(it)
            })
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(this, Observer {
                adapter.setData(it)
            })
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmAllRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(),"Successfully Removed Everything!",
                Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton("No"){_,_ -> }
        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure you want to remove everything")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!=null){
            searchThroughDatabase(query)
        }
            return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query!=null){
            searchThroughDatabase(query)
        }
        return true
    }
    private fun searchThroughDatabase(query: String?) {
            var searchQuery = query
            searchQuery = "%$searchQuery%"
        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer {list->
            list?.let {
                adapter.setData(it)
            }
        })

    }

}
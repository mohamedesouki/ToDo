package org.first.todo.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.first.todo.data.models.ToDoData
import org.first.todo.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    var dataList = emptyList<ToDoData>()
    class MyViewHolder(private val binding: RowLayoutBinding) :RecyclerView.ViewHolder(binding.root) {

        fun bind(toDoData: ToDoData){
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup) : MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
/*        holder.itemView.title_txt.text = dataList[position].title
        holder.itemView.description_txt.text = dataList[position].description
        holder.itemView.row_background.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }

        val priority = dataList[position].priority
        when(priority){
            Priority.HIGH ->holder.itemView.priority_indicator.setCardBackgroundColor(ContextCompat.getColor(
                holder.itemView.context,R.color.red
            ))
            Priority.MEDIUM ->holder.itemView.priority_indicator.setCardBackgroundColor(ContextCompat.getColor(
                holder.itemView.context,R.color.yellow
            ))
            Priority.LOW ->holder.itemView.priority_indicator.setCardBackgroundColor(ContextCompat.getColor(
                holder.itemView.context,R.color.green
            ))

        }*/
        val currentItem = dataList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
      return  dataList.size
    }
    fun setData (toDoData: List<ToDoData>){
        val toDoDiffUtil =ToDoDiffUtil(dataList,toDoData)
        val toDoDiffUtilResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoData
        toDoDiffUtilResult.dispatchUpdatesTo(this)
    }
}
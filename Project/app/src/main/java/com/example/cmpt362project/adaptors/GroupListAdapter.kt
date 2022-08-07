package com.example.cmpt362project.adaptors

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.CreateBoardActivity
import com.example.cmpt362project.activities.DisplayBoardActivity
import com.example.cmpt362project.models.Group

class GroupListAdapter(private var groupList: List<Group>) : RecyclerView.Adapter<GroupListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupListAdapter.ViewHolder {
        if(viewType == 0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.group_list_adapter, parent, false)
            return ViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.add_board_button, parent, false)
            val addBoardButton = view.findViewById<Button>(R.id.add_board_button)

            addBoardButton.setOnClickListener {
                val intent = Intent(view.context, CreateBoardActivity::class.java)
//                intent.putExtra("groupTitle", groupTitle)
//                intent.putExtra("groupID", groupID)
                view.context.startActivity(intent)
            }

            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: GroupListAdapter.ViewHolder, position: Int) {
        val groupEntry = holder.itemView.findViewById<Button>(R.id.group_chat_entry)
        groupEntry.text = groupList[position].groupName

        groupEntry.setOnClickListener {
            val intent = Intent(holder.itemView.context, DisplayBoardActivity::class.java)
            intent.putExtra("group", groupList[position])
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    }

    fun updateList(newList:List<Group>){
        groupList = newList
    }

}
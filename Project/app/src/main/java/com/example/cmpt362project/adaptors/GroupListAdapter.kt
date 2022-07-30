package com.example.cmpt362project.adaptors

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.DisplayBoardActivity
import com.example.cmpt362project.activities.DisplayCategoryActivity
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.ui.home.HomeFragment

class GroupListAdapter(val context: Context, private var groupList: List<Group>): BaseAdapter() {

    override fun getCount(): Int {
        return groupList.size
    }

    override fun getItem(p0: Int): Any {
        return groupList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

//////////////////////////////////////////
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = View.inflate(context, R.layout.group_list_adapter, null)
        val groupEntry = view.findViewById<Button>(R.id.group_entry)
        groupEntry.text = groupList[p0].groupName

        groupEntry.setOnClickListener {
            val intent = Intent(view.context, DisplayBoardActivity::class.java)
            intent.putExtra("groupTitle", groupList[p0].groupName)
            view.context.startActivity(intent)
        }
        return view
    }
//////////////////////////////////////
    fun updateList(newList:List<Group>){
        groupList = newList
    }

}
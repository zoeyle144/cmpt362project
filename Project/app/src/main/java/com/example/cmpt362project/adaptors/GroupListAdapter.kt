package com.example.cmpt362project.adaptors

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.CreateBoardActivity
import com.example.cmpt362project.activities.DisplayBoardActivity
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.ui.groups.InviteMemberDialogFragment


class GroupListAdapter(private val context: Context, private var groupList: List<Group>, private var type: Int) : RecyclerView.Adapter<GroupListAdapter.ViewHolder>(), InviteMemberDialogFragment.DialogListener{

    private var onItemClickValue: OnItemClickValue? = null

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
            if(type == 0) {
                val intent = Intent(holder.itemView.context, DisplayBoardActivity::class.java)
                intent.putExtra("group", groupList[position])
                holder.itemView.context.startActivity(intent)
            }
            if(type == 1) {
                //send invitation
                println("Debug: ${groupList[position].groupID} pressed")
                onItemClickValue?.onValueChange(groupList[position].groupID)

//                val intent = Intent(context, addToGroupActivity::class.java)
//                intent.putExtra(addToGroupActivity.KEY_SEARCH_GROUP_ID, groupList[position].groupID)
//                context.startActivity(intent)

//                val preferences = holder.itemView.getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
//                var editor = preferences.edit()
//                editor.putString("groupID",groupList[position].groupID)
//                editor.commit()
                setGroupID(groupList[position].groupID)
            }
        }
    }
    fun getGroupID(): String? {
        val mAppPreferences: SharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context)
        return mAppPreferences.getString("GroupID", "")
    }

    fun setGroupID(username: String?) {
        val mAppPreferences: SharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context)
        val mEditor: SharedPreferences.Editor = mAppPreferences.edit()
        mEditor.putString("GroupID", username)
        mEditor.commit()
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    }

    fun updateList(newList:List<Group>){
        groupList = newList
    }

    override fun sendTexts(userName: String, role: String) {
        TODO("Not yet implemented")
    }

    interface OnItemClickValue {
        fun onValueChange(value: String)
    }
}

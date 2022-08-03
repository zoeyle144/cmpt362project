package com.example.cmpt362project.adaptors

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.ChatConversationActivity
import com.example.cmpt362project.activities.DisplayCategoryActivity
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.models.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class MessageListAdaptor(val context: Context, private var msgList: List<Message>): BaseAdapter() {
    val database = Firebase.database
    val auth = Firebase.auth

    override fun getCount(): Int {
        return msgList.size
    }

    override fun getItem(p0: Int): Any {
        return msgList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = View.inflate(context, R.layout.mgs_list_adaptor, null)
        val mgsUsername = view.findViewById<TextView>(R.id.msg_username)
        val mgsDatetime = view.findViewById<TextView>(R.id.msg_datetime)
        val mgsContent = view.findViewById<TextView>(R.id.msg_content)

        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = simpleDate.format(Date(msgList[p0].timestamp))

        mgsUsername.text = msgList[p0].senderUsername
        mgsDatetime.text = currentDate.toString()
        mgsContent.text = msgList[p0].message

        return view
    }

    fun updateList(newList:List<Message>){
        msgList = newList
    }

    fun clear() {
        msgList = listOf()
    }
}
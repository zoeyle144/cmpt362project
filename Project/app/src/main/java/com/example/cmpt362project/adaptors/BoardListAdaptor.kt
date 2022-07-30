package com.example.cmpt362project.adaptors

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.DisplayCategoryActivity
import com.example.cmpt362project.models.Board

class BoardListAdaptor(val context: Context, private var boardList: List<Board>): BaseAdapter() {

    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(p0: Int): Any {
        return boardList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = View.inflate(context, R.layout.board_list_adaptor, null)
        val boardEntry = view.findViewById<Button>(R.id.board_entry)
        boardEntry.text = boardList[p0].boardName

        boardEntry.setOnClickListener {
            val intent = Intent(view.context, DisplayCategoryActivity::class.java)
            intent.putExtra("boardTitle", boardList[p0].boardName)
            view.context.startActivity(intent)
        }
        return view
    }

    fun updateList(newList:List<Board>){
        boardList = newList
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemTitle: TextView? = itemView.findViewById(R.id.item_title)
    }
}
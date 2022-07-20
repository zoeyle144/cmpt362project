package com.example.cmpt362project.adaptors

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
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
            intent.putExtra("Board", boardList[p0])
            view.context.startActivity(intent)
        }
        return view
    }

    fun updateList(newList:List<Board>){
        boardList = newList
    }

}
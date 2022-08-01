package com.example.cmpt362project.adaptors

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.DisplayCategoryActivity
import com.example.cmpt362project.models.Board

class BoardListAdaptor(private var boardList: List<Board>) : RecyclerView.Adapter<BoardListAdaptor.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListAdaptor.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.board_list_adaptor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardListAdaptor.ViewHolder, position: Int) {
        val boardEntry = holder.itemView.findViewById<Button>(R.id.board_entry)
        boardEntry.text = boardList[position].boardName

        boardEntry.setOnClickListener {
            val intent = Intent(holder.itemView.context, DisplayCategoryActivity::class.java)
            intent.putExtra("board", boardList[position])
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    }

    fun updateList(newList:List<Board>){
        boardList = newList
    }
}
package com.example.cmpt362project.adaptors

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.DisplayCategoryActivity
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.utility.ImageUtility
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope

class BoardListAdaptor(private var boardList: List<Board>) : RecyclerView.Adapter<BoardListAdaptor.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListAdaptor.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.board_list_adaptor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardListAdaptor.ViewHolder, position: Int) {
        var boardPicString = boardList[position].boardPic
        if (boardPicString == ""){
            boardPicString = "defaults/default_bp.png"
        }
        val boardEntry = holder.itemView.findViewById<Button>(R.id.board_entry)
        val pictureView = holder.itemView.findViewById<ImageView>(R.id.board_picture)
        if (boardList[position].boardID == ""){
            boardEntry.isEnabled = false
            pictureView.visibility = View.GONE
            val database = Firebase.database
            val groupRef = database.getReference("groups")
            groupRef.child(boardList[position].groupID).child("groupName").get()
                .addOnSuccessListener {
                    boardEntry.text = it.value.toString()
                }.addOnFailureListener{
                }
        }else{
            boardEntry.isEnabled = true
            pictureView.visibility = View.VISIBLE
        }
        val boardListViewModel = ViewModelProvider(holder.itemView.context as ViewModelStoreOwner)[BoardListViewModel::class.java]
        boardEntry.text = boardList[position].boardName
        boardListViewModel.boardPic.observe(holder.itemView.context as LifecycleOwner) { pictureView.setImageBitmap(it) }
        ImageUtility.setImageViewToProfilePic(boardPicString, pictureView)

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
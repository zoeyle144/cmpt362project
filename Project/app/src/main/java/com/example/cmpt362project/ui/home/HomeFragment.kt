package com.example.cmpt362project.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.MainActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.CreateBoardActivity
import com.example.cmpt362project.activities.CreateBoardActvitiyFromHome
import com.example.cmpt362project.adaptors.*
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<BoardListAdaptor.ViewHolder>? = null
    private lateinit var boardList: List<Board>
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val floatActionButton = view.findViewById<FloatingActionButton>(R.id.add_board_button)
        val boardListViewModel: BoardListViewModel = ViewModelProvider(requireActivity())[BoardListViewModel::class.java]

        val boardListView = view.findViewById<RecyclerView>(R.id.board_list)
        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        boardListView.layoutManager = layoutManager
        boardList = ArrayList()
        adapter = BoardListAdaptor(boardList)
        boardListView.adapter = adapter

        boardListViewModel.fetchBoardsByUser()
        boardListViewModel.boardsLiveData.observe(requireActivity()){ listB ->
            val tempList = listB.groupBy({it.groupID}, {it}).toMap()
            val finalList: MutableList<Board> = ArrayList()
            val tempIter = tempList.iterator()
            var prevGroup = ""
            for (i in tempIter) {
                val tempIter2 = i.value.iterator()
                for (j in tempIter2){
                    val currentGroup = j.groupID
                    if (currentGroup != prevGroup){
                        val groupLabel = Board("","","","","",currentGroup)
                        finalList.add(groupLabel)
                        prevGroup = currentGroup
                    }
                    finalList.add(j)
                }
            }
            println("debug: grouped boardlist: ${finalList}")
            (adapter as BoardListAdaptor).updateList(finalList)
            (adapter as BoardListAdaptor).notifyDataSetChanged()
        }

        floatActionButton.setOnClickListener{
            val intent = Intent(view.context, CreateBoardActvitiyFromHome::class.java)
            view.context.startActivity(intent)
        }

        return view
    }
}
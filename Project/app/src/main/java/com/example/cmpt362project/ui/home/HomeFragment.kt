package com.example.cmpt362project.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
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
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.MainActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.CreateBoardActivity
import com.example.cmpt362project.adaptors.*
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class HomeFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<BoardListAdaptor.ViewHolder>? = null
    private lateinit var boardList: List<Board>

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

        boardListViewModel.fetchBoards()
        boardListViewModel.boardsLiveData.observe(requireActivity()){
            (adapter as BoardListAdaptor).updateList(it)
            (adapter as BoardListAdaptor).notifyDataSetChanged()
        }

        val dividerItemDecoration = DividerItemDecoration(requireActivity(), (layoutManager as LinearLayoutManager).orientation)
        boardListView.addItemDecoration(dividerItemDecoration)
        // Setup ItemTouchHelper
        val callback = BoardDragManageAdaptor(
            adapter as BoardListAdaptor, requireActivity(),
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT))
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(boardListView)

        floatActionButton.setOnClickListener{
            if (MainActivity.role == "admin"){
                val intent = Intent(view.context, CreateBoardActivity::class.java)
                view.context.startActivity(intent)
            }else{
                Toast.makeText(requireActivity(), "You do not have permission to create a board", Toast.LENGTH_SHORT).show()
            }

        }

//        showNotification(requireActivity(),"This is the title", "This is the body", Intent());

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val prefSignature = prefs.getString("signature", "")
        val prefReply = prefs.getString("reply", "")
        val prefSync = prefs.getBoolean("sync", false)
        val prefAttachment = prefs.getBoolean("attachment", false)

        println("prefSignature is $prefSignature, prefReply is $prefReply, " +
                "prefSync is $prefSync, prefAttachment is $prefAttachment")

        return view
    }

}
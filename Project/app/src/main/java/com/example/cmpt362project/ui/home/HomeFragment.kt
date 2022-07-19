package com.example.cmpt362project.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.preference.PreferenceManager
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.CreateBoardActivity
import com.example.cmpt362project.adaptors.BoardListAdaptor
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var boardListAdaptor: BoardListAdaptor
    private lateinit var boardList: List<Board>
    private lateinit var boardListView: ListView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//                ViewModelProvider(this).get(HomeViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val floatActionButton = view.findViewById<FloatingActionButton>(R.id.add_board_button)
        val boardListViewModel: BoardListViewModel = ViewModelProvider(requireActivity())[BoardListViewModel::class.java]

        boardListView = view.findViewById(R.id.board_list)
        boardList =  ArrayList()
        boardListAdaptor = BoardListAdaptor(requireActivity(), boardList)
        boardListView.adapter = boardListAdaptor

        boardListViewModel.fetchBoards()
        boardListViewModel.boardsLiveData.observe(requireActivity()){
            boardListAdaptor.updateList(it)
            boardListAdaptor.notifyDataSetChanged()
        }

        floatActionButton.setOnClickListener{
            val intent = Intent(view.context, CreateBoardActivity::class.java)
            view.context.startActivity(intent)
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val prefSignature = prefs.getString("signature", "")
        val prefReply = prefs.getString("reply", "")
        val prefSync = prefs.getBoolean("sync", false)
        val prefAttachment = prefs.getBoolean("attachment", false)

        println("prefSignature is $prefSignature, prefReply is $prefReply, " +
                "prefSync is $prefSync, prefAttachment is $prefAttachment")


//        return root
        return view
    }
}
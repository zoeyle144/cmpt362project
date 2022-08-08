package com.example.cmpt362project.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.CreateBoardActvitiyFromHome
import com.example.cmpt362project.adaptors.BoardListAdaptor
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.GroupListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<BoardListAdaptor.ViewHolder>? = null
    private var groupIDList: MutableList<String> = ArrayList()
    private lateinit var boardList: List<Board>
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val floatActionButton = view.findViewById<FloatingActionButton>(R.id.add_board_button)
        val boardListViewModel: BoardListViewModel = ViewModelProvider(requireActivity())[BoardListViewModel::class.java]
        val groupListViewModel: GroupListViewModel = ViewModelProvider(requireActivity())[GroupListViewModel::class.java]
        database = Firebase.database
        auth = Firebase.auth
        val groupsRef = database.getReference("groups")

        val boardListView = view.findViewById<RecyclerView>(R.id.board_list)
        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        boardListView.layoutManager = layoutManager
        boardList = ArrayList()
        adapter = BoardListAdaptor(boardList)
        boardListView.adapter = adapter

        groupListViewModel.getGroups(auth.currentUser?.uid.toString())
        groupListViewModel.groupsLiveData.observe(requireActivity()){
            val iter = it.iterator()
            val tempList:MutableList<Board> = ArrayList()
            for (i in iter){
                groupsRef.child(i.groupID).child("boards").get().addOnSuccessListener { data ->
                    val boards: List<Board> = data.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Board::class.java)!!
                    }
                    val iter2 = boards.iterator()
                    val groupLabel = Board("","","","","",i.groupID)
                    tempList.add(groupLabel)
                    for (j in iter2){
                        tempList.add(j)
                    }
                    (adapter as BoardListAdaptor).updateList(tempList)
                    (adapter as BoardListAdaptor).notifyDataSetChanged()
                    println("debug: testing: ${boards}")
                }
            }

        }

//        boardListViewModel.fetchBoardsByUser()
//        boardListViewModel.boardsLiveData.observe(requireActivity()){ listB ->
//            val tempList = listB.groupBy({it.groupID}, {it}).toMap()
//            val finalList: MutableList<Board> = ArrayList()
//            val tempIter = tempList.iterator()
//            var prevGroup = ""
//            for (i in tempIter) {
//                val tempIter2 = i.value.iterator()
//                for (j in tempIter2){
//                    val currentGroup = j.groupID
////                    if (currentGroup in groupIDList){
//                        if (currentGroup != prevGroup){
//                            val groupLabel = Board("","","","","",currentGroup)
//                            finalList.add(groupLabel)
//                            prevGroup = currentGroup
//                        }
//                        finalList.add(j)
////                    }
//                }
//            }
//            println("debug: grouped boardlist: ${finalList}")
//            println("debug: ListB: $listB")
//            (adapter as BoardListAdaptor).updateList(finalList)
//            (adapter as BoardListAdaptor).notifyDataSetChanged()
//        }

        floatActionButton.setOnClickListener{
            val intent = Intent(view.context, CreateBoardActvitiyFromHome::class.java)
            view.context.startActivity(intent)
        }

        return view
    }
}
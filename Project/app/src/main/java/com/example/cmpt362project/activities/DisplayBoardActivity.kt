package com.example.cmpt362project.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.adaptors.BoardListAdaptor
import com.example.cmpt362project.adaptors.CategoryListAdaptor
import com.example.cmpt362project.adaptors.GroupListAdapter
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.example.cmpt362project.viewModels.GroupListViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel

class DisplayBoardActivity: AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<BoardListAdaptor.ViewHolder>? = null
    private lateinit var boardList: List<Board>
    private lateinit var groupListViewModel: GroupListViewModel

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_display_board)
//
//        val boardListViewModel: BoardListViewModel = ViewModelProvider(this)[BoardListViewModel::class.java]
//
//        val boardListView = findViewById<RecyclerView>(R.id.board_list)
//        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        boardListView.layoutManager = layoutManager
//        boardList = ArrayList()
//        adapter = BoardListAdaptor(boardList)
//        boardListView.adapter = adapter
//
//        boardListViewModel.fetchBoards()
//        boardListViewModel.boardsLiveData.observe(this){
//            (adapter as BoardListAdaptor).updateList(it)
//            (adapter as BoardListAdaptor).notifyDataSetChanged()
//        }
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_board)
        val groupTitle  = intent.getSerializableExtra("groupTitle").toString()
        val groupID = intent.getParcelableExtra<Group>("group")?.groupID.toString()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_board)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        boardList = ArrayList()
        adapter = BoardListAdaptor(boardList)
        recyclerView.adapter = adapter

        val boardListViewModel = ViewModelProvider(this)[BoardListViewModel::class.java]
        boardListViewModel.fetchBoards()
        boardListViewModel.boardsLiveData.observe(this){
            (adapter as BoardListAdaptor).updateList(it)
            (adapter as BoardListAdaptor).notifyDataSetChanged()
        }

    }

    override fun onResume() {
        super.onResume()
//        val categoryListViewModel: CategoryListViewModel = ViewModelProvider(this)[CategoryListViewModel::class.java]
//        categoryListViewModel.fetchCategories()

        val groupTitle  = intent.getSerializableExtra("groupTitle").toString()
        val groupID = intent.getParcelableExtra<Group>("group")?.groupID.toString()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_board)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        boardList = ArrayList()
        adapter = BoardListAdaptor(boardList)
        recyclerView.adapter = adapter

        val boardListViewModel = ViewModelProvider(this)[BoardListViewModel::class.java]
        boardListViewModel.fetchBoards()
        boardListViewModel.boardsLiveData.observe(this){
            (adapter as BoardListAdaptor).updateList(it)
            (adapter as BoardListAdaptor).notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.custom_board_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.board_info_button){
            val selectedGroup = intent.getParcelableExtra<Group>("group")

            val groupID = selectedGroup?.groupID.toString()

            println("Debug: $groupID")
            val groupListViewModel: GroupListViewModel = ViewModelProvider(this)[GroupListViewModel::class.java]
            groupListViewModel.delete(groupID)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

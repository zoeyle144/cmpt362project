package com.example.cmpt362project.activities

import android.os.Bundle
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
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.example.cmpt362project.viewModels.GroupListViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel

class DisplayBoardActivity: AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapter: BoardListAdaptor
    private lateinit var boardList: List<Board>
    private lateinit var boardListView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
    }

}

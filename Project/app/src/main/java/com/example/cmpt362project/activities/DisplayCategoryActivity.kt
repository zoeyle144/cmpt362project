package com.example.cmpt362project.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipDescription
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ListView
import android.widget.ScrollView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.adaptors.CategoryListAdaptor
//import com.example.cmpt362project.adaptors.DragManageAdapter
import com.example.cmpt362project.adaptors.TaskListAdaptor
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel

class DisplayCategoryActivity: AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CategoryListAdaptor.ViewHolder>? = null
    private lateinit var categoryList: List<Category>
    private lateinit var boardListViewModel: BoardListViewModel

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_category)

        val boardParcel = intent.getParcelableExtra<Board>("board")
        val boardTitle  = boardParcel?.boardName.toString()
        val boardID = boardParcel?.boardID.toString()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        categoryList = ArrayList()
        adapter = CategoryListAdaptor(categoryList, boardTitle, boardID)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.HORIZONTAL
            )
        )

        val categoryListViewModel = ViewModelProvider(this)[CategoryListViewModel::class.java]
        categoryListViewModel.fetchCategories(boardID)
        categoryListViewModel.categoriesLiveData.observe(this){
            (adapter as CategoryListAdaptor).updateList(it)
            (adapter as CategoryListAdaptor).notifyDataSetChanged()
        }

    }

    override fun onResume() {
        super.onResume()

        val boardTitle  = intent.getParcelableExtra<Board>("board")?.boardName.toString()
        val boardID = intent.getParcelableExtra<Board>("board")?.boardID.toString()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        categoryList = ArrayList()
        adapter = CategoryListAdaptor(categoryList, boardTitle , boardID)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.HORIZONTAL
            )
        )

        val categoryListViewModel = ViewModelProvider(this)[CategoryListViewModel::class.java]
        categoryListViewModel.fetchCategories(boardID)
        categoryListViewModel.categoriesLiveData.observe(this){
            (adapter as CategoryListAdaptor).updateList(it)
            (adapter as CategoryListAdaptor).notifyDataSetChanged()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val boardTitle  = intent.getParcelableExtra<Board>("board")?.boardName.toString()
        menuInflater.inflate(R.menu.custom_board_menu,menu)
        getSupportActionBar()?.setTitle("$boardTitle");
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.board_info_button){
            openDisplayBoardInfoActivityForResult()
        }
        return super.onOptionsItemSelected(item)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == RESULT_OK && result.data != null){
//            val temp = result.data!!.getSerializableExtra("boardNameField").toString()
//            getSupportActionBar()?.setTitle("$temp");
//        } else
        if (result.resultCode == RESULT_OK){
            this.finish()
        }
    }

    private fun openDisplayBoardInfoActivityForResult(){
        val infoIntent = Intent(this, DisplayBoardInfoActivity::class.java)
        val boardParcel = intent.getParcelableExtra<Board>("board")
        infoIntent.putExtra("board", boardParcel)
        resultLauncher.launch(infoIntent)
    }
}
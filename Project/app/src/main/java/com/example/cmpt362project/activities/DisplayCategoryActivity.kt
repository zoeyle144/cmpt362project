package com.example.cmpt362project.activities

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ListView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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

        val boardTitle  = intent.getParcelableExtra<Board>("board")?.boardName.toString()
        val boardID = intent.getParcelableExtra<Board>("board")?.boardID.toString()

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
        menuInflater.inflate(R.menu.custom_category_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.delete_category_button){
            val confirmationBuilder = AlertDialog.Builder(this)
            val selectedBoard = intent.getParcelableExtra<Board>("board")
            confirmationBuilder.setMessage("Are you sure you want to Delete Board <${selectedBoard?.boardName}>?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    val boardID = selectedBoard?.boardID.toString()
                    boardListViewModel = ViewModelProvider(this)[BoardListViewModel::class.java]
                    boardListViewModel.delete(boardID)
                    finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = confirmationBuilder.create()
            alert.show()
        }
        return super.onOptionsItemSelected(item)
    }
}
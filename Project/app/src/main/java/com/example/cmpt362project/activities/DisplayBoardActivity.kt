package com.example.cmpt362project.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.adaptors.BoardListAdaptor
import com.example.cmpt362project.adaptors.SpinnerAdapter
import com.example.cmpt362project.database.User
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.ui.groups.FirebaseSuccessListener
import com.example.cmpt362project.viewModels.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.security.AccessController.getContext

class DisplayBoardActivity: AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<BoardListAdaptor.ViewHolder>? = null
    private lateinit var boardList: List<Board>
    private lateinit var groupListViewModel: GroupListViewModel
    private var listener: FirebaseSuccessListener? = null

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

        val toolbar = findViewById<Toolbar>(R.id.profile_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Your Groups"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_baseline_close_24, theme))
        supportActionBar?.setHomeActionContentDescription(getString(R.string.profile_toolbar_discard))

        val lv = findViewById<ListView>(R.id.member_listview)


        //mData = list of members
        val mData: ArrayList<String> = ArrayList()
        mData.add("admin")
        mData.add("mod")
        mData.add("writer")
        mData.add("reader")

        val mSpinnerData: ArrayList<String> = ArrayList()
        mSpinnerData.add("admin")
        mSpinnerData.add("mod")
        mSpinnerData.add("writer")
        mSpinnerData.add("reader")


        val spinnerAdapter = SpinnerAdapter(mData, mSpinnerData, this)
        lv.setAdapter(spinnerAdapter)

        var database = Firebase.database
        val permRef = database.getReference("permission")
        val permissionID = permRef.push().key!!
        println("Debug: permissionID $permissionID")


        val permissionViewModel: PermissionViewModel = ViewModelProvider(this)[PermissionViewModel::class.java]
        //println("Debug: ROLE $role")


//
//        val userQuery = database.
//        userQuery.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                listOfUsernames.clear()
//                listOfUsers.clear()
//                for (i in snapshot.children) {
//                    val data = i.value as Map<*, *>
//                    val username = data["username"] as String
//
//                    val user = User(username,
//                        data["email"] as String,
//                        data["name"] as String,
//                        data["profilePic"] as String,
//                        data["aboutMe"] as String
//                    )
//
//                    listOfUsernames.add(username)
//                    listOfUsers.add(user)
//                }
//                if (initRecyclerViewAdapter) recyclerViewAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val itemId = item.itemId
//        if (itemId == R.id.board_info_button){
//            val selectedGroup = intent.getParcelableExtra<Group>("group")



////        permissionViewModel.canDelete("-N8nxMBOuplwJJx6iNFn","zoey")
////        permissionViewModel.canRead("-N8nxMBOuplwJJx6iNFn","zoey")
////        permissionViewModel.canEdit("-N8nxMBOuplwJJx6iNFn","zoey")
//        permissionViewModel.updateRole("-N8nxMBOuplwJJx6iNFn","zoey", "admin")
//
//        role = permissionViewModel.userHasRole("-N8nxMBOuplwJJx6iNFn","zoey", listener!!)
//        println("Debug: ROLE $role")
    }
//
//    override fun onResume() {
//        super.onResume()
////        val categoryListViewModel: CategoryListViewModel = ViewModelProvider(this)[CategoryListViewModel::class.java]
////        categoryListViewModel.fetchCategories()
//
//        val groupTitle  = intent.getSerializableExtra("groupTitle").toString()
//        val groupID = intent.getParcelableExtra<Group>("group")?.groupID.toString()
//
//        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_board)
//
//        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recyclerView.layoutManager = layoutManager
//        boardList = ArrayList()
//        adapter = BoardListAdaptor(boardList)
//        recyclerView.adapter = adapter
//
//        val boardListViewModel = ViewModelProvider(this)[BoardListViewModel::class.java]
//        boardListViewModel.fetchBoards()
//        boardListViewModel.boardsLiveData.observe(this){
//            (adapter as BoardListAdaptor).updateList(it)
//            (adapter as BoardListAdaptor).notifyDataSetChanged()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.custom_board_menu,menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val itemId = item.itemId
//        if (itemId == R.id.delete_board_btn){
//            val selectedGroup = intent.getParcelableExtra<Group>("group")
//
//            val groupID = selectedGroup?.groupID.toString()
//
//            println("Debug: $groupID")
//            val groupListViewModel: GroupListViewModel = ViewModelProvider(this)[GroupListViewModel::class.java]
//            groupListViewModel.delete(groupID)
//            finish()
//        }
//        return super.onOptionsItemSelected(item)
//    }
}

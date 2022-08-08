package com.example.cmpt362project.activities

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Permission
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateBoardActvitiyFromHome: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var groupIDs: ArrayList<String>
    private lateinit var groupNames: ArrayList<String>
    private lateinit var roles: ArrayList<String>
    private lateinit var permissionEntries: List<Permission>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board_from_home)

        val createBoardButton = findViewById<Button>(R.id.create_board_button)
        val cancelCreateBoardButton = findViewById<Button>(R.id.cancel_create_board_button)
        val createBoardName = findViewById<EditText>(R.id.create_board_name_input)
        val createBoardDescription = findViewById<EditText>(R.id.create_board_description_input)
        val chosenGroup = findViewById<Spinner>(R.id.chosen_group)

        database = Firebase.database
        auth = Firebase.auth
        val permissionRef = database.getReference("permission")
        permissionRef
            .orderByChild("uid")
            .equalTo(auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                if (it.exists()){
                    permissionEntries = it.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Permission::class.java)!!
                    }
                    println("debug: GroupUsers: $permissionEntries")
                    val iterator = permissionEntries.listIterator()
                    groupIDs = ArrayList()
                    groupNames = ArrayList()
                    roles = ArrayList()
                    for (i in iterator) {
                        groupIDs.add(i.groupID)
                        roles.add(i.role)
                        val groupRef = database.getReference("groups")
                        groupRef.child(i.groupID).child("groupName").get().addOnSuccessListener {entry ->
                            groupNames.add(entry.value.toString())
                            val tempAdaptor = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,groupNames);
                            chosenGroup.adapter = tempAdaptor
                        }.addOnFailureListener{ err ->
                        }
                    }
                    println("debug: GroupNames: $groupNames")
                }
            }.addOnFailureListener { err ->
                println("debug: get groupUsers fail Error ${err.message}")
            }

        createBoardButton.setOnClickListener{
            val boardListViewModel: BoardListViewModel = ViewModelProvider(this)[BoardListViewModel::class.java]
            val database = Firebase.database
            val boardsRef = database.getReference("boards")
            val selectedGroupID = groupIDs[groupNames.indexOf(chosenGroup.selectedItem.toString())]
            val roleOfUser = roles[groupIDs.indexOf(selectedGroupID)]

            if (roleOfUser == "admin"){
                val boardID = boardsRef.push().key!!
                val boardName = createBoardName.text
                val boardDescription = createBoardDescription.text
                auth = Firebase.auth
                val createdBy = auth.currentUser?.uid
                val board = Board(boardID,boardName.toString(), boardDescription.toString(), createdBy.toString(), "",selectedGroupID)
                boardListViewModel.insert(board)
                finish()
            }else{
                Toast.makeText(this,
                    "You do not have permission to create board in group: ${chosenGroup.selectedItem}",
                    Toast.LENGTH_SHORT).show()
            }
        }

        cancelCreateBoardButton.setOnClickListener{
            finish()
        }
    }
}
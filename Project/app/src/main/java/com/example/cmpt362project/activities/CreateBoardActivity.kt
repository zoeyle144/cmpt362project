package com.example.cmpt362project.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateBoardActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)

        val createBoardButton = findViewById<Button>(R.id.create_board_button)
        val createBoardName = findViewById<EditText>(R.id.create_board_name_input)
        val createBoardDescription = findViewById<EditText>(R.id.create_board_description_input)
        createBoardButton.setOnClickListener{
            val boardListViewModel: BoardListViewModel = ViewModelProvider(this)[BoardListViewModel::class.java]
            val database = Firebase.database
            val boardsRef = database.getReference("boards")
            val boardID = boardsRef.push().key!!
            val boardName = createBoardName.text
            val boardDescription = createBoardDescription.text
            val groupName = intent.getSerializableExtra("groupTitle").toString()

            auth = Firebase.auth
            val createdBy = auth.currentUser?.uid
            val board = Board(boardID,boardName.toString(), boardDescription.toString(), createdBy.toString())
            boardListViewModel.insert(board)
            
            finish()

        }
    }
}
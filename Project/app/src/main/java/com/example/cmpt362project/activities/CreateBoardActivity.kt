package com.example.cmpt362project.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Board
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

            val database = Firebase.database
            val boardsRef = database.getReference("boards")
            val boardID = boardsRef.push().key!!
            val boardName = createBoardName.text
            val boardDescription = createBoardDescription.text
            auth = Firebase.auth
            val createdBy = auth.currentUser?.uid
            val board = Board(boardName.toString(), boardDescription.toString(), createdBy.toString(),ArrayList())


            boardsRef.child(boardID).setValue(board)
                .addOnCompleteListener{
                    Toast.makeText(this, "Board Created Successfully", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{ err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

            finish()

        }
    }
}
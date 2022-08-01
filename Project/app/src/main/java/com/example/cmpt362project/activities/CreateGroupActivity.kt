package com.example.cmpt362project.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateGroupActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        val createGroupButton = findViewById<Button>(R.id.create_group_button)
        val createGroupName = findViewById<EditText>(R.id.create_group_name_input)
        val createGroupDescription = findViewById<EditText>(R.id.create_group_description_input)
        createGroupButton.setOnClickListener{

            val database = Firebase.database
            val groupsRef = database.getReference("groups")
            val groupID = groupsRef.push().key!!
            val groupName = createGroupName.text
            val groupDescription = createGroupDescription.text
            auth = Firebase.auth
            val createdBy = auth.currentUser?.uid
            val group = Group(groupName.toString(), groupDescription.toString(), createdBy.toString(),ArrayList(),ArrayList())


            groupsRef.child(groupID).setValue(group)
                .addOnCompleteListener{
                    Toast.makeText(this, "Group Created Successfully", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{ err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

            finish()

        }
    }
}
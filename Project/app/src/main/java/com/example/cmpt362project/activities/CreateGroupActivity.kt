package com.example.cmpt362project.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.database.User
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.models.GroupChat
import com.example.cmpt362project.models.Permission
import com.example.cmpt362project.ui.groups.InviteMemberDialogFragment
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.GroupChatListViewModel
import com.example.cmpt362project.viewModels.GroupListViewModel
import com.example.cmpt362project.viewModels.PermissionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateGroupActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var memberListView: TextView

    private lateinit var memberList: ArrayList<String>
    private lateinit var userName: String
    private lateinit var role: String
    private val database = Firebase.database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        auth = Firebase.auth
        val createGroupButton = findViewById<Button>(R.id.create_group_button)
        val createGroupName = findViewById<EditText>(R.id.create_group_name_input)
        val createGroupDescription = findViewById<EditText>(R.id.create_group_description_input)

        val uID = auth.currentUser?.uid
        val userRole = "admin"
        var userName = ""
        database.getReference("users").child(uID!!).get().addOnSuccessListener {
            if (it.value != null) {
                val userData = it.value as Map<*, *>
                userName = userData["username"].toString()
            }
        }

        createGroupButton.setOnClickListener{

            val groupListViewModel: GroupListViewModel = ViewModelProvider(this)[GroupListViewModel::class.java]
            val groupsRef = database.getReference("groups")
            val groupID = groupsRef.push().key!!
            val groupName = createGroupName.text.toString()
            val groupDescription = createGroupDescription.text
            auth = Firebase.auth

            groupsRef.get().addOnSuccessListener {
                if (it.value != null) {
                    var exists = false
                    val groupsList = it.value as Map<*, *>
                    for ((key, value) in groupsList) {

                        var entry = value as Map<*, *>
                        println(entry)
                        if (entry["groupName"] == groupName) {
                            exists = true
                            break
                        }
                    }
                    if (exists) {
                        Toast.makeText(this, "Group name ${groupName} already taken.", Toast.LENGTH_SHORT).show()
                    } else {
                        val permRef = database.getReference("permission")
                        val permissionID = permRef.push().key!!
                        val permissionViewModel: PermissionViewModel = ViewModelProvider(this)[PermissionViewModel::class.java]

                        // push creator as admin
                        val permission = Permission(permissionID,userRole,uID,groupID, userName)
                        permissionViewModel.insert(permission)

                        // push group chat
                        val groupChatsRef = database.getReference("group_chats")
                        val groupChatViewModel = ViewModelProvider(this)[GroupChatListViewModel::class.java]
                        val groupChatID = groupChatsRef.push().key!!
                        val groupChat = GroupChat(groupChatID, groupID, System.currentTimeMillis())
                        groupChatViewModel.insert(groupChat)
                        Toast.makeText(this, "Group ${groupName} created.", Toast.LENGTH_SHORT).show()

                        // push group
                        val group = Group(groupID, groupName.toString(), groupDescription.toString())
                        groupListViewModel.insert(group)
                        finish()
                    }
                }

            }



        }

    }


}
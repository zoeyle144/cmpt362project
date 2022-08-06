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
import com.example.cmpt362project.models.Permission
import com.example.cmpt362project.ui.groups.InviteMemberDialogFragment
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.GroupListViewModel
import com.example.cmpt362project.viewModels.PermissionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateGroupActivity: AppCompatActivity(), InviteMemberDialogFragment.DialogListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var memberListView: TextView

    private lateinit var memberList: ArrayList<String>
    private lateinit var userName: String
    private lateinit var role: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        val createGroupButton = findViewById<Button>(R.id.create_group_button)
        val createGroupName = findViewById<EditText>(R.id.create_group_name_input)
        val createGroupDescription = findViewById<EditText>(R.id.create_group_description_input)
        val inviteMemberButton = findViewById<Button>(R.id.invite_member_btn)
        val groupName = intent.getSerializableExtra("groupTitle").toString()


        memberListView = findViewById<TextView>(R.id.memberList)

        createGroupButton.setOnClickListener{

            val groupListViewModel: GroupListViewModel = ViewModelProvider(this)[GroupListViewModel::class.java]
            val database = Firebase.database
            val groupsRef = database.getReference("groups")
            val groupID = groupsRef.push().key!!
            val groupName = createGroupName.text
            val groupDescription = createGroupDescription.text
            auth = Firebase.auth
            val createdBy = auth.currentUser?.uid

            val permRef = database.getReference("permission")
            val permissionID = permRef.push().key!!
            val permissionViewModel: PermissionViewModel = ViewModelProvider(this)[PermissionViewModel::class.java]


            println("Debug: Assign role $memberList")

            val group = Group(groupID, groupName.toString(), groupDescription.toString(), createdBy.toString(),memberList, ArrayList())

            val permission = Permission(permissionID,role,userName,groupID, userName)
            groupListViewModel.insert(group)
//            permissionViewModel.insert(permission, permissionID)
            
            finish()

        }

        inviteMemberButton.setOnClickListener {
//            val myFragment= SearchUserFragment()
//            getSupportFragmentManager().beginTransaction().replace(R.id.container1,myFragment).addToBackStack(null).commit()
//
            val myDialog = InviteMemberDialogFragment()
            val bundle = Bundle()
            bundle.putInt(InviteMemberDialogFragment.DIALOG_KEY, InviteMemberDialogFragment.TEST_DIALOG)
            myDialog.arguments = bundle
            myDialog.show(supportFragmentManager, "my dialog")

        }
    }

    override fun sendTexts(assignName: String, assignRole: String) {
        memberListView.text = "$assignName - $assignRole"
        userName = assignName
        role = assignRole
        memberList = ArrayList()
        memberList.add(userName)



    }

    fun getMember(userName: String) {

    }

}
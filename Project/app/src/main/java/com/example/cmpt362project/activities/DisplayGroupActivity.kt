package com.example.cmpt362project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.adaptors.MessageListAdaptor
import com.example.cmpt362project.adaptors.PermissionAdaptor
import com.example.cmpt362project.viewModels.GroupListViewModel
import com.example.cmpt362project.viewModels.MessageListViewModel
import com.example.cmpt362project.viewModels.PermissionViewModel
import com.google.android.material.textfield.TextInputLayout
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.ui.groups.InviteMemberDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DisplayGroupActivity : AppCompatActivity(), InviteMemberDialogFragment.DialogListener {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.custom_group_info_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_group)


        val groupID = intent!!.getStringExtra("groupID")
        val groupName = intent!!.getStringExtra("groupName")
        val groupDescription = intent!!.getStringExtra("groupDescription")

        val groupNameView = findViewById<TextInputLayout>(R.id.group_name_field)
        val groupDescView = findViewById<TextInputLayout>(R.id.group_desc_field)
        val groupHeaderView = findViewById<TextView>(R.id.group_view_header)

        groupHeaderView.text = "View Group"
        groupNameView.editText!!.setText(groupName)
        groupDescView.editText!!.setText(groupDescription)

        val listView = findViewById<ListView>(R.id.group_list)
        val permVM: PermissionViewModel = ViewModelProvider(this)[PermissionViewModel::class.java]
        var adapter = PermissionAdaptor(this, listOf(), permVM, false)




        val saveNameDescriptionBtn = findViewById<Button>(R.id.save_group_changes)
        val inviteMemberButton = findViewById<Button>(R.id.invite_member_btn)
        val groupViewModel: GroupListViewModel = ViewModelProvider(this)[GroupListViewModel::class.java]

        val auth = Firebase.auth
        val permRef = Firebase.database.getReference("permission")
        permRef.get().addOnSuccessListener {
            if (it.value != null) {
                val permList = it.value as Map<*, *>
                var isAdmin = false;
                for ((key, value) in permList) {
                    var permEntry = value as Map<*, *>
                    if (permEntry["uid"] == auth.currentUser!!.uid &&
                        permEntry["groupID"] == groupID &&
                        permEntry["role"] == "admin") {

                        isAdmin = true
                        break
                    }
                }
                if (isAdmin) {
                    saveNameDescriptionBtn.visibility = View.VISIBLE
                    inviteMemberButton.visibility = View.VISIBLE

                    groupNameView.editText!!.setFocusableInTouchMode(true)
                    groupNameView.editText!!.focusable = View.FOCUSABLE
                    groupDescView.editText!!.setFocusableInTouchMode(true)
                    groupDescView.editText!!.focusable = View.FOCUSABLE
                    groupHeaderView.text = "Edit Group"


                    listView.adapter = PermissionAdaptor(this, listOf(), permVM, true)
                    listView.adapter = adapter

                    permVM.getPermissions(groupID!!)
                    permVM.permissionsLiveData.observe(this) { it ->
                        adapter.clear()
                        //Log.w("DEBUGR", it.toString())
                        adapter.updateList(it)
                        adapter.notifyDataSetChanged()
                    }
                }


                else {
                    listView.adapter = adapter
                    if (groupID != null) {
                        permVM.getPermissions(groupID)
                    }
                    permVM.permissionsLiveData.observe(this) { it ->
                        adapter.clear()
                        //Log.w("DEBUGR", it.toString())
                        adapter.updateList(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        saveNameDescriptionBtn.setOnClickListener {
            var newGroupName = groupNameView.editText!!.text.toString()
            var newGroupDesc = groupDescView.editText!!.text.toString()
            val groupsRef = Firebase.database.getReference("groups")
            groupsRef.get().addOnSuccessListener {
                if (it.value != null) {
                    var exists = false
                    val groupsList = it.value as Map<*, *>
                    for ((key, value) in groupsList) {

                        var entry = value as Map<*, *>
                        println(entry)
                        if (entry["groupName"] == newGroupName) {
                            exists = true
                            break
                        }
                    }
                    if (exists) {
                        Toast.makeText(
                            this,
                            "Group name ${newGroupName} already taken.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        groupViewModel.insert(Group(groupID!!, newGroupName, newGroupDesc))
                        Toast.makeText(
                            this,
                            "Saved group name and description.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        inviteMemberButton.setOnClickListener{
            val myDialog = InviteMemberDialogFragment()
            val bundle = Bundle()
            bundle.putInt(InviteMemberDialogFragment.DIALOG_KEY, InviteMemberDialogFragment.TEST_DIALOG)
            myDialog.arguments = bundle
            myDialog.show(supportFragmentManager, "my dialog")
        }

    }

    override fun sendTexts(assignName: String, assignRole: String) {
        /*
        memberListView.text = "$assignName - $assignRole"
        userName = assignName
        role = assignRole
        memberList = ArrayList()
        memberList.add(userName)
        */
    }

}
package com.example.cmpt362project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
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

class DisplayGroupActivity : AppCompatActivity() {

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
        groupHeaderView.text = "Edit Group"
        groupNameView.editText!!.setText(groupName)
        groupDescView.editText!!.setText(groupDescription)

        val listView = findViewById<ListView>(R.id.group_list)
        val permVM: PermissionViewModel = ViewModelProvider(this)[PermissionViewModel::class.java]
        val adapter = PermissionAdaptor(this, listOf(), permVM)
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

        val saveNameDescriptionBtn = findViewById<Button>(R.id.save_group_changes)
        val groupViewModel: GroupListViewModel = ViewModelProvider(this)[GroupListViewModel::class.java]

        saveNameDescriptionBtn.setOnClickListener {
            var newGroupName = groupNameView.editText!!.text.toString()
            var newGroupDesc = groupDescView.editText!!.text.toString()
            groupViewModel.insert(Group(groupID!!, newGroupName, newGroupDesc))

            Toast.makeText(this, "Saved group name and description.", Toast.LENGTH_SHORT).show()
        }

    }
}
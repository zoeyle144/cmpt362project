package com.example.cmpt362project.ui.invitation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.ChatConversationActivity
import com.example.cmpt362project.activities.CreateGroupActivity
import com.example.cmpt362project.adaptors.GroupListAdapter
import com.example.cmpt362project.adaptors.InvitationListAdaptor
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.models.Invitation
import com.example.cmpt362project.ui.search.SearchUserResultActivity
import com.example.cmpt362project.viewModels.ChatListViewModel
import com.example.cmpt362project.viewModels.GroupListViewModel
import com.example.cmpt362project.viewModels.InvitationViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class addToGroupActivity: AppCompatActivity(), AdapterView.OnItemClickListener, GroupListAdapter.OnItemClickValue {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapter: GroupListAdapter
    private lateinit var groupList: List<Group>
    private var groupID = ""
    var username = ""
    
    companion object {
        const val KEY_USER_NAME = "KEY_SEARCH_USER_RESULT_USERNAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_group)

        val toolbar = findViewById<Toolbar>(R.id.profile_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_baseline_close_24, theme))
        supportActionBar?.setHomeActionContentDescription(getString(R.string.profile_toolbar_close))

        val usernameView = findViewById<TextInputLayout>(R.id.profile_username_field)
        val extras = intent.extras
        if (extras != null) {
            username = extras.getString(KEY_USER_NAME, "")
            //groupID = extras.getString(KEY_SEARCH_GROUP_ID, "")

            supportActionBar?.title = "$username's Profile"

        }


        val groupListViewModel: GroupListViewModel = ViewModelProvider(this)[GroupListViewModel::class.java]
        val groupListView = findViewById<RecyclerView>(R.id.group_list)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        groupListView.layoutManager = layoutManager
        groupList =  ArrayList()
        adapter = GroupListAdapter(this,groupList, 1)
        groupListView.adapter = adapter

        groupID = adapter.getGroupID().toString()


        //groupListViewModel.getGroups()
        groupListViewModel.groupsLiveData.observe(this){
            (adapter as GroupListAdapter).updateList(it)
            (adapter as GroupListAdapter).notifyDataSetChanged()
        }


        val invitationListViewModel: InvitationViewModel = ViewModelProvider(this)[InvitationViewModel::class.java]
        val invitationAdapter = InvitationListAdaptor(this, listOf(), invitationListViewModel)

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println("Debug: HELLO")
    }

    override fun onValueChange(value: String) {
        groupID = value
        println("Debug: groupID $groupID")

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_to_group_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.profile_search_toolbar_invite -> {
            val invitationViewModel: InvitationViewModel = ViewModelProvider(this)[InvitationViewModel::class.java]
            val database = Firebase.database
            val auth = Firebase.auth
            val invitationRef = database.getReference("invitations")
            val usernamesRef = database.getReference("usernames")
            val usersRef = database.getReference("users")
            val invitationID = invitationRef.push().key!!
            val user1uid = auth.uid
            var user2uid = ""
            var myUsername = ""

            println("Debug: here $groupID")
            println("Debug: username $username")

            Log.w("DEBUGU", username)
            usernamesRef.child(username).get().addOnSuccessListener {
                Log.w("DEBUGZ", it.value.toString())
                user2uid = it.value.toString()
                usersRef.child(auth.currentUser!!.uid).get().addOnSuccessListener {
                    var usersListEntry = it.value as Map<*, *>
                    myUsername = usersListEntry["username"].toString()
                    val invitation =
                        Invitation(invitationID, user1uid as String, username, user2uid, groupID)


                    invitationRef.get().addOnSuccessListener {
                        var exists = false
                        if (it.value != null) {
                            val invitationList = it.value as Map<*, *>
                            for ((key, value) in invitationList) {
                                var invitationListEntry = value as Map<*, *>
                                if (invitationListEntry["sender"] == invitation.sender && invitationListEntry["receiver"] == invitation.receiver
                                    && invitationListEntry["groupId"] == invitation.groupId
                                ) {
                                    exists = true
                                    break
                                }
                            }
                        }
                        if (!exists) {
                            invitationRef.child(invitation.invitationId).setValue(invitation)
                                .addOnCompleteListener {
                                    println("Debug: added invitation successfully")
                                }
                                .addOnFailureListener { err ->
                                    println("Debug: added invitation failed")

                                }
                        }
                    }
                }
            }
            val intent = Intent(this,CreateGroupActivity::class.java)
            intent.putExtra("invitation",invitationID)
            startActivity(intent)

            finish()
            true
        }
        else -> {
            println("Click close")
            finish()
            true
        }
    }
}
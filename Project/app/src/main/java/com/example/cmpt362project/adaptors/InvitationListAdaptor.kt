package com.example.cmpt362project.adaptors

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Invitation
import com.example.cmpt362project.models.Permission
import com.example.cmpt362project.viewModels.InvitationViewModel
import com.example.cmpt362project.viewModels.PermissionViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class InvitationListAdaptor(val context: Context, private var invitationList: List<Invitation>, private var viewModel: InvitationViewModel): BaseAdapter() {
    val database = Firebase.database
    val auth = Firebase.auth

    override fun getCount(): Int {
        return invitationList.size
    }

    override fun getItem(p0: Int): Any {
        return invitationList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val view = View.inflate(context, R.layout.invitation_list_adaptor, null)
        val invitationSenderUsername = view.findViewById<TextView>(R.id.inv_sender_username)
        val invitationGroupName = view.findViewById<TextView>(R.id.inv_group_name)
        val invitationAcceptBtn = view.findViewById<Button>(R.id.inv_accept_btn)
        val invitationDeclineBtn = view.findViewById<Button>(R.id.inv_decline_btn)
        invitationSenderUsername.text = invitationList[p0].sender_username


        var groupName = ""
        database.getReference("groups").child(invitationList[p0].groupId).get().addOnSuccessListener {
            if (it.value != null) {
                val groupData = it.value as Map<*, *>
                groupName = groupData["groupName"].toString()
                invitationGroupName.text = groupName
            }

            println(it.value)
        }

        val uID = auth.currentUser?.uid
        var userName = ""
        database.getReference("users").child(uID!!).get().addOnSuccessListener {
            if (it.value != null) {
                val userData = it.value as Map<*, *>
                userName = userData["username"].toString()
            }
        }

        invitationDeclineBtn.setOnClickListener{
            viewModel.delete(invitationList[p0].invitationId)
            Toast.makeText(context, "Declined invitation for group: ${groupName}." ,Toast.LENGTH_SHORT).show()
        }

        invitationAcceptBtn.setOnClickListener{
            //add permission
            val permissionViewModel: PermissionViewModel = ViewModelProvider((context as FragmentActivity)!!)[PermissionViewModel::class.java]
            val permRef = database.getReference("permission")
            val permissionID = permRef.push().key!!
            val role = "reader"
            val groupID_uID = "${invitationList[p0].groupId} _ $uID"

            val permission = Permission(permissionID,role,uID!!,invitationList[p0].groupId, userName, groupID_uID)

            println("Debug: permission!!! $permission")
            permissionViewModel.insert(permission)



            Toast.makeText(context, "Accepted invitation for group: ${groupName}" ,Toast.LENGTH_SHORT).show()
        }
        return view
    }

    fun updateList(newList:List<Invitation>){
        invitationList = newList
    }

    fun clear() {
        invitationList = listOf()
    }
}
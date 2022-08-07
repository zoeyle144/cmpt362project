package com.example.cmpt362project.ui.groups

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.database.User
import com.example.cmpt362project.ui.search.SearchUserAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class InviteMemberDialogFragment: DialogFragment(), DialogInterface.OnClickListener {

    private var columnCount = 1

    private val listOfUsernames = ArrayList<String>()
    private val listOfUsers = ArrayList<User>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: SearchUserAdapter
    private lateinit var database: DatabaseReference
    private lateinit var userName: String
    private lateinit var role: String
 //   private lateinit var role_spinner: Spinner

    private lateinit var member: TextView
    private lateinit var dialogListener: DialogListener

    companion object{
        const val DIALOG_KEY = "dialog"
        const val TEST_DIALOG = 1
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var ret: Dialog
        val bundle = arguments
        val dialogId = bundle?.getInt(DIALOG_KEY)
        val view: View = requireActivity().layoutInflater.inflate(R.layout.fragment_invite_member_dialog, null)

 //       role_spinner = view.findViewById(R.id.role_spinner)

        member = view.findViewById(R.id.member_user_name)
        userName = ""
        role = ""

        if (dialogId == TEST_DIALOG) {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setView(view)
            builder.setTitle("Invite member")
            builder.setPositiveButton("ok", this)
            builder.setNegativeButton("cancel", this)
            ret = builder.create()
        }



        database = Firebase.database.reference
        val allUsersRef = database.child("users")
        allUsersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    val data = i.value as Map<*, *>
                    val username = data["username"] as String

                    val user = User(username,
                        data["email"] as String,
                        data["name"] as String,
                        data["profilePic"] as String,
                        data["aboutMe"] as String
                    )

                    listOfUsernames.add(username)
                    listOfUsers.add(user)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        recyclerView = view.findViewById(R.id.search_member)
        recyclerView.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }

        recyclerViewAdapter = SearchUserAdapter(requireActivity(), listOfUsers, 1)
        recyclerView.adapter = recyclerViewAdapter

        val searchView = view.findViewById<SearchView>(R.id.search_user_search_bar)
        val mySearchListener = SearchListener()
        searchView.setOnQueryTextListener(mySearchListener)

        return ret
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    inner class SearchListener : SearchView.OnQueryTextListener {
        // https://www.geeksforgeeks.org/android-searchview-with-example/
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (listOfUsernames.contains(query)) {
                userName = query.toString()
                recyclerViewAdapter.filter.filter(query)
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            recyclerViewAdapter.filter.filter(newText)
            return false
        }
    }
    override fun onClick(dialog: DialogInterface, item: Int) {
        if (item == DialogInterface.BUTTON_POSITIVE) {
//            val item = role_spinner.selectedItemPosition
//            if(item == 0) {
//                role = "admin"
//            } else if (item == 1) {
//                role = "author"
//            } else if (item == 2) {
//                role = "reader"
//            }
//            dialogListener.sendTexts(userName, role)

            Toast.makeText(activity, "Member invited", Toast.LENGTH_LONG).show()
        } else if (item == DialogInterface.BUTTON_NEGATIVE) {
            Toast.makeText(activity, "Cancel", Toast.LENGTH_LONG).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogListener = context as DialogListener
    }

    interface DialogListener {
        fun sendTexts(userName: String, role: String)
    }
}
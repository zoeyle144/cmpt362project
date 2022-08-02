package com.example.cmpt362project.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.database.User
import com.example.cmpt362project.login.LoginPageActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class SearchUserFragment : Fragment() {

    private var columnCount = 1

    private val listOfUsernames = ArrayList<String>()
    private val listOfUsers = ArrayList<User>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: SearchUserAdapter
    private var initRecyclerViewAdapter = false

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_user, container, false)

        database = Firebase.database.reference

        val allUsersRef = database.child("users")
        allUsersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfUsernames.clear()
                listOfUsers.clear()
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
                if (initRecyclerViewAdapter) recyclerViewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        recyclerView = view.findViewById(R.id.search_user_search_results_recycler)
        recyclerView.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }

        recyclerViewAdapter = SearchUserAdapter(requireActivity(), listOfUsers)
        recyclerView.adapter = recyclerViewAdapter
        initRecyclerViewAdapter = true

        val searchView = view.findViewById<SearchView>(R.id.search_user_search_bar)
        val mySearchListener = SearchListener()
        searchView.setOnQueryTextListener(mySearchListener)

        val addAccountButton = view.findViewById<Button>(R.id.search_user_add_account_button)
        addAccountButton.setOnClickListener {
            addNewAccount()
        }

        return view
    }

    private fun addNewAccount() {
        val usernameTxt = UUID.randomUUID().toString().replace("-", "").take(8)
        val emailTxt = UUID.randomUUID().toString().replace("-", "").take(8)

        val userData = User(usernameTxt, "$emailTxt@test.com", "", "defaults/default_pfp.png", "")
        val entryID = "testUser_$usernameTxt"
        database.child("users").child(entryID).setValue(userData)
        println("Added user $entryID")
    }

    inner class SearchListener : SearchView.OnQueryTextListener {
        // https://www.geeksforgeeks.org/android-searchview-with-example/
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (listOfUsernames.contains(query)) {
                recyclerViewAdapter.filter.filter(query)
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            recyclerViewAdapter.filter.filter(newText)
            return false
        }
    }
}

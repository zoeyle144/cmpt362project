package com.example.cmpt362project.ui.search

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.database.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchUserFragment : Fragment() {

    private var columnCount = 1

    private val listOfUsernames = ArrayList<String>()
    private val listOfUsers = ArrayList<User>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: SearchUserAdapter

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
                for (i in snapshot.children) {
                    val username = i.child("username").value as String
                    val email = i.child("email").value as String
                    val user = User(username, email)

                    listOfUsernames.add(username)
                    listOfUsers.add(user)
                }
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

        val searchView = view.findViewById<SearchView>(R.id.search_user_search_bar)
        val mySearchListener = SearchListener()
        searchView.setOnQueryTextListener(mySearchListener)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    inner class SearchListener : SearchView.OnQueryTextListener {
        // https://www.geeksforgeeks.org/android-searchview-with-example/
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (listOfUsernames.contains(query)) {
                recyclerViewAdapter.filter.filter(query)

            } else {
                Toast.makeText(activity, "Not found", Toast.LENGTH_LONG).show()
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            recyclerViewAdapter.filter.filter(newText)
            return false
        }
    }

}

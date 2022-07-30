package com.example.cmpt362project.ui.search

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.cmpt362project.R

class SearchUserFragment : Fragment() {

    private val tempList = ArrayList<String>()
    private lateinit var tempAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_user, container, false)

        for (i in 0..25) {
            tempList.add(java.util.UUID.randomUUID().toString())
        }

        tempAdapter = activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, tempList) }!!
        val myListView = view.findViewById<ListView>(R.id.search_user_search_results)
        myListView.adapter = tempAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}
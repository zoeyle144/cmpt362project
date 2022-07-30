package com.example.cmpt362project.ui.search

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.ui.search.placeholder.PlaceholderContent

class SearchUserFragment : Fragment() {

    private val tempList = ArrayList<String>()
    private lateinit var tempAdapter: ArrayAdapter<String>
    private var columnCount = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_user, container, false)

        for (i in 0..25) {
            tempList.add(java.util.UUID.randomUUID().toString())
        }

//        tempAdapter = activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, tempList) }!!
//        val myListView = view.findViewById<ListView>(R.id.search_user_search_results)
//        myListView.adapter = tempAdapter

        val recyclerView = view.findViewById<RecyclerView>(R.id.search_bar_search_results_recycler)

        recyclerView.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        recyclerView.adapter = SearchUserAdapter(PlaceholderContent.ITEMS)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}
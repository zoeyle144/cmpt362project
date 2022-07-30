package com.example.cmpt362project.ui.search

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.cmpt362project.R

class SearchUserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_user, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}
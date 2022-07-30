package com.example.cmpt362project.ui.search

import android.os.Bundle
import android.view.*
import android.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.example.cmpt362project.R
import com.example.cmpt362project.databinding.FragmentSearchUserBinding

class SearchUserFragment : Fragment() {
    private lateinit var binding: FragmentSearchUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_user, container, false)
        binding = FragmentSearchUserBinding.inflate(inflater, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchUserFragmentToolbar.inflateMenu(R.menu.search_user_menu)

    }
}
package com.example.cmpt362project.ui.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.MainActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.CreateBoardActivity
import com.example.cmpt362project.activities.CreateChatActivity
import com.example.cmpt362project.activities.CreateGroupActivity
import com.example.cmpt362project.adaptors.ChatListAdaptor
import com.example.cmpt362project.adaptors.GroupChatListAdaptor
import com.example.cmpt362project.adaptors.GroupListAdaptor
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.ui.search.SearchUserFragment
import com.example.cmpt362project.viewModels.ChatListViewModel
import com.example.cmpt362project.viewModels.GroupChatListViewModel
import com.example.cmpt362project.viewModels.GroupListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GroupsListFragment : Fragment(), AdapterView.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_groups_list, container, false)
        val adapter = GroupListAdaptor(requireActivity(), listOf())

        val groupListViewModel: GroupListViewModel = ViewModelProvider(requireActivity())[GroupListViewModel::class.java]

        val groupListView = view.findViewById<ListView>(R.id.group_list)
        groupListView.onItemClickListener = this
        groupListView.adapter = adapter

        groupListViewModel.getGroups()
        groupListViewModel.groupsLiveData.observe(requireActivity()) { it ->
            adapter.clear()
            adapter.updateList(it)
            adapter.notifyDataSetChanged()
        }

        val floatActionButton = view.findViewById<FloatingActionButton>(R.id.add_group_button)
        floatActionButton.setOnClickListener{

            val intent = Intent(view.context, CreateGroupActivity::class.java)
            view.context.startActivity(intent)
        }


        return view
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
}
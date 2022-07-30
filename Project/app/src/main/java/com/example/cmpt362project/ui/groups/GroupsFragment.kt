package com.example.cmpt362project.ui.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.CreateBoardActivity
import com.example.cmpt362project.activities.CreateGroupActivity
import com.example.cmpt362project.adaptors.GroupListAdapter
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.ui.home.HomeViewModel
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.GroupListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GroupsFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var groupListAdapter: GroupListAdapter
    private lateinit var groupList: List<Group>
    private lateinit var groupListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_groups, container, false)
        val floatActionButton = view.findViewById<FloatingActionButton>(R.id.add_group_button)
        val groupListViewModel: GroupListViewModel = ViewModelProvider(requireActivity())[GroupListViewModel::class.java]

        groupListView = view.findViewById(R.id.group_list)
        groupList =  ArrayList()
        groupListAdapter = GroupListAdapter(requireActivity(), groupList)
        groupListView.adapter = groupListAdapter

        groupListViewModel.getGroups()
        groupListViewModel.groupsLiveData.observe(requireActivity()){
            groupListAdapter.updateList(it)
            groupListAdapter.notifyDataSetChanged()
        }

        floatActionButton.setOnClickListener{
            val intent = Intent(view.context, CreateGroupActivity::class.java)
            view.context.startActivity(intent)
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val prefSignature = prefs.getString("signature", "")
        val prefReply = prefs.getString("reply", "")
        val prefSync = prefs.getBoolean("sync", false)
        val prefAttachment = prefs.getBoolean("attachment", false)

        println("prefSignature is $prefSignature, prefReply is $prefReply, " +
                "prefSync is $prefSync, prefAttachment is $prefAttachment")


//        return root
        return view
    }
}
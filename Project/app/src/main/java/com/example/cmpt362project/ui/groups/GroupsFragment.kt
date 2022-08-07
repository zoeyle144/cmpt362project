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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<GroupListAdapter.ViewHolder>? = null
    private lateinit var groupList: List<Group>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_groups, container, false)
        val floatActionButton = view.findViewById<FloatingActionButton>(R.id.add_group_button)
        val groupListViewModel: GroupListViewModel = ViewModelProvider(requireActivity())[GroupListViewModel::class.java]
        val groupListView = view.findViewById<RecyclerView>(R.id.group_list)

        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        groupListView.layoutManager = layoutManager
        groupList =  ArrayList()
        adapter = GroupListAdapter(requireContext(),groupList, 0)
        groupListView.adapter = adapter

        groupListViewModel.getGroups()
        groupListViewModel.groupsLiveData.observe(requireActivity()){
            (adapter as GroupListAdapter).updateList(it)
            (adapter as GroupListAdapter).notifyDataSetChanged()
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
package com.example.cmpt362project.ui.invitation

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
import com.example.cmpt362project.adaptors.InvitationListAdaptor
import com.example.cmpt362project.models.Invitation
import com.example.cmpt362project.ui.search.SearchUserFragment
import com.example.cmpt362project.viewModels.InvitationViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InvitationListFragment : Fragment(), AdapterView.OnItemClickListener {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_invitations, container, false)
        val invitationListViewModel: InvitationViewModel = ViewModelProvider(requireActivity())[InvitationViewModel::class.java]
        val adapter = InvitationListAdaptor(requireActivity(), listOf(), invitationListViewModel)
        val invitationListView = view.findViewById<ListView>(R.id.invitation_list)

        invitationListView.onItemClickListener = this
        invitationListView.adapter = adapter

        invitationListViewModel.fetchInvitations()
        invitationListViewModel.invitationListData.observe(requireActivity()) { it ->
            adapter.clear()
            adapter.updateList(it)
            adapter.notifyDataSetChanged()
        }

        val floatActionButton = view.findViewById<FloatingActionButton>(R.id.add_board_button)
        floatActionButton.setOnClickListener{
            invitationListViewModel.insert(
                Invitation("temp", "SaPrBhTbfWPtxkNs5zGq3H4JXo62", "newman", "elWLQPeyKGXEBQbpcVZv3Pdv7Pk2", "N8iqOCHhm0MRjzlXADH")
            )

        }

        return view
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println(view)
    }
}
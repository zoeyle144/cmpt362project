package com.example.cmpt362project.ui.chat

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
import com.example.cmpt362project.adaptors.ChatListAdaptor
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.ui.search.SearchUserFragment
import com.example.cmpt362project.viewModels.ChatListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatListFragment : Fragment(), AdapterView.OnItemClickListener {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)
        val adapter = ChatListAdaptor(requireActivity(), listOf())

        val chatListViewModel: ChatListViewModel = ViewModelProvider(requireActivity())[ChatListViewModel::class.java]

        val chatListView = view.findViewById<ListView>(R.id.chat_list)
        chatListView.onItemClickListener = this
        chatListView.adapter = adapter

        chatListViewModel.fetchChats()
        chatListViewModel.chatListData.observe(requireActivity()) { it ->
            adapter.clear()
            adapter.updateList(it)
            adapter.notifyDataSetChanged()
        }

        /*
        val floatActionButton = view.findViewById<FloatingActionButton>(R.id.add_board_button)
        floatActionButton.setOnClickListener{

            val intent = Intent(view.context, CreateChatActivity::class.java)
            view.context.startActivity(intent)


            val fragment = SearchUserFragment()
            val fr = requireActivity().supportFragmentManager
            fr.popBackStack()
            fr.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit()

        }

         */

        return view
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
}
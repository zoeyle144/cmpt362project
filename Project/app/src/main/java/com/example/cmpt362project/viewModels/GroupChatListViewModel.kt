package com.example.cmpt362project.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.models.Group
import com.example.cmpt362project.models.GroupChat
import com.example.cmpt362project.repositories.BoardsRepository
import com.example.cmpt362project.repositories.ChatRepository
import com.example.cmpt362project.repositories.GroupChatRepository

class GroupChatListViewModel: ViewModel(){
    private val repository = GroupChatRepository()

    private val _chatsLiveData = MutableLiveData<List<GroupChat>>()
    val chatListData: LiveData<List<GroupChat>> = _chatsLiveData

    fun fetchChats(){
        repository.fetchChats(_chatsLiveData)
    }

    fun insert(chat: GroupChat){
        repository.insert(chat)
    }
}
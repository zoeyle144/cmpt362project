package com.example.cmpt362project.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.repositories.BoardsRepository
import com.example.cmpt362project.repositories.ChatRepository

class ChatListViewModel: ViewModel(){
    private val repository = ChatRepository()

    private val _chatsLiveData = MutableLiveData<List<Chat>>()
    val chatListData: LiveData<List<Chat>> = _chatsLiveData

    fun fetchChats(){
        repository.fetchChats(_chatsLiveData)
    }

    fun insert(chat: Chat){
        repository.insert(chat)
    }
}
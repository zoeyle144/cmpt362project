package com.example.cmpt362project.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Chat
import com.example.cmpt362project.models.Message
import com.example.cmpt362project.repositories.BoardsRepository
import com.example.cmpt362project.repositories.ChatRepository
import com.example.cmpt362project.repositories.MessageRepository

class MessageListViewModel: ViewModel(){
    private val repository = MessageRepository()

    private val _msgsLiveData = MutableLiveData<List<Message>>()
    val msgListData: LiveData<List<Message>> = _msgsLiveData

    fun fetchMessages(chatId:String){
        repository.fetchMsgs(_msgsLiveData, chatId)
    }

    fun insert(msg: Message){
        repository.insert(msg)
    }
}
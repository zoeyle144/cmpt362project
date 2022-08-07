package com.example.cmpt362project.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Invitation
import com.example.cmpt362project.repositories.InvitationRepository

class InvitationViewModel: ViewModel(){
    private val repository = InvitationRepository()

    private val _invitationLiveData = MutableLiveData<List<Invitation>>()
    val invitationListData: LiveData<List<Invitation>> = _invitationLiveData

    fun fetchInvitations(){
        repository.fetchInvitations(_invitationLiveData)
    }

    fun insert(invitation: Invitation){
        repository.insert(invitation)
    }

    fun delete(invitation_id: String) {
        repository.delete(invitation_id)
    }

}
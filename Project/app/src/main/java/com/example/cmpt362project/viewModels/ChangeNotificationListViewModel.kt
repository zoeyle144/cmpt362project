package com.example.cmpt362project.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.ChangeNotification
import com.example.cmpt362project.repositories.ChangeNotificationsRepository

class ChangeNotificationListViewModel {
    private val repository = ChangeNotificationsRepository()

    private val _changeNotificationsLiveData = MutableLiveData<List<ChangeNotification>>()
    val changeNotificationsLiveData: LiveData<List<ChangeNotification>> = _changeNotificationsLiveData

    fun fetchChangeNotifications(groupID: String){
        repository.fetchChangeNotifications(_changeNotificationsLiveData, groupID)
    }
}
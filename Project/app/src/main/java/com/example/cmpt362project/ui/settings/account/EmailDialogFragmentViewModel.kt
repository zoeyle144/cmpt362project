package com.example.cmpt362project.ui.settings.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class EmailDialogFragmentViewModel : ViewModel() {

    enum class WaitBoolean {
        TRUE, FALSE, WAIT
    }

    fun reAuthenticate(user: FirebaseUser?, password: String) : LiveData<WaitBoolean> {
        val success = MutableLiveData<WaitBoolean>()
        success.value = WaitBoolean.WAIT

        viewModelScope.launch {
            if (user != null) {
                val credential = EmailAuthProvider.getCredential(user.email!!, password)
                user.reauthenticate(credential).addOnCompleteListener {
                    success.postValue(if (it.isSuccessful) WaitBoolean.TRUE else WaitBoolean.FALSE)
                }
            } else success.postValue(WaitBoolean.FALSE)
        }
        return success
    }
}
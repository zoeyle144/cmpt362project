package com.example.cmpt362project.ui.settings.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class ReAuthenticator : ViewModel() {

    // Use viewModelScope to do suspend functions: https://reddit.com/r/Kotlin/comments/iceztd//g3ogp49/
    // Use viewModelScope to return value: https://stackoverflow.com/a/60911126
    fun reAuthenticate(user: FirebaseUser?, password: String) : LiveData<ReAuthenticateBoolean> {
        val success = MutableLiveData<ReAuthenticateBoolean>()
        success.value = ReAuthenticateBoolean.WAIT

        viewModelScope.launch {
            if (user != null) {
                val credential = EmailAuthProvider.getCredential(user.email!!, password)
                user.reauthenticate(credential).addOnCompleteListener {
                    success.postValue(if (it.isSuccessful) ReAuthenticateBoolean.TRUE else ReAuthenticateBoolean.FALSE)
                }
            } else success.postValue(ReAuthenticateBoolean.FALSE)
        }
        return success
    }
}
package com.example.cmpt362project.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginPageViewModel : ViewModel() {
    enum class LoginPageErrors {
        EMAIL_EMPTY, PASSWORD_EMPTY, NO_USER_ID, WRONG_CREDENTIALS
    }
    enum class LoginPageVMState {
        SUCCESS, FAILURE, WAIT
    }

    val database: DatabaseReference = Firebase.database.reference
    val auth: FirebaseAuth = Firebase.auth

    private val _errorList = MutableLiveData<ArrayList<LoginPageErrors>>(arrayListOf())
    val errorList: LiveData<ArrayList<LoginPageErrors>> get() = _errorList

    private var _returnVal = Pair("", "")
    val returnVal: Pair<String, String> get() = _returnVal

    fun clearErrorList() {
        _errorList.value?.clear()
    }

    fun login(email: String, password: String) : LiveData<LoginPageVMState> {
        val success = MutableLiveData<LoginPageVMState>()
        success.value = LoginPageVMState.WAIT

        viewModelScope.launch {
            var checkFields = true
            if (email.isEmpty()) {
                _errorList.value?.add(LoginPageErrors.EMAIL_EMPTY)
                checkFields = false
            }
            if (password.isEmpty()) {
                _errorList.value?.add(LoginPageErrors.PASSWORD_EMPTY)
                checkFields = false
            }

            if (!checkFields) {
                success.postValue(LoginPageVMState.FAILURE)
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {

                    val user = auth.currentUser
                    database.child("users").child(user!!.uid).get().addOnSuccessListener {
                        if (it.value == null) {
                            _errorList.value?.add(LoginPageErrors.NO_USER_ID)
                            checkFields = false
                        } else {
                            val userData = it.value as Map<*, *>
                            _returnVal = Pair(userData["username"].toString(), userData["email"].toString())
                            success.postValue(LoginPageVMState.SUCCESS)
                        }
                    }
                }.addOnFailureListener {
                    _errorList.value?.add(LoginPageErrors.WRONG_CREDENTIALS)
                    success.postValue(LoginPageVMState.FAILURE)
                }
            }
        }

        return success
    }
}
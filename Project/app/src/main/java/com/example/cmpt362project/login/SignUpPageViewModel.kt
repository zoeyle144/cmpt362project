package com.example.cmpt362project.login

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cmpt362project.database.User
import com.example.cmpt362project.utility.GlobalStrings.DEFAULT_PFP_PATH
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SignUpPageViewModel : ViewModel() {

    enum class SignUpPageErrors {
        EMAIL_EMPTY, PASSWORD_EMPTY, USERNAME_SHORT, PASSWORD_DIFFERENT, USERNAME_TAKEN,
        PASSWORD_WEAK, EMAIL_MALFORMED, EMAIL_TAKEN, UNKNOWN_ERROR
    }
    enum class SignUpPageVMState {
        SUCCESS, FAILURE, WAIT
    }

    val database: DatabaseReference = Firebase.database.reference
    val auth: FirebaseAuth = Firebase.auth

    private val _errorList = MutableLiveData<ArrayList<SignUpPageErrors>>(arrayListOf())
    val errorList: LiveData<ArrayList<SignUpPageErrors>> get() = _errorList

    fun clearErrorList() {
        _errorList.value?.clear()
    }

    fun signUp(email: String, username: String, password: String, confirmPassword: String)
    : LiveData<SignUpPageVMState> {
        val success = MutableLiveData<SignUpPageVMState>()
        success.value = SignUpPageVMState.WAIT

        viewModelScope.launch {
            var checkFields = true
            if (email.isEmpty()) {
                _errorList.value?.add(SignUpPageErrors.EMAIL_EMPTY)
                checkFields = false
            }
            if (password.isEmpty()) {
                _errorList.value?.add(SignUpPageErrors.PASSWORD_EMPTY)
                checkFields = false
            }
            if (username.length <= 3) {
                _errorList.value?.add(SignUpPageErrors.USERNAME_SHORT)
                checkFields = false
            }
            if (password != confirmPassword) {
                _errorList.value?.add(SignUpPageErrors.PASSWORD_DIFFERENT)
                checkFields = false
            }

            if (!checkFields) {
                println("In (!checkFields")
                println("checkFields Errors are ${errorList.value}")
                println("checkFields _Errors are ${_errorList.value}")
                success.postValue(SignUpPageVMState.FAILURE)
            } else {
                println("In viewModelScope.launch")

                // Referenced for ideas: https://stackoverflow.com/questions/35243492/firebase-android-make-username-unique
                database.child("usernames").child(username).get().addOnSuccessListener {
                    if (it.value != null) {
                        _errorList.value?.add(SignUpPageErrors.USERNAME_TAKEN)
                        success.postValue(SignUpPageVMState.FAILURE)
                    } else {
                        // add login data to auth
                        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                            // Sign up success
                            val user = auth.currentUser
                            val userData = User(username, email, "", DEFAULT_PFP_PATH, "")

                            database.child("users").child(user!!.uid).setValue(userData)

                            // add unique username into database
                            database.child("usernames").child(username).setValue(user.uid)

                            success.postValue(SignUpPageVMState.SUCCESS)
                        }.addOnFailureListener { e ->
                            when(e) {
                                is FirebaseAuthWeakPasswordException -> {
                                    _errorList.value?.add(SignUpPageErrors.PASSWORD_WEAK)
                                    success.postValue(SignUpPageVMState.FAILURE)
                                }
                                is FirebaseAuthInvalidCredentialsException -> {
                                    _errorList.value?.add(SignUpPageErrors.EMAIL_MALFORMED)
                                    success.postValue(SignUpPageVMState.FAILURE)
                                }
                                is FirebaseAuthUserCollisionException -> {
                                    _errorList.value?.add(SignUpPageErrors.EMAIL_TAKEN)
                                    success.postValue(SignUpPageVMState.FAILURE)
                                }
                            }
                        }
                    }
                }.addOnFailureListener{
                    _errorList.value?.add(SignUpPageErrors.UNKNOWN_ERROR)
                    success.postValue(SignUpPageVMState.FAILURE)
                }
            }
        }

        return success
    }
}
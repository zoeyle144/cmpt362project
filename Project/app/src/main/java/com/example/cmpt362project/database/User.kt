package com.example.cmpt362project.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (val username: String , val email: String) {

}
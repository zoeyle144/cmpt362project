package com.example.cmpt362project.ui.groups

interface FirebaseSuccessListener {
    fun setSearchResult(existed: Boolean)
    fun setPermissionID (permissionID: String)
    fun setUserRole(role: String)

}
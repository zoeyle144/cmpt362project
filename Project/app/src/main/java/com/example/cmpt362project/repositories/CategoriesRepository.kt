package com.example.cmpt362project.repositories

import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Category
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CategoriesRepository {

    val database = Firebase.database
    val auth = Firebase.auth
    val categoriesRef = database.getReference("categories")

    fun fetchCategories(liveData: MutableLiveData<List<Category>>){
        categoriesRef
            .orderByChild("createdBy").equalTo(auth.currentUser?.uid.toString())
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categories: List<Category> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Category::class.java)!!
                    }
                    liveData.postValue(categories)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}
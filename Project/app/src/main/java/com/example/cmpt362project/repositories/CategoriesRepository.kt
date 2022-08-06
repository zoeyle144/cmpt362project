package com.example.cmpt362project.repositories

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.models.ChangeNotification
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CategoriesRepository {

    val database = Firebase.database
    val auth = Firebase.auth
    val categoriesRef = database.getReference("boards")

    fun fetchCategories(liveData: MutableLiveData<List<Category>>, boardID: String){
        categoriesRef
            .child(boardID)
            .child("categories")
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

    fun insert(category: Category, boardID:String){
        categoriesRef
            .child(boardID)
            .child("categories")
            .child(category.categoryID)
            .orderByChild("title")
            .equalTo(category.title)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()){
                        categoriesRef.child(boardID).child("categories").child(category.categoryID).setValue(category)
                            .addOnCompleteListener{
                                println("debug: add category success")
                            }.addOnFailureListener{ err ->
                                println("debug: add category fail Error ${err.message}")
                            }
                    }else{
                        println("debug: add category failed because category already exist")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    println("debug: check category failed Error: ${error.message}")
                }
            })
    }

    fun delete(boardID:String, categoryID:String){
        categoriesRef.child(boardID).child("categories").child(categoryID).removeValue()
            .addOnSuccessListener {
                println("debug: delete category success")
            }.addOnFailureListener{ err ->
                println("debug: delete category fail Error ${err.message}")
            }

    }
}
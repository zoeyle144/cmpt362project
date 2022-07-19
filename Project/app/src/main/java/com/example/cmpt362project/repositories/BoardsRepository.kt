package com.example.cmpt362project.repositories

import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Board
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BoardsRepository {

    val database = Firebase.database
    val auth = Firebase.auth
    val boardsRef = database.getReference("boards")

    fun fetchBoards(liveData: MutableLiveData<List<Board>>){
        boardsRef
            .orderByChild("createdBy").equalTo(auth.currentUser?.uid.toString())
            .addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val boards: List<Board> = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(Board::class.java)!!
                }

                liveData.postValue(boards)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}
package com.example.cmpt362project.repositories

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.database.User
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.BoardUpdateData
import com.example.cmpt362project.models.ChangeNotification
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
    var usersRef = database.getReference("users")
    private val changeNotificationsRef = database.getReference("changeNotifications")

    fun fetchBoards(liveData: MutableLiveData<List<Board>>, groupID:String){
        boardsRef
            .orderByChild("groupID").equalTo(groupID)
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

    fun insert(board: Board){
        boardsRef.child(board.boardID).setValue(board)
            .addOnCompleteListener{
                println("debug: add board success")
            }.addOnFailureListener{ err ->
                println("debug: add board fail Error ${err.message}")
            }
        val changeNotificationKey = changeNotificationsRef.push().key!!
        usersRef.child(auth.currentUser?.uid.toString()).child("username").get()
            .addOnSuccessListener {
                val user = it.value as String
                val changeNotification = ChangeNotification(user,"board insert", board.boardName, board.groupID)
                changeNotificationsRef.child(changeNotificationKey).setValue(changeNotification)
            }.addOnFailureListener{
            }
    }

    fun delete(boardID:String, boardName:String, groupID:String){
        boardsRef.child(boardID).removeValue()
            .addOnSuccessListener {
                println("debug: delete board success")
            }.addOnFailureListener{ err ->
                println("debug: delete board fail Error ${err.message}")
            }
        val changeNotificationKey = changeNotificationsRef.push().key!!
        usersRef.child(auth.currentUser?.uid.toString()).child("username").get()
            .addOnSuccessListener {
                val user = it.value as String
                val changeNotification = ChangeNotification(user,"board delete", boardName, groupID)
                changeNotificationsRef.child(changeNotificationKey).setValue(changeNotification)
            }.addOnFailureListener{
            }
    }

    fun update(boardID:String, boardUpdateData:BoardUpdateData){
        val boardUpdate = hashMapOf<String, Any>(
            "/boardName" to boardUpdateData.boardName,
            "/description" to boardUpdateData.description,
        )
        boardsRef.child(boardID).updateChildren(boardUpdate)
            .addOnSuccessListener {
                println("debug: update board success")
            }.addOnFailureListener{ err ->
                println("debug: update board fail Error ${err.message}")
            }
    }
}
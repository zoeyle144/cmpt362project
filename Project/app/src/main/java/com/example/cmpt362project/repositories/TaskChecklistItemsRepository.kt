package com.example.cmpt362project.repositories

import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.TaskChecklistItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TaskChecklistItemsRepository {

    val database = Firebase.database
    val auth = Firebase.auth
    val boardsRef = database.getReference("boards")

    fun fetchTasks(liveData: MutableLiveData<List<TaskChecklistItem>>, boardID: String, taskID: String){
        boardsRef
            .child(boardID)
            .child("tasks")
            .child(taskID)
            .child("checklist")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks: List<TaskChecklistItem> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(TaskChecklistItem::class.java)!!
                    }
                    liveData.postValue(tasks)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun insert(boardID:String, taskID: String, taskCheckListItem: TaskChecklistItem, ){
        boardsRef.child(boardID).child("tasks").child(taskID)
            .child("checklist").child(taskCheckListItem.taskChecklistItemID).setValue(taskCheckListItem)
            .addOnCompleteListener{
                println("debug: add checklist item success")
            }.addOnFailureListener{ err ->
                println("debug: add checklist item fail Error ${err.message}")
            }
    }

    fun delete(boardID: String, taskID:String, itemID:String){
        boardsRef.child(boardID).child("tasks").child(taskID).child("checklist").child(itemID).removeValue()
            .addOnSuccessListener {
                println("debug: delete checklist item success")
            }.addOnFailureListener{ err ->
                println("debug: delete checklist item fail Error ${err.message}")
            }
    }

}
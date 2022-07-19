package com.example.cmpt362project.repositories

import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TasksRepository {

    val database = Firebase.database
    val auth = Firebase.auth
    val tasksRef = database.getReference("tasks")

    fun fetchTasks(liveData: MutableLiveData<List<Task>>){
        tasksRef
            .orderByChild("createdBy").equalTo(auth.currentUser?.uid.toString())
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks: List<Task> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Task::class.java)!!
                    }
                    liveData.postValue(tasks)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}
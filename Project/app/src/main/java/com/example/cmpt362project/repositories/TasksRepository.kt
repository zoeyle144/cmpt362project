package com.example.cmpt362project.repositories

import androidx.lifecycle.MutableLiveData
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.models.ChangeNotification
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.models.TaskUpdateData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TasksRepository {

    val database = Firebase.database
    val auth = Firebase.auth
    val tasksRef = database.getReference("boards")

    fun fetchTasks(liveData: MutableLiveData<List<Task>>, boardID: String){
        tasksRef
            .child(boardID)
            .child("tasks")
            .orderByChild("endDate")
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

    fun insert(task: Task, boardID:String){
        tasksRef.child(boardID).child("tasks").child(task.taskID).setValue(task)
            .addOnCompleteListener{
                println("debug: add task success")
            }.addOnFailureListener{ err ->
                println("debug: add task fail Error ${err.message}")
            }
    }

    fun delete(boardID: String, id:String){
        tasksRef.child(boardID).child("tasks").child(id).removeValue()
            .addOnSuccessListener {
                println("debug: delete task success")
            }.addOnFailureListener{ err ->
                println("debug: delete task fail Error ${err.message}")
            }
    }

    fun updateCategory(boardID: String, id: String, category:String){
        tasksRef.child(boardID).child("tasks").child(id).child("category").setValue(category)
            .addOnSuccessListener {
                println("debug: update task category success")
            }.addOnFailureListener{ err ->
                println("debug: update task category fail Error ${err.message}")
            }
    }

    fun updateTask(boardID: String, task: TaskUpdateData){
        val taskUpdate = hashMapOf<String, Any>(
            "/name" to task.name,
            "/summary" to task.summary,
            "/type" to task.type,
            "/assignedUser" to task.assignedUser,
            "/assignedUserID" to task.assignedUserID,
            "/startDate" to task.startDate,
            "/endDate" to task.endDate
        )
        tasksRef.child(boardID).child("tasks").child(task.taskID).updateChildren(taskUpdate)
            .addOnSuccessListener {
                println("debug: update task success")
            }.addOnFailureListener{ err ->
                println("debug: update task fail Error ${err.message}")
            }
    }

}
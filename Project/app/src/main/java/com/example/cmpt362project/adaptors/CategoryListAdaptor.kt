package com.example.cmpt362project.adaptors

import android.app.AlertDialog
import android.content.ClipDescription
import android.content.Intent
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.MainActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.CreateCategoryActivity
import com.example.cmpt362project.activities.CreateTaskActivity
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.models.Permission
import com.example.cmpt362project.models.Task
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.example.cmpt362project.viewModels.TaskListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CategoryListAdaptor(private var categoryList: List<Category>, private var boardTitle:String, private var boardID:String, private var groupID:String) : RecyclerView.Adapter<CategoryListAdaptor.ViewHolder>(){
    private lateinit var vmsForDrag: ViewModelStoreOwner
    private lateinit var  categoryListViewModel: CategoryListViewModel
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var permissionEntries: List<Permission>
    private lateinit var groupIDs: ArrayList<String>
    private lateinit var roles: ArrayList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListAdaptor.ViewHolder {
        if(viewType == 0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.category_list_adaptor, parent, false)
            return ViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.add_category_button, parent, false)
            val addCategoryButton = view.findViewById<Button>(R.id.add_category_button)

            database = Firebase.database
            auth = Firebase.auth
            val permissionRef = database.getReference("permission")
            permissionRef
                .orderByChild("uid")
                .equalTo(auth.currentUser?.uid.toString())
                .get()
                .addOnSuccessListener {
                    if (it.exists()){
                        permissionEntries = it.children.map { dataSnapshot ->
                            dataSnapshot.getValue(Permission::class.java)!!
                        }
                        val iterator = permissionEntries.listIterator()
                        groupIDs = ArrayList()
                        roles = ArrayList()
                        for (i in iterator) {
                            groupIDs.add(i.groupID)
                            roles.add(i.role)
                        }
                        if (groupIDs.indexOf(groupID) >= 0){
                            if (roles[groupIDs.indexOf(groupID)] == "reader" || roles[groupIDs.indexOf(groupID)] == "writer"){
                                addCategoryButton.isEnabled = false
                            }
                        }
                    }
                }.addOnFailureListener{
                }

            addCategoryButton.setOnClickListener {
                val intent = Intent(view.context, CreateCategoryActivity::class.java)
                intent.putExtra("boardTitle", boardTitle)
                intent.putExtra("boardID", boardID)
                view.context.startActivity(intent)
            }
            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: CategoryListAdaptor.ViewHolder, position: Int) {
        if (position < itemCount-1){
            vmsForDrag = holder.vms
            holder.itemTitle?.text = categoryList[position].title
            val categoryTitle = categoryList[position].title

            val taskListView: RecyclerView = holder.itemView.findViewById(R.id.task_list)
            val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
            taskListView.layoutManager = layoutManager
            val taskList: List<Task> = ArrayList()
            val adapter: RecyclerView.Adapter<TaskListAdaptor.ViewHolder> = TaskListAdaptor(taskList, boardID, groupID)
            taskListView.adapter = adapter
            taskListView.setOnDragListener(dragListener)
            taskListView.addItemDecoration(
                DividerItemDecoration(
                    holder.itemView.context,
                    DividerItemDecoration.VERTICAL
                )
            )

            holder.taskListViewModel.fetchTasks(boardID)
            holder.taskListViewModel.tasksLiveData.observe(holder.lifecycleOwner){
                val mutableIt = it.toMutableList()
                mutableIt.removeIf{ it -> it.category != categoryTitle}
                val mutableList = mutableIt.toList()
                (adapter as TaskListAdaptor).updateList(mutableList)
                (adapter as TaskListAdaptor).notifyDataSetChanged()
            }

            val deleteCategoryButton = holder.itemView.findViewById<Button>(R.id.delete_category_button)

            deleteCategoryButton.setOnClickListener {
                val confirmationBuilder = AlertDialog.Builder(holder.itemView.context)
                confirmationBuilder.setMessage("Are you sure you want to Delete Category <$categoryTitle>?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        val numOfTasksUnderCategory = taskListView.childCount
                        var taskIDsToDelete: MutableList<String> = ArrayList()
                        for (i in 0 until numOfTasksUnderCategory) {
                            taskIDsToDelete.add(taskListView.children.toList()[i].findViewById<TextView>(R.id.task_id).text.toString())
                        }
                        categoryListViewModel = ViewModelProvider(holder.vms)[CategoryListViewModel::class.java]
                        categoryListViewModel.delete(boardID, categoryList[position].categoryID, taskIDsToDelete)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                val alert = confirmationBuilder.create()
                alert.show()
            }

            val addTaskButton = holder.itemView.findViewById<Button>(R.id.add_task_button)
            addTaskButton.setOnClickListener {
                val intent = Intent(holder.itemView.context, CreateTaskActivity::class.java)
                intent.putExtra("category_title", categoryTitle)
                intent.putExtra("boardID", boardID)
                holder.itemView.context.startActivity(intent)
            }

            database = Firebase.database
            auth = Firebase.auth
            val permissionRef = database.getReference("permission")
            permissionRef
                .orderByChild("uid")
                .equalTo(auth.currentUser?.uid.toString())
                .get()
                .addOnSuccessListener {
                    if (it.exists()){
                        permissionEntries = it.children.map { dataSnapshot ->
                            dataSnapshot.getValue(Permission::class.java)!!
                        }
                        val iterator = permissionEntries.listIterator()
                        groupIDs = ArrayList()
                        roles = ArrayList()
                        for (i in iterator) {
                            groupIDs.add(i.groupID)
                            roles.add(i.role)
                        }
                        if (groupIDs.indexOf(groupID) >= 0){
                            if (roles[groupIDs.indexOf(groupID)] == "reader" || roles[groupIDs.indexOf(groupID)] == "writer"){
                                deleteCategoryButton.isEnabled = false
                                if(roles[groupIDs.indexOf(groupID)] == "reader"){
                                    addTaskButton.isEnabled = false
                                }
                            }
                        }
                    }
                }.addOnFailureListener{
                }

        }
    }

    override fun getItemCount(): Int {
        return categoryList.size+1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < itemCount-1){
            0
        }else{
            1
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemTitle: TextView? = itemView.findViewById(R.id.item_title)
        var vms: ViewModelStoreOwner = itemView.context as ViewModelStoreOwner
        var taskListViewModel: TaskListViewModel = ViewModelProvider(vms)[TaskListViewModel::class.java]
        var lifecycleOwner:LifecycleOwner = itemView.context as LifecycleOwner
    }

    fun updateList(newList:List<Category>){
        categoryList = newList
    }

    private val dragListener = View.OnDragListener{view, event ->
        when(event.action){
            DragEvent.ACTION_DRAG_STARTED -> {
                println("debug: ACTION_DRAG_STARTED ")
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                println("debug: ACTION_DRAG_ENTERED")
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                println("debug: ACTION_DRAG_EXITED")
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                println("debug: ACTION_DROP")
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                println("debug: dragData is $dragData")
                view.invalidate()
                val v = event.localState as CardView
                val taskID = v.findViewById<TextView>(R.id.task_id).text.toString()
                val taskBoardID = v.findViewById<TextView>(R.id.task_board_id).text.toString()
                println("debug: cardview v title: $taskID")
                val owner = v.parent as RecyclerView
                owner.removeView(v)
                val destination = view as RecyclerView
                val destParent = view.parent as ConstraintLayout
                val categoryName = destParent.findViewById<TextView>(R.id.item_title).text.toString()
                println("debug: dest: $destination")
                println("debug: destParent: ${destination.parent}")
                println("debug: category: $categoryName")
                val taskListViewModel: TaskListViewModel = ViewModelProvider(vmsForDrag)[TaskListViewModel::class.java]
                taskListViewModel.updateCategory(taskBoardID, taskID, categoryName)
                destination.addView(v)
                v.visibility = View.VISIBLE
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                print("debug: ACTION_DRAG_ENDED")
                if (event.result){
                    view.invalidate()
                }else{
                    val v = event.localState as CardView
                    v.visibility = View.VISIBLE
                    view.invalidate()
                }
                view.invalidate()
                true
            }
            else -> {
                println("debug: else occured")
                false
            }
        }
    }
}
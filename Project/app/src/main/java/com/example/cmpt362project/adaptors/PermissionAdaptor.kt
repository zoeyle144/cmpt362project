package com.example.cmpt362project.adaptors

import androidx.recyclerview.widget.RecyclerView


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Message
import com.example.cmpt362project.models.Permission
import com.example.cmpt362project.viewModels.PermissionViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class PermissionAdaptor(val context: Context, private var permList: List<Permission>, private var vm: PermissionViewModel, editableIn: Boolean): BaseAdapter(),
    AdapterView.OnItemSelectedListener {

    val spinnerItems = listOf("Reader", "Writer", "Mod", "Admin")
    val spinnerItemsDatabase = listOf("reader", "writer", "mod", "admin")
    val editable = editableIn

    val auth = Firebase.auth
    override fun getCount(): Int {
        return permList.size
    }

    override fun getItem(position: Int): Any {
        return permList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = View.inflate(context, R.layout.member_list_view_layout, null)

        val textView = view.findViewById<TextView>(R.id.row_item_textview)
        val spinner = view.findViewById<Spinner>(R.id.role_spinner)
        val deleteBtn = view.findViewById<Button>(R.id.permission_delete_btn)

        textView.text = permList[p0].userName
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerItems)

        spinner.adapter = adapter
        spinner.setSelection(spinnerItemsDatabase.indexOf(permList[p0].role))
        spinner.onItemSelectedListener = this
        deleteBtn.setOnClickListener{
            vm.delete(permList[p0], auth.currentUser!!.uid)
        }
        if (auth.currentUser!!.uid == permList[p0].uid) {
            spinner.isEnabled = false
            deleteBtn.visibility = View.INVISIBLE
        } else if (editable) {
            spinner.isEnabled = true
            deleteBtn.visibility = View.VISIBLE
        }



        return view
    }

    fun clear() {
        permList = listOf()
    }

    fun updateList(list: List<Permission>) {
        permList = list
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        Log.w("DEBUG", spinnerItemsDatabase[position])

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}

//class FileDataAdapter : RecyclerView.Adapter<FileDataAdapter.ViewHolder>() {
//    var items : List<ProjectFilesModel> = listOf()
//        set(value) {
//            // implements setter for notifying item changed
//            field = value
//            notifyDataSetChanged()
//        }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileDataViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.filtered_file, parent, false)
//        return FileDataViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = items.count()
//
//    override fun onBindViewHolder(holder: FileDataViewHolder, position: Int) {
//        holder.containerView.fileNameLayout.text = fileList[position].fileName
//        holder.containerView.ctypeLayout.text = fileList[position].ctype
//        holder.containerView.floorLayout.text = "${fileList[position].floor}floor"
//    }
//}
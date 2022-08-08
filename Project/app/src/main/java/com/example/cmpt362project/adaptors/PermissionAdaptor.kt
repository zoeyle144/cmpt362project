package com.example.cmpt362project.adaptors

import android.app.Activity
import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Message
import com.example.cmpt362project.models.Permission
import com.example.cmpt362project.viewModels.PermissionViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class PermissionAdaptor(val context: Context, private var permList: List<Permission>, private var vm: PermissionViewModel, editableIn: Boolean): BaseAdapter(),
    AdapterView.OnItemSelectedListener {

    val spinnerItems = listOf("Reader", "Writer",  "Admin")
    val spinnerItemsDatabase = listOf("reader", "writer", "admin")
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
        var start = true
        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (!start) {
                        Log.w("DEBUG", spinnerItemsDatabase[position])
                        Log.w("DEBUG", permList[p0].permissionID.toString())

                        vm.replace(permList[p0].permissionID, spinnerItemsDatabase[position])
                    }
                    start = false
                }
                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }


        deleteBtn.setOnClickListener{
            val confirmationBuilder = AlertDialog.Builder(context)
            var leaving = false
            var message = "Are you sure you want to kick <${permList[p0].userName}>?"
            if (auth.currentUser!!.uid == permList[p0].uid) {
                message = "Are you sure you want to leave the group?"
                leaving = true
            }
            confirmationBuilder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    vm.delete(permList[p0], auth.currentUser!!.uid)
                    if (leaving) {
                        (context as Activity).finish()
                    }
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = confirmationBuilder.create()
            alert.show()

        }

        // role based ui changes
        if (auth.currentUser!!.uid == permList[p0].uid) {
            spinner.isEnabled = false
            deleteBtn.visibility = View.VISIBLE
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

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}

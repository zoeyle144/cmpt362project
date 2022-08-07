package com.example.cmpt362project.adaptors

import androidx.recyclerview.widget.RecyclerView


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.cmpt362project.R


class SpinnerAdapter(private var data: ArrayList<String>, private var spinnerItems: ArrayList<String>, private var context: Context): BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = View.inflate(context, R.layout.member_list_view_layout, null)

        val textView = view.findViewById<TextView>(R.id.row_item_textview)
        val spinner = view.findViewById<Spinner>(R.id.role_spinner)

        textView.setText(data.get(p0))
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerItems)

        spinner.setAdapter(adapter)

        return view
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
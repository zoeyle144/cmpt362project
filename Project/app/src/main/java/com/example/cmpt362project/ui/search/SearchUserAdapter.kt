package com.example.cmpt362project.ui.search

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.cmpt362project.database.User
//import com.example.cmpt362project.databinding.FragmentItemBinding
import com.example.cmpt362project.databinding.FragmentSearchUserEntryBinding

import com.example.cmpt362project.ui.search.placeholder.PlaceholderContent.PlaceholderItem

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class SearchUserAdapter(private var list: ArrayList<User>)
    : RecyclerView.Adapter<SearchUserAdapter.ViewHolder>(), Filterable {

    private var originalList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentSearchUserEntryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.usernameView.text = item.username
        holder.emailView.text = item.email
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(binding: FragmentSearchUserEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        val usernameView: TextView = binding.searchUserEntryUsername
        val emailView: TextView = binding.searchUserEntryEmail

        override fun toString(): String {
            return super.toString() + " '" + emailView.text + "'"
        }
    }

    // Adapted from https://stackoverflow.com/a/37735562
    override fun getFilter(): Filter {
        val customFilter = object : Filter() {
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                list = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filteredResults = ArrayList<User>()
                if (constraint != null) {
                    filteredResults = (if (constraint.isEmpty()) originalList
                    else getFilteredResults(constraint))
                }

                val filterResults = FilterResults()
                filterResults.values = filteredResults

                return filterResults
            }
        }

        return customFilter
    }

    // Adapted from https://stackoverflow.com/a/37735562
    private fun getFilteredResults(constraint: CharSequence?) : ArrayList<User> {
        val results = ArrayList<User>()

        if (constraint != null) {
            for (i in originalList) {
                if (i.username.contains(constraint)) results.add(i)
            }
        }

        return results
    }
}
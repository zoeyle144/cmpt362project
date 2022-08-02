package com.example.cmpt362project.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.example.cmpt362project.database.User
//import com.example.cmpt362project.databinding.FragmentItemBinding
import com.example.cmpt362project.databinding.FragmentSearchUserEntryBinding
import com.example.cmpt362project.ui.search.SearchUserResultActivity.Companion.KEY_SEARCH_USER_RESULT_ABOUT_ME
import com.example.cmpt362project.ui.search.SearchUserResultActivity.Companion.KEY_SEARCH_USER_RESULT_EMAIL
import com.example.cmpt362project.ui.search.SearchUserResultActivity.Companion.KEY_SEARCH_USER_RESULT_NAME
import com.example.cmpt362project.ui.search.SearchUserResultActivity.Companion.KEY_SEARCH_USER_RESULT_PROFILE_PIC
import com.example.cmpt362project.ui.search.SearchUserResultActivity.Companion.KEY_SEARCH_USER_RESULT_USERNAME
import com.example.cmpt362project.utility.ImageUtility

/**
 * [RecyclerView.Adapter] that can display a [User].
 * TODO: Replace the implementation with code for your data type.
 */
class SearchUserAdapter(private val context: Context, private var list: ArrayList<User>)
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
        ImageUtility.setImageViewToProfilePic(item.profilePic, holder.pictureView)

        holder.entryView.setOnClickListener {
            val intent = Intent(context, SearchUserResultActivity::class.java)
            intent.putExtra(KEY_SEARCH_USER_RESULT_USERNAME, item.username)
            intent.putExtra(KEY_SEARCH_USER_RESULT_EMAIL, item.email)
            intent.putExtra(KEY_SEARCH_USER_RESULT_NAME, item.name)
            intent.putExtra(KEY_SEARCH_USER_RESULT_PROFILE_PIC, item.profilePic)
            intent.putExtra(KEY_SEARCH_USER_RESULT_ABOUT_ME, item.aboutMe)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(binding: FragmentSearchUserEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        val entryView: LinearLayout = binding.searchUserEntryAll
        val usernameView: TextView = binding.searchUserEntryUsername
        val pictureView: ImageView = binding.searchUserEntryProfilePic

//        override fun toString(): String {
//            return super.toString() + " '" + emailView.text + "'"
//        }
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
                if (results.size == 5) {
                    println("Reached results limit")
                    return results
                }
                if (i.username.contains(constraint)) {
                    results.add(i)
                }
            }
        }

        return results
    }
}
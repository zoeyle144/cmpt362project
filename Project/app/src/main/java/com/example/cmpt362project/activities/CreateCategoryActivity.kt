package com.example.cmpt362project.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.CategoryListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateCategoryActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category)

        val createCategoryButton = findViewById<Button>(R.id.create_category_button)
        val createCategoryName = findViewById<EditText>(R.id.create_category_name_input)
        createCategoryButton.setOnClickListener{

            val database = Firebase.database
            val categoriesRef = database.getReference("categories")
            val categoryID = categoriesRef.push().key!!
            val categoryListViewModel: CategoryListViewModel = ViewModelProvider(this)[CategoryListViewModel::class.java]
            val categoryName = createCategoryName.text
            val boardTitle = intent.getSerializableExtra("boardTitle").toString()
            val boardID = intent.getSerializableExtra("boardID").toString()
            val groupID = intent.getSerializableExtra("groupID").toString()
            auth = Firebase.auth
            val createdBy = auth.currentUser?.uid
            val category = Category(categoryID, categoryName.toString(), createdBy.toString(), boardTitle)
            categoryListViewModel.insert(category, groupID, boardID)
            finish()

        }
    }
}
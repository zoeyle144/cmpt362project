package com.example.cmpt362project.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Category
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
            val categoryName = createCategoryName.text
            auth = Firebase.auth
            val createdBy = auth.currentUser?.uid
            val category = Category(categoryName.toString(), createdBy.toString(),ArrayList())

            categoriesRef.child(categoryID).setValue(category)
                .addOnCompleteListener{
                    Toast.makeText(this, "Category Created Successfully", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{ err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

            finish()

        }
    }
}
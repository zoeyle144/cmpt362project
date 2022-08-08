package com.example.cmpt362project.ui.settings

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.cmpt362project.R

class AboutAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

        val toolbar = findViewById<Toolbar>(R.id.about_app_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.toolbar_attribution -> {
            val attributionMessage = "   URL: https://commons.wikimedia.org/wiki/File:Vancouver_Sunrise_(189537003).jpeg\n\n" +
                    "   Attribution: Terry Lucas, CC BY 3.0 <https://creativecommons.org/licenses/by/3.0>, via Wikimedia Commons\n\n" +
                    "   Modifications: border was cropped out"

            val message = TextView(this)
            val s = SpannableString(attributionMessage)
            Linkify.addLinks(s, Linkify.WEB_URLS)
            message.text = s
            message.movementMethod = LinkMovementMethod.getInstance()

            val dialog = AlertDialog.Builder(this)
                .setTitle("Image attribution")
                .setCancelable(true)
                .setPositiveButton("OK", null)
                .setView(message)
                .create()
            dialog.show()

            true
        }
        else -> {
            finish()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.about_app_toolbar, menu)
        return true
    }
}
package com.example.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class FeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.art_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_art_item) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}
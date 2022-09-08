package com.example.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import com.example.android.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            val database = this.openOrCreateDatabase("Workers", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS workers(id INTEGER PRIMARY KEY,name VARCHAR,age INT)")

            database.execSQL("INSERT INTO workers(name,age)VALUES('ibo',10)")

            val cursor = database.rawQuery("SELECT * FROM Workers", null)
            val nameIx = cursor.getColumnIndex("name")
            val ageIx = cursor.getColumnIndex("age")
            val idIx = cursor.getColumnIndex("id")

            while (cursor.moveToNext()) {
                println("id:" + cursor.getInt(idIx))
                println("Name:" + cursor.getString(nameIx))
                println("Age:" + cursor.getInt(ageIx))
            }
            cursor.close()

        } catch (e: Exception) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()

        }
    }
}






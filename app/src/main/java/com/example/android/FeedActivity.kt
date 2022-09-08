package com.example.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class FeedActivity : AppCompatActivity() {
    private lateinit var artList:ArrayList<Art>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        artList=ArrayList<Art>()
        try{
            val database=this.openOrCreateDatabase("Arts", MODE_PRIVATE,null)
            val cursor=database.rawQuery("SELECT * FROM arts",null)
            val artNameIx=cursor.getColumnIndex("artname")
            val idIx=cursor.getColumnIndex("id")
            while(cursor.moveToNext()){
                val name=cursor.getString(artNameIx)
                val id=cursor.getInt(idIx)
                val art=Art(name,id)
                artList.add(art)
            }
            cursor.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
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
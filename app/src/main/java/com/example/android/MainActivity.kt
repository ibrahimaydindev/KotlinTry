package com.example.android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.android.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerLauncher()
    }

    fun saveButtonClicked(view: View) {
        val artname = binding.artName.text.toString()
        val artistname = binding.artistName.text.toString()
        val year = binding.year.text.toString()

        if (selectedBitmap != null) {
            //bitmap olusturduk
            val smallBitmap = makeSmallerBitmap(selectedBitmap!!, 300)

            //bitmapi 0 1 lere donusturmen
            val outputStream = ByteArrayOutputStream()
            smallBitmap?.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val byteArray = outputStream.toByteArray()

            //veritabanı islemleri try and catchde yapılır.
            try {
                val database = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null)

                database.execSQL("CREATE TABLE IF NOT EXISTS arts(id INTEGER PRIMARY KEY,artname VARCHAR,artistname VARCHAR,year VARCHAR,image BLOB)")
                val sqlString="INSERT INTO arts(artname,artistname,year,image) VALUES (?,?,?,?)"
                val statement=database.compileStatement(sqlString)
                statement.bindString(1,artname)
                statement.bindString(2,artistname)
                statement.bindString(3,year)
                statement.bindString(4, byteArray.toString())

            } catch (e: Exception) {
                e.printStackTrace()
            }
            val intent=Intent(this@MainActivity,FeedActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }


    }

    fun makeSmallerBitmap(image: Bitmap, maximumSize: Int): Bitmap? {
        var width = image.width
        var height = image.height

        val bitmapRatio: Double = width.toDouble() / height.toDouble()
        if (bitmapRatio > 1) {
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image, width, height, true)

    }

    fun selectImage(view: View) {


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give Permission",
                        View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val intentFromResult = it.data
                    if (intentFromResult != null) {
                        val imageData = intentFromResult.data
                        binding.imageView.setImageURI(imageData)
                        if (imageData != null) {

                            try {
                                if (Build.VERSION.SDK_INT >= 28) {
                                    val source = ImageDecoder.createSource(
                                        this@MainActivity.contentResolver,
                                        imageData
                                    )
                                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                                    binding.imageView.setImageBitmap(selectedBitmap)
                                } else {
                                    selectedBitmap = MediaStore.Images.Media.getBitmap(
                                        contentResolver,
                                        imageData
                                    )
                                    binding.imageView.setImageBitmap(selectedBitmap)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                } else {
                    Toast.makeText(this@MainActivity, "Permission Needed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}










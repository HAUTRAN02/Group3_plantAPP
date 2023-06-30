package com.example.plantappication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.plantappication.databinding.ActivityHomepageBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.Exception

private lateinit var binding: ActivityHomepageBinding
class HomepageActivity : AppCompatActivity() {
    lateinit var ptPath : String
    val REQUEST_TAKE_PHOTO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtName.text = intent.getStringExtra("name")

//        val bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"))
//        binding.avatar.setImageBitmap(bitmap)

        clickEvent()
        process()
        process1()
    }

    private fun process1() {
        val list = mutableListOf<outData1>()
        list.add(outData1(R.drawable.pic10))
        list.add(outData1(R.drawable.pic9))
        list.add(outData1(R.drawable.pic1))
        list.add(outData1(R.drawable.pic3))
        list.add(outData1(R.drawable.pic4))
        list.add(outData1(R.drawable.pic5))
        list.add(outData1(R.drawable.pic6))
        list.add(outData1(R.drawable.pic7))
        list.add(outData1(R.drawable.pic8))

        binding.rcView1.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)

        val itemAdapter = RvAdapter1(list, object :rvInterface{
            override fun onClick(pos: Int) {


            }

        })
        binding.rcView1.adapter = itemAdapter

    }

    private fun process() {
        val list = mutableListOf<outData>()
        list.add(outData(R.drawable.pic2,"Home Plants"))
        list.add(outData(R.drawable.h6,"Weeds"))
        list.add(outData(R.drawable.h7,"Trees"))
        list.add(outData(R.drawable.h8,"Toxic Plants"))
        list.add(outData(R.drawable.h1,"Vegetables"))
        list.add(outData(R.drawable.h2,"Leaf Plants"))
        list.add(outData(R.drawable.h3,"Flower"))
        list.add(outData(R.drawable.h4,"Fruits"))

        binding.rcView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)

        val itemAdapter = RvAdapter(list, object :rvInterface{
            override fun onClick(pos: Int) {
//                val i = Intent(this@HomepageActivity, FetchingActivity::class.java)
//                val loai:String = list[pos].title
//                i.putExtra("nameSpecial",loai)
//                startActivity(i)


            }

        })
        binding.rcView.adapter = itemAdapter

    }

    private fun clickEvent() {
        var click = true
        // Nhan dau +
        binding.btnadd.setOnClickListener {
            val intent= Intent(this@HomepageActivity,addNewPlant::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        // Nhan dau addnew
        binding.btnaddnew.setOnClickListener {
            click = if (click) {
                binding.btnaddnew.setBackgroundColor(Color.GREEN)
                false
            } else {
                binding.btnaddnew.setBackgroundColor(Color.WHITE)
                true
            }

            takePT()
        }

        // Nhan dau species
        binding.btnspecies.setOnClickListener {
            click = if (click) {
                binding.btnspecies.setBackgroundColor(Color.GREEN)
                false
            } else {
                binding.btnspecies.setBackgroundColor(Color.WHITE)
                true
            }

            val intent= Intent(this@HomepageActivity,specialActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        //Nhan article
        binding.btnArt.setOnClickListener {
            click = if (click) {
                binding.btnArt.setBackgroundColor(Color.GREEN)
                false
            } else {
                binding.avatar.setBackgroundColor(Color.WHITE)
                true
            }

            val intent= Intent(this@HomepageActivity,articleActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        //nhan profile
        binding.btnProfile.setOnClickListener {
            click = if (click) {
                binding.btnHome.setBackgroundResource(R.drawable.tab6)
                binding.btnProfile.setBackgroundResource(R.drawable.tab5)
                false
            } else {
                binding.btnadd.setBackgroundResource(R.drawable.ellipse_b)
                true
            }

            val intent= Intent(this@HomepageActivity,profileActivity::class.java)
            passImage()
            startActivity(intent)
        }

        binding.avatar.setOnClickListener {
            val intent= Intent(this@HomepageActivity,profileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun passImage() {
        val drawble = binding.avatar.drawable as BitmapDrawable
        val bitmap = drawble.bitmap
        createImageFromBitmap(bitmap)
    }



    private fun takePT() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if(intent.resolveActivity(packageManager) != null){
            var ptFile : File? = null
            try {
                ptFile = creatImageFile()
            } catch (e: IOException){}

            if (ptFile != null){
                val photoUri = FileProvider.getUriForFile(
                    this,
                    "com.example.android.fileprovider",
                    ptFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            binding.picture.rotation = 90f
            binding.picture.setImageURI(Uri.parse(ptPath))

            val i = Intent(this@HomepageActivity, addNewPlant::class.java)
            val drawble = binding.picture.drawable as BitmapDrawable
            val bitmap = drawble.bitmap
            createImageFromBitmap(bitmap)

            startActivity(i)
        }
    }

    fun createImageFromBitmap(bitmap: Bitmap): String? {
        var fileName: String? = "myImage" //no .png or .jpg needed
        try {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val fo = openFileOutput(fileName, Context.MODE_PRIVATE)
            fo.write(bytes.toByteArray())
            // remember close file output
            fo.close()
        } catch (e: Exception) {
            e.printStackTrace()
            fileName = null
        }
        return fileName
    }



    private fun creatImageFile(): File? {
        val fileName = "MyPictue"
        val storeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            fileName,
            ".jpg",
            storeDir
        )
        ptPath = image.absolutePath

        return image
    }


}
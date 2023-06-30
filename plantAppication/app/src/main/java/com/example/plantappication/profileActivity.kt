package com.example.plantappication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.appcompat.app.AlertDialog
import com.example.plantappication.databinding.ActivityHomepageBinding
import com.example.plantappication.databinding.ActivityProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.lang.Exception

private lateinit var binding: ActivityProfileBinding
class profileActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseStorage
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var selectImg: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"))
        binding.avatar.setImageBitmap(bitmap)

        storage = FirebaseStorage.getInstance()
        dialog = AlertDialog.Builder(this)
            .setMessage("uploading...")
            .setCancelable(false)

        clickEvent()
        val  adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tab, binding.viewPager) {tab, pos->
            when(pos){
                0->{tab.text = "ARTICLES"}
                1->{tab.text = "SPECIES"}

            }
        }.attach()






    }

    private fun clickEvent() {
        var click = true

        //nhan avatar
        binding.avatar.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        //nhan add
        binding.btnadd.setOnClickListener {
            val intent= Intent(this@profileActivity,addNewPlant::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        //nhan home
        binding.btnHome.setOnClickListener {
            click = if (click) {
                binding.btnHome.setBackgroundResource(R.drawable.tab2)
                binding.btnProfile.setBackgroundResource(R.drawable.tab4)
                false
            } else {
                binding.btnadd.setBackgroundResource(R.drawable.ellipse_b)
                true
            }

            val intent= Intent(this@profileActivity,HomepageActivity::class.java)
            passImage()
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            if (data.data != null){
                selectImg = data.data!!

                binding.avatar.setImageURI(selectImg)

            }
        }
    }

    private fun passImage() {
        val drawble = binding.avatar.drawable as BitmapDrawable
        val bitmap = drawble.bitmap
        createImageFromBitmap(bitmap)
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
}
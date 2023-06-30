package com.example.plantappication

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.plantappication.databinding.ActivityDetailArtBinding
import com.example.plantappication.databinding.ActivityDetailBinding

private lateinit var binding : ActivityDetailArtBinding

class detailArtActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //khởi tạo viewbinding
        binding = ActivityDetailArtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent= Intent(this@detailArtActivity,articleActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        // Nhan dau addnew
        binding.btnadd.setOnClickListener {
            val intent= Intent(this@detailArtActivity,addNewPlant::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        var click = true

        binding.ellipse.setOnClickListener(View.OnClickListener {
            click = if (click) {
                binding.ellipse.setBackgroundResource(R.drawable.ellipse_r)
                false
            } else {
                binding.ellipse.setBackgroundResource(R.drawable.ellipse_b)
                true
            }

        })


        binding.title.text = intent.getStringExtra("title")
        binding.name.text = intent.getStringExtra("nameAuthor")
        binding.date.text = intent.getStringExtra("postDate")
        binding.note1.text = intent.getStringExtra("species")
        binding.note2.text = intent.getStringExtra("species2")
        binding.content.text = intent.getStringExtra("content")

        val bundle : Bundle?= intent.extras
        val image : Int = bundle!!.getInt("pic")
        val image2 : Int = bundle!!.getInt("pic2")
        binding.titleImage.setImageResource(image)
        binding.iconuser.setImageResource(image2)

        val bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"))
        val bitmap2 = BitmapFactory.decodeStream(this.openFileInput("myImage2"))
        binding.titleImage.setImageBitmap(bitmap)
        binding.iconuser.setImageBitmap(bitmap2)

    }
}
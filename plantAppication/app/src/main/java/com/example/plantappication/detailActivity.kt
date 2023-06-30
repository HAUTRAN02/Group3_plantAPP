package com.example.plantappication

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plantappication.databinding.ActivityDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private lateinit var binding : ActivityDetailBinding

class detailActivity : AppCompatActivity() {
    private lateinit var storageRef : StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri : Uri? = null
    private var special : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //khởi tạo viewbinding
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        val loai:String = "plantLiked"
        special = loai

        storageRef = FirebaseStorage.getInstance().reference.child(loai)

        binding.Cactus.text = intent.getStringExtra("name")
        binding.note1.text = intent.getStringExtra("note")
        binding.kingdom.text = intent.getStringExtra("kingdom")
        binding.family.text = intent.getStringExtra("family")
        binding.description.text = intent.getStringExtra("description")

        val bundle : Bundle?= intent.extras
        val image : Int = bundle!!.getInt("pic")
        binding.titleImage.setImageResource(image)

        val bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"))
        binding.titleImage.setImageBitmap(bitmap)

        binding.back.setOnClickListener {
            val intent= Intent(this@detailActivity,specialActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        // Nhan dau addnew
        binding.btnadd.setOnClickListener {
            val intent= Intent(this@detailActivity,addNewPlant::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        var click = true

        binding.ellipse.setOnClickListener(View.OnClickListener {
            click = if (click) {
                binding.ellipse.setBackgroundResource(R.drawable.ellipse_r)
                uploadDataLike()
                false
            } else {
                binding.ellipse.setBackgroundResource(R.drawable.ellipse_b)
                true
            }

        })

    }

    private fun initVars() {
        firebaseFirestore = FirebaseFirestore.getInstance()

    }

    private fun uploadDataLike() {
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        //get info
//        imageUri = binding.titleImage.

        val name = binding.Cactus.text.toString()
        val kingDom = binding.kingdom.text.toString()
        val family= binding.family.text.toString()
        val desc = binding.description.text.toString()
        val note = binding.note1.text.toString()

        imageUri?.let {
            storageRef.putFile(it) .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    storageRef.downloadUrl.addOnSuccessListener {uri ->
                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()
                        map["name"] = name
                        map["kingdom"] = kingDom
                        map["family"] = family
                        map["description"] = desc
                        map["note"] = note

                        special?.let { it1 ->
                            firebaseFirestore.collection(it1).add(map).addOnCompleteListener { fireStoreTask ->
                                if(fireStoreTask.isSuccessful){
                                    Toast.makeText(this, "Uploaded successfully", Toast.LENGTH_SHORT).show()


                                }else{
                                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }

                } else{
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }



    }
}
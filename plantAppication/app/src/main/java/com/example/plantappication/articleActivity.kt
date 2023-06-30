package com.example.plantappication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptest.plantAdapter
import com.example.plantappication.databinding.ActivityArticleBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

private lateinit var binding: ActivityArticleBinding

class articleActivity : AppCompatActivity() {
    private lateinit var storageRef : StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var articleList: ArrayList<articleModule>
    private lateinit var articleAdapter: articleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //khởi tạo viewbinding
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent= Intent(this@articleActivity,HomepageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        // Nhan dau +
        binding.btnadd.setOnClickListener {
            val intent= Intent(this@articleActivity,addNewPlant::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        initVars()
        getData()
    }
    private fun getData() {
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection("Articles").orderBy("nameAuthor", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("Firebase store error", error.message.toString())
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            articleList.add(dc.document.toObject(articleModule::class.java))

                        }
                    }

                    articleAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun initVars() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvarticle.setHasFixedSize(true)
        binding.rvarticle.layoutManager = LinearLayoutManager(this)

        articleList = arrayListOf()
        articleAdapter = articleAdapter(articleList)
        binding.rvarticle.adapter = articleAdapter

        //Listen on click item
        articleAdapter.setOnItemClickListener(object : articleAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val i = Intent(this@articleActivity, detailArtActivity::class.java)
                i.putExtra("title", articleList[position].title)
                i.putExtra("nameAuthor", articleList[position].nameAuthor)
                i.putExtra("postDate", articleList[position].postDate)
                i.putExtra("species", articleList[position].species)
                i.putExtra("species2", articleList[position].species2)
                i.putExtra("content", articleList[position].content)

                with(articleList[position].pic){
                   Picasso.get().load(this).into(binding.image)
                }
                with(articleList[position].pic2){
                    Picasso.get().load(this).into(binding.image2)
                }

                val drawble = binding.image.drawable as BitmapDrawable
                val drawble2 = binding.image2.drawable as BitmapDrawable
                val bitmap = drawble.bitmap
                val bitmap2 = drawble2.bitmap
                createImageFromBitmap(bitmap)
                createImageFromBitmap2(bitmap2)
                startActivity(i)
            }
        })

        binding.progressBar.visibility = View.GONE
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

    fun createImageFromBitmap2(bitmap: Bitmap): String? {
        var fileName: String? = "myImage2" //no .png or .jpg needed
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
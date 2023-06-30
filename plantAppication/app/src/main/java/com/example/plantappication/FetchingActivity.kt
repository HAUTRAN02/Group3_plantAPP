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
import com.example.apptest.plantModule
import com.example.plantappication.databinding.ActivityFetchingBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

private lateinit var binding: ActivityFetchingBinding

class FetchingActivity : AppCompatActivity() {
    private lateinit var storageRef : StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var plantList: ArrayList<plantModule>
    private lateinit var plantAdapter: plantAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //khởi tạo viewbinding
        binding = ActivityFetchingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle?= intent.extras
        val spetit  = bundle!!.getString("special")

        binding.catus.setText(spetit.toString())
        binding.catusHint.setText(spetit.toString())

        binding.back.setOnClickListener {
            val intent= Intent(this@FetchingActivity,specialActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        // Nhan dau +
        binding.btnadd.setOnClickListener {
            val intent= Intent(this@FetchingActivity,addNewPlant::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        initVars()
        getData()
    }

    private fun getData() {
        val bundle : Bundle?= intent.extras
        val special  = bundle!!.getString("special")
        storageRef = special?.let { FirebaseStorage.getInstance().reference.child(it) }!!

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection(special).orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("Firebase store error", error.message.toString())
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            plantList.add(dc.document.toObject(plantModule::class.java))

                        }
                    }

                    plantAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun initVars() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvPlant.setHasFixedSize(true)
        binding.rvPlant.layoutManager = LinearLayoutManager(this)

        plantList = arrayListOf()
        plantAdapter = plantAdapter(plantList)
        binding.rvPlant.adapter = plantAdapter

        //Listen on click item
        plantAdapter.setOnItemClickListener(object : plantAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val i = Intent(this@FetchingActivity, detailActivity::class.java)
                i.putExtra("name", plantList[position].name)
                i.putExtra("note", plantList[position].note)
                i.putExtra("kingdom", plantList[position].kingdom)
                i.putExtra("family", plantList[position].family)
                i.putExtra("description", plantList[position].description)

                with(plantList[position].pic){
                    Picasso.get().load(this).into(binding.image)
                }
                val drawble = binding.image.drawable as BitmapDrawable
                val bitmap = drawble.bitmap
                createImageFromBitmap(bitmap)
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
}
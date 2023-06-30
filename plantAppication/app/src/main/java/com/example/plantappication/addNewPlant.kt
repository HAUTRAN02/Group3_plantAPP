package com.example.plantappication

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.plantappication.databinding.ActivityAddNewPlantBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private lateinit var binding: ActivityAddNewPlantBinding
class addNewPlant : AppCompatActivity() {
    private lateinit var storageRef : StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri : Uri? = null
    private var special : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //khởi tạo viewbinding
        binding = ActivityAddNewPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"))
        binding.imageView.setImageBitmap(bitmap)

        val listSpe = resources.getStringArray(R.array.special)
        val adt = ArrayAdapter(this, android.R.layout.simple_spinner_item, listSpe)
        binding.spSpecial.adapter = adt
        //khi click spinner
        binding.spSpecial.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val loai:String = listSpe[position]
                special = loai
                storageRef = FirebaseStorage.getInstance().reference.child(loai)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        initVars()
        registerClickEvent()

        binding.back.setOnClickListener {
            val intent= Intent(this@addNewPlant,HomepageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


    }

    private fun registerClickEvent() {
        binding.imageView.setOnClickListener {
            resultLauncher.launch("image/*")

        }
        binding.btnSave.setOnClickListener {
            uploadImge()
        }
    }

    private fun uploadImge() {
        binding.progressBar.visibility = View.VISIBLE
        storageRef = storageRef.child(System.currentTimeMillis().toString())
        //get text
        val name = binding.edtName.text.toString()
        val kingDom = binding.edtKingdom.text.toString()
        val family= binding.edtFamily.text.toString()
        val desc = binding.edtDesc.text.toString()
        val note = binding.edtNote.text.toString()

        //Kiểm tra điều kiện
        if(name.isEmpty()){
            binding.edtName.error = "Please enter name's plant"
            binding.progressBar.visibility = View.GONE
            return
        }
        if(kingDom.isEmpty()){
            binding.edtKingdom.error = "Please enter kingdom's plant"
            binding.progressBar.visibility = View.GONE
            return
        }
        if(family.isEmpty()){
            binding.edtFamily.error = "Please enter family's plant"
            binding.progressBar.visibility = View.GONE
            return
        }
        if(desc.isEmpty()){
            binding.edtDesc.error = "Please enter description's plant"
            binding.progressBar.visibility = View.GONE
            return
        }
        if(note.isEmpty()){
            binding.edtNote.error = "Please enter note's plant"
            binding.progressBar.visibility = View.GONE
            return
        }


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
                                binding.progressBar.visibility = View.GONE
                                binding.imageView.setImageResource(R.drawable.vector)
                                binding.edtName.setText("")
                                binding.edtKingdom.setText("")
                                binding.edtFamily.setText("")
                                binding.edtDesc.setText("")
                                binding.edtNote.setText("")

                            }
                        }
                    }

                } else{
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()){
        imageUri = it
        binding.imageView.setImageURI(it)

    }


    private fun initVars() {
        firebaseFirestore = FirebaseFirestore.getInstance()


    }
}
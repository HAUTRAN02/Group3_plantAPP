package com.example.plantappication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantappication.databinding.ActivityMainBinding
import com.example.plantappication.databinding.ActivityStartpage3Binding

private lateinit var binding: ActivityStartpage3Binding
class Startpage3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //khởi tạo viewbinding
        binding = ActivityStartpage3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val intent= Intent(this@Startpage3Activity,LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
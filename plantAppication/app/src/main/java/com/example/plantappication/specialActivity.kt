package com.example.plantappication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.plantappication.databinding.ActivitySpecialBinding

private lateinit var binding: ActivitySpecialBinding
class specialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //khởi tạo viewbinding
        binding = ActivitySpecialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEvent()
        binding.back.setOnClickListener {
            val intent= Intent(this@specialActivity,HomepageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // Nhan dau +
        binding.btnadd.setOnClickListener {
            val intent= Intent(this@specialActivity,addNewPlant::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    private fun addEvent() {
        showList()

    }

    private fun showList() {
        var arraySpecial = resources.getStringArray(R.array.special)
        binding.lvSpecial.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraySpecial)

        binding.lvSpecial.setOnItemClickListener { parent, view, position, id ->
            // Toast.makeText(this, arraySpecial[position], Toast.LENGTH_SHORT).show()
            val i = Intent(this@specialActivity, FetchingActivity::class.java)
            val loai:String = arraySpecial[position]
            i.putExtra("special",loai)
            startActivity(i)
        }
    }
}
package com.example.plantappication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.plantappication.databinding.ActivityResetPassBinding
import com.example.plantappication.databinding.ActivitySignupBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Matcher
import java.util.regex.Pattern

private lateinit var binding: ActivityResetPassBinding
class ResetPassActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onStart() {
        super.onStart()
        // Kiểm tra người dùng đã đăng nhập hay chưa (khác null) và cập nhật UI tương ứng.
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                val intent = Intent(applicationContext, HomepageActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //khởi tạo viewbinding
        binding = ActivityResetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Kiểm tra email hợp lệ
        class EmailValidator {
            private val pattern: Pattern
            private var matcher: Matcher? = null

            private val EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

            init {
                pattern = Pattern.compile(EMAIL_PATTERN)
            }

            fun validate(email: String): Boolean {
                matcher = pattern.matcher(email)
                return matcher!!.matches()
            }
        }

        val validator = EmailValidator()
        mAuth = FirebaseAuth.getInstance()

        binding.btnSendEmail.setOnClickListener {
            val email: String = binding.etLanguage.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@ResetPassActivity, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!validator.validate(email)) {
                Toast.makeText(this@ResetPassActivity, "Wrong email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task: Task<Void> ->
                if (task.isSuccessful) {
                    Toast.makeText(this@ResetPassActivity, "Please check your email", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@ResetPassActivity, "Enter correct email", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e: Exception ->
                Toast.makeText(this@ResetPassActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
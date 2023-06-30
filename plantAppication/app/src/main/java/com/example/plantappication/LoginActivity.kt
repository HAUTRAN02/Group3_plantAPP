package com.example.plantappication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.plantappication.databinding.ActivityLoginBinding
import com.example.plantappication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Matcher
import java.util.regex.Pattern

private lateinit var binding: ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Check valid email
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

        binding.txtSigup.setOnClickListener {
            val intent= Intent(this@LoginActivity,signup::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.txtForgotPassword.setOnClickListener {
            val intent=Intent(this@LoginActivity,ResetPassActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email: String = binding.etLanguage.text.toString()
            val password: String = binding.etGroupEightyOne.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@LoginActivity, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!(validator.validate(email))) {
                Toast.makeText(
                    this@LoginActivity,
                    "Wrong email format",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(
                    this@LoginActivity,
                    "Enter password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val progressDialog = ProgressDialog(this@LoginActivity)
            progressDialog.setTitle("Login")
            progressDialog.setMessage("Logging in...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        binding.name.text = intent.getStringExtra("name")
                        val username = binding.name.text.toString()
                        val intent = Intent(this@LoginActivity, HomepageActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra("name", username)
                        startActivity(intent)
                        finish()
                    } else {
                        val message = task.exception!!.toString()
                        Toast.makeText(this, "Password or Email Invalid", Toast.LENGTH_LONG).show()
                        mAuth.signOut()
                        progressDialog.dismiss()
                    }
                }
        }

    }

    override fun onStart() {
        super.onStart()

        if(FirebaseAuth.getInstance().currentUser!=null)
        {

            binding.name.text = intent.getStringExtra("name")
            val username = binding.name.text.toString()

            //forwarding to home page
            val intent=Intent(this@LoginActivity,HomepageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("name", username)
            startActivity(intent)
            finish()
        }
    }
}
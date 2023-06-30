package com.example.plantappication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.plantappication.databinding.ActivityMainBinding
import com.example.plantappication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import java.util.regex.Matcher
import java.util.regex.Pattern

private lateinit var binding: ActivitySignupBinding
class signup : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //khởi tạo viewbinding
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check valid email
        class EmailValidator {
            private var pattern: Pattern
            private var matcher: Matcher? = null
            private val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

            init {
                pattern = Pattern.compile(EMAIL_PATTERN)
            }

            fun validate(email: String): Boolean {
                matcher = pattern.matcher(email)
                return matcher?.matches() ?: false
            }
        }

        val validator = EmailValidator()

        // Check valid password
        class PasswordValidator {
            fun validateString(str: String): Boolean {
                val hasCorrectLenght = str.length >= 11
                val hasNumber = str.matches(".*\\d+.*".toRegex())
                val hasSpecialChar = str.matches(".*[!@#\$%^&*(),.?\":{}|<>]+.*".toRegex())
                val hasUppercase = str.matches(".*[A-Z]+.*".toRegex())
                val hasLowercase = str.matches(".*[a-z]+.*".toRegex())
                return hasCorrectLenght && hasNumber && hasSpecialChar && hasUppercase && hasLowercase
            }
        }
        val passValidtor = PasswordValidator()
        mAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.etLanguage.text.toString()
            val username1 = binding.etLanguage12.text.toString()
            val password = binding.etGroupEightyOne.text.toString()
            val password_check = binding.etGroupEightyOne1.text.toString()


            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@signup, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!validator.validate(email)) {
                Toast.makeText(this@signup, "Wrong email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@signup, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!passValidtor.validateString(password)) {
                Toast.makeText(
                    this@signup, "Your password must contain at least 11 character, with number, uppercase, lowercase and special characters!", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password_check)) {
                Toast.makeText(this@signup, "Confirm your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.toString() == password_check.toString()) {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        binding.progressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            val auth = FirebaseAuth.getInstance()
                            val user = auth.currentUser
                            user?.sendEmailVerification()?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    //
                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                        .setDisplayName(username1)
                                        .build()

                                    user?.updateProfile(profileUpdates)
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {


                                            } else {
                                                // Name update failed
                                            }
                                        }
                                    //
                                    Toast.makeText(this@signup, "Registered successfully. Please verify your email!",
                                        Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@signup, "Failed to send email verification!",
                                        Toast.LENGTH_SHORT).show()

                                }
                            }
                        } else {
                            Toast.makeText(this@signup, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                        val intent= Intent(this@signup,LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        //pass name's user
                        intent.putExtra("name", username1)
                        startActivity(intent)
                        finish()
                    }
            } else {
                Toast.makeText(this@signup, "Password don't match", Toast.LENGTH_SHORT).show()
            }

        }


    }
}
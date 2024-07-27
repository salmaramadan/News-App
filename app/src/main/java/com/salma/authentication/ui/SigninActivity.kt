package com.salma.authentication.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.salma.authentication.R

class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var signInBtn: Button
    private lateinit var emailTil: TextInputLayout
    private lateinit var emailEt: EditText
    private lateinit var passwordTil: TextInputLayout
    private lateinit var passwordEt: EditText
    private lateinit var registerTv: TextView
    private lateinit var noAccountTv: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)

        initComponent()

        signInBtn.setOnClickListener {
            if (validateEmail() && validatePassword()) {
                signInUser()
            }
        }

        registerTv.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

//        googleSignInBtn.setOnClickListener {
//            // TODO: Implement Google Sign-In
//        }

        // Check if user is already logged in
        if (isLoggedIn()) {
            navigateToHome()
        }
    }

    private fun signInUser() {
        val emailStr = emailEt.text.toString()
        val passwordStr = passwordEt.text.toString()

        auth.signInWithEmailAndPassword(emailStr, passwordStr)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()
            // Mark user as logged in
            setLoggedInStatus(true)
            navigateToHome()
        } else {
            // Show an error message or handle accordingly
        }
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun setLoggedInStatus(loggedIn: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", loggedIn)
            apply()
        }
    }

    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val value = emailEt.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Invalid email address"
        }
        emailTil.apply {
            isErrorEnabled = errorMessage != null
            error = errorMessage
        }
        return errorMessage == null
    }

    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val value = passwordEt.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password is required"
        } else if (value.length < 6) {
            errorMessage = "Password must be at least 6 characters long"
        }
        passwordTil.apply {
            isErrorEnabled = errorMessage != null
            error = errorMessage
        }
        return errorMessage == null
    }

    private fun initComponent() {
        emailTil = findViewById(R.id.email_til)
        emailEt = findViewById(R.id.email_et)
        passwordTil = findViewById(R.id.password_til)
        passwordEt = findViewById(R.id.password_et)
        signInBtn = findViewById(R.id.sign_in_btn)
        registerTv = findViewById(R.id.register_tv)
        noAccountTv = findViewById(R.id.text_dont_have_account)
    }

    override fun onClick(view: View?) {
        // Handle clicks if needed
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}

package com.salma.authentication.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.salma.authentication.R

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    private lateinit var register_btn: Button
    private lateinit var name_til: TextInputLayout
    private lateinit var name: EditText
    private lateinit var email_til: TextInputLayout
    private lateinit var email: EditText
    private lateinit var password_til: TextInputLayout
    private lateinit var password: EditText
    private lateinit var confirm_password: EditText
    private lateinit var confirm_password_til: TextInputLayout
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponent()
        auth = FirebaseAuth.getInstance()
        name.onFocusChangeListener = this
        email.onFocusChangeListener = this
        password.onFocusChangeListener = this
        confirm_password.onFocusChangeListener = this

        register_btn.setOnClickListener {
            if (validateFullName() && validateEmail() && validatePassword() && validateConfirmPassword() && validatePasswordAndConfirmPassword()) {
                registerUser()
            }
        }
    }

    private fun registerUser() {
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()
        auth.createUserWithEmailAndPassword(emailStr, passwordStr)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> "Weak Password"
                        is FirebaseAuthInvalidCredentialsException -> "Invalid Email"
                        is FirebaseAuthUserCollisionException -> "User already exists"
                        else -> task.exception?.message
                    }
                    Toast.makeText(this, "Authentication Failed: $errorMessage", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Show an error message
        }
    }

    private fun validatePasswordAndConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val passwordStr = password.text.toString()
        val confirmPasswordStr = confirm_password.text.toString()

        if (passwordStr != confirmPasswordStr) {
            errorMessage = "Confirm password doesn't match password"
        } else {
            errorMessage = null
        }

        confirm_password_til.apply {
            isErrorEnabled = errorMessage != null
            error = errorMessage
        }

        return errorMessage == null
    }

    private fun validateConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val value = confirm_password.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Confirm password is required"
        } else if (value.length < 6) {
            errorMessage = "Confirm password must be at least 6 characters long"
        }
        confirm_password_til.apply {
            isErrorEnabled = errorMessage != null
            error = errorMessage
        }
        return errorMessage == null
    }

    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val value = password.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password is required"
        } else if (value.length < 6) {
            errorMessage = "Password must be at least 6 characters long"
        }
        password_til.apply {
            isErrorEnabled = errorMessage != null
            error = errorMessage
        }
        return errorMessage == null
    }

    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val value = email.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Invalid email address"
        }
        email_til.apply {
            isErrorEnabled = errorMessage != null
            error = errorMessage
        }
        return errorMessage == null
    }

    private fun validateFullName(): Boolean {
        var errorMessage: String? = null
        val value = name.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Full name is required"
        }
        name_til.apply {
            isErrorEnabled = errorMessage != null
            error = errorMessage
        }
        return errorMessage == null
    }

    private fun initComponent() {
        name_til = findViewById(R.id.name_til)
        name = findViewById(R.id.name_et)
        email_til = findViewById(R.id.email_til)
        email = findViewById(R.id.email_et)
        password_til = findViewById(R.id.password_til)
        password = findViewById(R.id.password_et)
        confirm_password_til = findViewById(R.id.confirm_password_til)
        confirm_password = findViewById(R.id.confirm_password_et)
        register_btn = findViewById(R.id.register_btn)
    }

    override fun onClick(view: View?) {
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.name_et -> {
                    if (hasFocus) {
                        if (name_til.isErrorEnabled) {
                            name_til.isErrorEnabled = false
                        }
                    } else {
                        validateFullName()
                    }

                }

                R.id.email_et -> {
                    if (hasFocus) {
                        if (email_til.isErrorEnabled) {
                            email_til.isErrorEnabled = false
                        }
                    } else {
                        if (validateEmail()) {
                            //retrofit
                        }
                    }

                }

                R.id.password_et -> {
                    if (hasFocus) {
                        if (password_til.isErrorEnabled) {
                            password_til.isErrorEnabled = false
                        }
                    } else {
                        if (validatePassword() && confirm_password.text!!.isNotEmpty() && validateConfirmPassword() && validatePasswordAndConfirmPassword()) {
                            if (confirm_password_til.isErrorEnabled) {
                                confirm_password_til.isErrorEnabled = false
                            }
                            confirm_password_til.setStartIconDrawable(R.drawable.check_circle_outline_24)
                            confirm_password_til.setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                        }
                    }

                }

                R.id.confirm_password_et -> {
                    if (hasFocus) {
                        if (confirm_password_til.isErrorEnabled) {
                            confirm_password_til.isErrorEnabled = false
                        }
                    } else {
                        if (validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()) {
                            if (password_til.isErrorEnabled) {
                                password_til.isErrorEnabled = false
                            }
                            password_til.setStartIconDrawable(R.drawable.check_circle_outline_24)
                            password_til.setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                        }
                    }

                }
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        // Handle key events if needed
        return false
    }
}

package com.gamaliel.uts_anmp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gamaliel.uts_anmp.databinding.ActivityLoginBinding
import com.gamaliel.uts_anmp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inisialisasi viewmodel
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPass.text.toString()


            if (validateInputs(username, password)) {
                viewModel.performLogin(username, password)
            }
        }

        binding.txtRegis.setOnClickListener {
            navigateToRegis()
        }

        viewModel.loginSuccessLD.observe(this) { success ->
            if (success) {
                // Handle successful login
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            }
        }

        viewModel.loginErrorLD.observe(this) { error ->
            if (error) {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            binding.usernameInputLayout.error = "Username is required"
            isValid = false
        } else {
            binding.usernameInputLayout.error = null
        }

        if (password.isEmpty()) {
            binding.passInputLayout.error = "Password is required"
            isValid = false
        } else {
            binding.passInputLayout.error = null
        }

        return isValid
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegis() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}

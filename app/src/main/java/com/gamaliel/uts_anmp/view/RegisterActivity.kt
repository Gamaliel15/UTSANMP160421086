package com.gamaliel.uts_anmp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gamaliel.uts_anmp.databinding.ActivityRegisterBinding
import com.gamaliel.uts_anmp.viewmodel.RegisViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RegisViewModel::class.java)

        binding.btnRegis.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val firstName = binding.txtFirstName.text.toString()
            val lastName = binding.txtLastName.text.toString()
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPass.text.toString()
            val repeatPassword = binding.txtRePass.text.toString()

            if (validateInputs(username, firstName, lastName, email, password, repeatPassword)) {
                // Panggil fungsi untuk memeriksa apakah username sudah ada sebelumnya
                viewModel.checkUsernameExists(username)
            }
        }

        binding.txtLogin.setOnClickListener {
            navigateToLogin()
        }

        // Observer untuk LiveData pengecekan username
        viewModel.usernameExistsLD.observe(this) { usernameExists ->
            if (usernameExists) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
            } else {
                // Panggil fungsi registrasi jika username belum ada
                val username = binding.txtUsername.text.toString()
                val firstName = binding.txtFirstName.text.toString()
                val lastName = binding.txtLastName.text.toString()
                val email = binding.txtEmail.text.toString()
                val password = binding.txtPass.text.toString()
                viewModel.registerUser(username, firstName, lastName, email, password)
            }
        }

        viewModel.registrationSuccessLD.observe(this) { registrationSuccess ->
            if (registrationSuccess) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                // Handle registration failure, e.g., display error message
            }
        }
    }

    private fun validateInputs(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        repeatPassword: String
    ): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            binding.usernameInputLayout.error = "Username is required"
            isValid = false
        } else {
            binding.usernameInputLayout.error = null
        }

        if (firstName.isEmpty()) {
            binding.firtsNameInputLayout.error = "First Name is required"
            isValid = false
        } else {
            binding.firtsNameInputLayout.error = null
        }

        if (lastName.isEmpty()) {
            binding.lastNameInputLayout.error = "Last Name is required"
            isValid = false
        } else {
            binding.lastNameInputLayout.error = null
        }

        if (email.isEmpty()) {
            binding.emailInputLayout.error = "Email is required"
            isValid = false
        } else {
            binding.emailInputLayout.error = null
        }

        if (password.isEmpty()) {
            binding.passInputLayout.error = "Password is required"
            isValid = false
        } else {
            binding.passInputLayout.error = null
        }

        if (password != repeatPassword) {
            binding.repassInputLayout.error = "Passwords do not match"
            isValid = false
        } else {
            binding.repassInputLayout.error = null
        }

        return isValid
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Optional: Close the RegisterActivity
    }
}

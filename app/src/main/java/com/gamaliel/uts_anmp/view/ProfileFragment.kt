package com.gamaliel.uts_anmp.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gamaliel.uts_anmp.databinding.FragmentProfileBinding
import com.gamaliel.uts_anmp.model.User
import com.gamaliel.uts_anmp.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        // Fetch current user profile
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPreferences.getString("idUsers", "")
        userId?.let { userId ->
            viewModel.fetchCurrentUser(userId)
        }

        viewModel.currentUser.observe(viewLifecycleOwner, Observer<User> { user ->
            user?.let {
                displayUserProfile(it)
            }
        })

        binding.btnUpdate.setOnClickListener {
            updateProfile()
        }

        binding.btnSignOut.setOnClickListener {
            logout()
        }
    }

    private fun displayUserProfile(user: User) {
        binding.txtUsername.text = user.username?.toEditable()
        binding.txtFirstName.text = user.firstname?.toEditable()
        binding.txtLastName.text = user.lastname?.toEditable()
        binding.txtEmail.text = user.email?.toEditable()
    }

    // Extension function to convert String to Editable
    fun String?.toEditable(): Editable? {
        return this?.let { Editable.Factory.getInstance().newEditable(this) }
    }

    private fun updateProfile() {
        val firstName = binding.txtFirstName.text.toString()
        val lastName = binding.txtLastName.text.toString()
        val newPassword = binding.txtPass.text.toString()

        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPreferences.getString("idUsers", "")

        userId?.let { userId ->
            viewModel.updateProfile(userId, firstName, lastName, newPassword)
        }

        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        // Clear SharedPreferences on logout
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }

        // Navigate to LoginFragment or LoginActivity
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
}

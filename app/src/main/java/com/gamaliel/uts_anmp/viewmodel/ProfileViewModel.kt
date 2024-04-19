package com.gamaliel.uts_anmp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.gamaliel.uts_anmp.model.User
import org.json.JSONObject

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    val currentUser = MutableLiveData<User>()

    fun fetchCurrentUser(userId: String) {
        val queue = Volley.newRequestQueue(getApplication())

        val url = "http://10.0.2.2/anmp/login.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getString("result") == "SUCCESS") {
                        val userData = obj.getJSONObject("user")
                        val user = User(
                            userData.getString("idusers"),
                            userData.getString("username"),
                            userData.getString("firstname"),
                            userData.getString("lastname"),
                            userData.getString("email"),
                            ""
                        )
                        currentUser.value = user
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            }) {

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idusers"] = userId
                return params
            }
        }

        queue.add(stringRequest)
    }

    fun updateProfile(userId: String, firstName: String, lastName: String, newPassword: String) {
        val queue = Volley.newRequestQueue(getApplication())

        val url = "http://10.0.2.2/anmp/update_profile.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getString("result") == "SUCCESS") {
                        // Handle successful profile update (optional)
                        currentUser.value = User(userId, "", firstName, lastName, "", "")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            }) {

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idusers"] = userId
                params["firstname"] = firstName
                params["lastname"] = lastName
                params["new_password"] = newPassword
                return params
            }
        }

        queue.add(stringRequest)
    }
}

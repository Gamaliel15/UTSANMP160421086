package com.gamaliel.uts_anmp.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.gamaliel.uts_anmp.model.User
import org.json.JSONObject

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val loginSuccessLD = MutableLiveData<Boolean>()
    val loginErrorLD = MutableLiveData<Boolean>()
    val loggedInUserLD = MutableLiveData<User>()

    private val TAG = "LoginViewModel"
    private val sharedPreferences = application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun authenticateUser(username: String, password: String) {
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
                        loggedInUserLD.value = user
                        loginSuccessLD.value = true

                        // Simpan data pengguna ke SharedPreferences
                        with(sharedPreferences.edit()) {
                            putString("idUsers", user.idusers)
                            putString("username", user.username)
                            putString("firstname", user.firstname)
                            putString("lastname", user.lastname)
                            putString("email", user.email)
                            apply()
                        }
                    } else {
                        loginErrorLD.value = true
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error: $e")
                    loginErrorLD.value = true
                }
            },
            { error ->
                Log.e(TAG, "Volley error: $error")
                loginErrorLD.value = true
            }) {

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }

        queue.add(stringRequest)
    }
}

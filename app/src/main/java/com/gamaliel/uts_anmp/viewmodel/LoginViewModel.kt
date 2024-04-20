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

// ViewModel untuk proses login
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val loginSuccessLD = MutableLiveData<Boolean>()//LiveData kalo berhasil login
    val loginErrorLD = MutableLiveData<Boolean>()
    val loggedInUserLD = MutableLiveData<User>() // LiveData untuk simpan data user yang berhasil login

    //buat log
    private val TAG = "LoginViewModel"

    // untuk simpan data user yg login
    private val sharedPreferences = application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    // Fungsi untuk melakukan autentikasi pengguna
    fun performLogin(username: String, password: String) {
        val queue = Volley.newRequestQueue(getApplication())

        //val url = "http://10.0.2.2/anmp/login.php"

        val url = "http://192.168.100.25/anmp/login.php"

        // StringRequest untuk melakukan request HTTP POST
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                try {
                    // Mengolah response JSON
                    val obj = JSONObject(response)

                    // Jika hasil autentikasi berhasil
                    if (obj.getString("result") == "SUCCESS") {
                        val userData = obj.getJSONObject("user")

                        // Mendapatkan data user dari response
                        val user = User(
                            userData.getString("idusers"),
                            userData.getString("username"),
                            userData.getString("firstname"),
                            userData.getString("lastname"),
                            userData.getString("email"),
                            ""
                        )

                        // Memasukkan data user ke LiveData
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
                        // Jika hasil autentikasi gagal
                        loginErrorLD.value = true
                    }
                } catch (e: Exception) {
                    // Handle exception jika terjadi error
                    Log.e(TAG, "Error: $e")
                    loginErrorLD.value = true
                }
            },
            { error ->
                // Handle error jika terjadi error pada Volley
                Log.e(TAG, "Volley error: $error")
                loginErrorLD.value = true
            }) {

            // Fungsi untuk menyediakan parameter POST
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }

        // Menambahkan request ke dalam queue Volley
        queue.add(stringRequest)
    }
}

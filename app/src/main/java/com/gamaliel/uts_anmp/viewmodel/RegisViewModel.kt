package com.gamaliel.uts_anmp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class RegisViewModel(application: Application) : AndroidViewModel(application) {
    val loadingLD = MutableLiveData<Boolean>()
    val registrationSuccessLD = MutableLiveData<Boolean>()
    val usernameExistsLD = MutableLiveData<Boolean>() // LiveData untuk menunjukkan apakah username sudah ada

    private val TAG = "RegisterViewModel"
    private var queue: RequestQueue? = null

    init {
        queue = Volley.newRequestQueue(application)
    }

    fun checkUsernameExists(username: String) {

        //val url = "http://10.0.2.2/anmp/check_username.php?username=$username"

        val url = "http://192.168.100.25/anmp/check_username.php?username=$username"

        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                usernameExistsLD.value = response.toBoolean()
            },
            { error ->
                usernameExistsLD.value = false
            })

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    fun registerUser(username: String, firstName: String, lastName: String, email: String, password: String) {
        loadingLD.value = true

        queue?.let { queue ->

            val url = "http://10.0.2.2/anmp/regis.php"

            //val url = "http://192.168.100.25/anmp/regis.php"

            val stringRequest = object : StringRequest(Request.Method.POST, url,
                { response ->
                    registrationSuccessLD.value = true
                    loadingLD.value = false
                },
                { error ->
                    registrationSuccessLD.value = false
                    loadingLD.value = false
                }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["username"] = username
                    params["firstname"] = firstName
                    params["lastname"] = lastName
                    params["email"] = email
                    params["password"] = password
                    return params
                }
            }

            stringRequest.tag = TAG
            queue.add(stringRequest)
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}

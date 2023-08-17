package com.example.volleylogin.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.volleylogin.RequestQueueSingleton
import com.example.volleylogin.api.Api
import com.example.volleylogin.model.Product
import org.json.JSONArray
import org.json.JSONObject

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val _application = application

    var userTokenResponse: MutableLiveData<String> = MutableLiveData()
    var userInfoResponse: MutableLiveData<MutableList<String>> = MutableLiveData()
    var productListResponse: MutableLiveData<MutableList<Product>> = MutableLiveData()

    fun getAuth(email: String, password: String) {
        // Instantiate the RequestQueue.
        val loginUrl = Api.auth_url + "?email=$email&password=$password"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.POST, loginUrl, { response ->
                // request successfully
                val responseObject = JSONObject(response)
                if (responseObject.has("access_token")) {
                    Log.d("LoginCheck", responseObject.toString())

                    val token = responseObject.getString("access_token")
                    userTokenResponse.value = token

                    getLogin(token, email, password)
                }
            },
            {
                // request failed
                it.localizedMessage?.let { it1 -> Log.d("AuthRequestFailed", it1) }
            }
        )
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(getApplication()).addToRequestQueue(stringRequest)
    }

    fun getLogin(token: String, email: String, password: String) {

        // Request a string response from the provided URL.
        val userInfo = mutableListOf<String>()

        val stringRequest = StringRequest(
            Request.Method.GET, Api.user_url, { response ->
                // request successfully
                val userJsonArray = JSONArray(response)

                Log.d("UserCheck", userJsonArray.getJSONObject(0).toString())

                for (i in 0 until userJsonArray.length()) {
                    val userDataObject = userJsonArray.getJSONObject(i)

                    val userEmail = userDataObject.getString("email")
                    val userPassword = userDataObject.getString("password")

                    Log.d("user email", "userEmail : $userEmail email: $email")

                    if (userEmail.equals(email) && userPassword.equals(password)) {

                        val userId = userDataObject.getInt("id").toString()
                        val userEmail = userDataObject.getString("email")
                        val userPassword = userDataObject.getString("password")
                        val userName = userDataObject.getString("name")
                        val userRole = userDataObject.getString("role")
                        val userAvatar = userDataObject.getString("avatar")

                        userInfo.add(userId)
                        userInfo.add(userEmail)
                        userInfo.add(userPassword)
                        userInfo.add(userName)
                        userInfo.add(userRole)
                        userInfo.add(userAvatar)

                        userInfoResponse.value = userInfo
                    }
                }
            },
            {
                // request failed
                it.localizedMessage?.let { it1 -> Log.d("loginRequestFailed", it1) }
            }
        )
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(getApplication()).addToRequestQueue(stringRequest)
    }

    fun getProduct() {
        // Request a string response from the provided URL.
        val productArrayList = mutableListOf<Product>()

        val stringRequest = StringRequest(
            Request.Method.GET, Api.product_url, { response ->
                // request successfully
                val userJsonArray = JSONArray(response)

                Log.d("ProductCheck", userJsonArray.getJSONObject(0).toString())

                for (i in 0 until userJsonArray.length()) {
                    val userDataObject = userJsonArray.getJSONObject(i)
                    Log.d("success request", userDataObject.toString())
                    val productData = Product(
                        userDataObject.getInt("id"),
                        userDataObject.getString("title"),
                        userDataObject.getDouble("price"),
                        userDataObject.getString("description"),
                        userDataObject.getJSONArray("images")[0].toString()
                    )
                    Log.d("success request image", userDataObject.getJSONArray("images")[0].toString())
                    productArrayList.add(productData)
                }
                productListResponse.value = productArrayList
            },
            {
                // request failed
                it.localizedMessage?.let { it1 -> Log.d("loginRequestFailed", it1) }
            }
        )
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(getApplication()).addToRequestQueue(stringRequest)
    }
}
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

    var productListResponse: MutableLiveData<List<Product>> = MutableLiveData()

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
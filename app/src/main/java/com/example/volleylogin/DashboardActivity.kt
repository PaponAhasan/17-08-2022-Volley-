package com.example.volleylogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.volleylogin.adapter.ProductAdapter
import com.example.volleylogin.api.Api
import com.example.volleylogin.databinding.ActivityDashboardBinding
import com.example.volleylogin.model.Product
import com.example.volleylogin.viewmodel.ProductViewModel
import org.json.JSONArray

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialization()

        viewModel.getProduct()
        viewModel.productListResponse.observe(this){
            val productArrayList = it

            val productAdapter = ProductAdapter(this, productArrayList)

            //productArrayList.clear()
            productAdapter.notifyDataSetChanged()

            binding.pbProduct.visibility = View.INVISIBLE
            binding.rvProduct.adapter = productAdapter
            binding.rvProduct.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun initialization() {
        binding.pbProduct.visibility = View.VISIBLE
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
    }
}
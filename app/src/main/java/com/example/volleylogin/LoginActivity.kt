package com.example.volleylogin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.volleylogin.api.Api
import com.example.volleylogin.databinding.ActivityLoginBinding
import com.example.volleylogin.viewmodel.ProductViewModel
import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var token: String? = null

    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialization()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            binding.progressBar.visibility = View.VISIBLE

            //call for user auth
            viewModel.getAuth(email, password)
            viewModel.userTokenResponse.observe(this) {
                val token = it
                viewModel.getLogin(token, email, password)
                viewModel.userInfoResponse.observe(this) { user ->

                    val userId = user[0]
                    val userEmail = user[1]
                    val userPassword = user[2]
                    val userName = user[3]
                    val userRole = user[4]
                    val avatar = user[5]

                    if (userEmail == email && userPassword == password) {

                        val sharedPref =
                            this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()

                        editor.apply {
                            editor.putString(Api.TOKEN, token)
                            putString(Api.ID, userId)
                            putString(Api.EMAIL, userEmail)
                            putString(Api.PASSWORD, userPassword)
                            putString(Api.NAME, userName)
                            putString(Api.ROLE, userRole)
                            putString(Api.AVATAR, avatar)
                            apply()
                        }
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Invalid User", Toast.LENGTH_LONG).show()
                    }
                }
            }
            //getAuth(email, password)
        }

        binding.progressBar.visibility = View.GONE
    }

    private fun initialization() {
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        binding.progressBar.visibility = View.GONE
    }

    override fun onPause() {
        binding.progressBar.visibility = View.GONE
        super.onPause()
    }
}
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
import com.example.volleylogin.viewmodel.UserViewModel
import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var token: String? = null

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialization()

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if(email.isEmpty() or password.isEmpty()){
                Toast.makeText(this, "needed email or password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            showProgress()
            binding.btnLogin.isEnabled = false
            //call for user auth
            viewModel.getAuth(email, password)
            viewModel.userTokenResponse.observe(this) {
                val token = it

                if(token.isEmpty()) {
                    hideProgress()
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this, "Invalid User", Toast.LENGTH_LONG).show()
                    return@observe
                }

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
                            putString(Api.TOKEN, token)
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
                        hideProgress()
                        binding.btnLogin.isEnabled = true
                        Toast.makeText(this, "Invalid User", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun initialization() {
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.INVISIBLE
    }
}
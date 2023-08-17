package com.example.volleylogin.api

object Api {
    const val TOKEN = "token"
    const val ID = "id"
    const val EMAIL = "email"
    const val PASSWORD = "password"
    const val NAME = "name"
    const val ROLE = "role"
    const val AVATAR = "avatar"

    private const val BASE_URl = "https://api.escuelajs.co/api/v1"

    const val auth_url = "$BASE_URl/auth/login"
    const val user_url = "$BASE_URl/users"
    const val product_url = "$BASE_URl/products"
}
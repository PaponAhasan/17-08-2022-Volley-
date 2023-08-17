package com.example.volleylogin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.volleylogin.R
import com.example.volleylogin.model.Product

class ProductAdapter(private val context : Context, private val products : List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val productIV = itemView.findViewById<ImageView>(R.id.iv_product)
        val titleTV = itemView.findViewById<TextView>(R.id.tv_title)
        val priceTV = itemView.findViewById<TextView>(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val searchItem = products[position]

        holder.titleTV.text = searchItem.title
        holder.priceTV.text = searchItem.price.toString()

        Glide.with(context)
            .load(searchItem.images)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.productIV)
    }

}
package com.example.house.Recyler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.house.DataModel.ModelClass
import com.example.house.R


class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var dataList = emptyList<ModelClass>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.findViewById<ImageView>(R.id.product_description_image_List)
        private val descriptionTextView = itemView.findViewById<TextView>(R.id.product_description_List)
        private val contactNumberTextView = itemView.findViewById<TextView>(R.id.contactNumberList)
        private val priceTextView = itemView.findViewById<TextView>(R.id.thePriceOfProductList)
        private val locationTextView = itemView.findViewById<TextView>(R.id.location_List)

        fun bind(data: ModelClass) {
            // Bind data to the itemView
            // Load the image using Glide
            Glide.with(itemView.context)
                .load(data.Image) // Assuming Image is the URL
                .into(imageView)
          //  imageView.setImageResource(data.Image.toInt())
            descriptionTextView.text = data.Description
            contactNumberTextView.text = data.Contact
            priceTextView.text = data.price
            locationTextView.text = data.location
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

//    fun setData(newDataList: List<ModelClass?>) {
//        dataList = newDataList
//        notifyDataSetChanged()
//    }
@SuppressLint("NotifyDataSetChanged")
fun setData(newDataList: List<ModelClass?>) {
    dataList = newDataList.filterNotNull()
    notifyDataSetChanged()
}

}




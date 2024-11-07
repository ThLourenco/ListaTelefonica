package com.example.applistatelefonica

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val image: ImageView = view.findViewById(R.id.img_contact)
    val textName: TextView = view.findViewById(R.id.text_contact_name)

}
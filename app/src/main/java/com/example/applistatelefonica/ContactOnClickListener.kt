package com.example.applistatelefonica

import com.example.applistatelefonica.model.ContactModel

class ContactOnClickListener(val clickListener: (contact: ContactModel) -> Unit) {
    fun onClick (contact: ContactModel) = clickListener
}
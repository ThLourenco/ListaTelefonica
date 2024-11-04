package com.example.applistatelefonica.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.applistatelefonica.R
import com.example.applistatelefonica.database.DBHelper
import com.example.applistatelefonica.databinding.ActivityNewContatctBinding

class NewContatctActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewContatctBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewContatctBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(applicationContext)

        binding.btnSave.setOnClickListener{

            val name = binding.editName.text.toString()
            val address = binding.editAddress.text.toString()
            val email = binding.editEmail.text.toString()
            val phone = binding.editPhone.text.toString().toInt()
            val imageId = 1

            if(name.isNotEmpty() && address.isNotEmpty() && email.isNotEmpty()){

                val res = db.insertContact(name,address,email, phone, imageId)
                if(res>0){
                    Toast.makeText(applicationContext,"Insert ok",Toast.LENGTH_LONG).show()
                    finish()

                }else{
                    Toast.makeText(applicationContext,"Insert Erro",Toast.LENGTH_LONG).show()
                }
            }

        }
        binding.btrCancel.setOnClickListener { finish() }

    }
    }
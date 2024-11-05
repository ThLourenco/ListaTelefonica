package com.example.applistatelefonica.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.applistatelefonica.R
import com.example.applistatelefonica.databinding.ActivityContactImageSelectionBinding

class ContactImageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactImageSelectionBinding
    private lateinit var i: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactImageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        i = intent

        binding.imgProfile1.setOnClickListener{sendId(R.drawable.jovem_female)}
        binding.imgProfile2.setOnClickListener{sendId(R.drawable.jovem_masculino)}
        binding.imgProfile3.setOnClickListener{sendId(R.drawable.moca)}
        binding.imgProfile4.setOnClickListener{sendId(R.drawable.moco)}
        binding.imgProfile5.setOnClickListener{sendId(R.drawable.senhor)}
        binding.imgProfile6.setOnClickListener{sendId(R.drawable.senhora)}
        binding.btnRemoveImage.setOnClickListener{sendId(R.drawable.default_avatar)}

    }

    private fun sendId(id:Int){
        i.putExtra("id",id)
        setResult(1,i)
        finish()
    }
}
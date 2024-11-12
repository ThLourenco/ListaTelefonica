package com.example.applistatelefonica.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.applistatelefonica.R
import com.example.applistatelefonica.database.DBHelper
import com.example.applistatelefonica.databinding.ActivityNewContatctBinding

class NewContatctActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewContatctBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var id: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewContatctBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(applicationContext)
        val i = intent

        //CADASTRANDO UM CONTATO NO BANCO DE DADOS CONTATOS
        binding.imgContact.setOnClickListener{
            launcher.launch(Intent(applicationContext, ContactImageSelectionActivity::class.java))

        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if(it.data !=null && it.resultCode == 1){
                     id = it.data?.extras?.getInt("id")
                    binding.imgContact.setImageResource(id!!)
                }else{
                     id = -1
                    binding.imgContact.setImageResource(R.drawable.default_avatar)
                }
        }

        binding.btnSave.setOnClickListener{

            val name = binding.editName.text.toString()
            val address = binding.editAddress.text.toString()
            val email = binding.editEmail.text.toString()
            val phone = binding.editPhone.text.toString().toInt()
            var imageId = -1
            if(id!=null) {
                 imageId = id as Int
            }
            if(name.isNotEmpty() && address.isNotEmpty() && email.isNotEmpty()){

                val res = db.insertContact(name,address,email, phone, imageId)
                if(res>0){
                    Toast.makeText(applicationContext,"Insert ok",Toast.LENGTH_LONG).show()
                    setResult(1, i)
                    finish()

                }else{
                    Toast.makeText(applicationContext,"Insert Erro",Toast.LENGTH_LONG).show()
                }
            }

        }
        binding.btrCancel.setOnClickListener {
            setResult(0,i)
            finish() }

    }
    }
package com.example.applistatelefonica.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.applistatelefonica.R
import com.example.applistatelefonica.database.DBHelper
import com.example.applistatelefonica.databinding.ActivitySingUpBinding

class SingUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)


        //ESTOU CRIANDO UMA NOVO USUARIO E O CADASTRANDO NO BANCO DE DADOS USERS

        binding.btnSingUp.setOnClickListener {
            val userName = binding.edtUserName.text.toString()
            val passWord = binding.edtPassWord.text.toString()
            val passwordConfirm = binding.edtConfirmPassWord.text.toString()

            if (userName.isNotEmpty() && passWord.isNotEmpty() && passwordConfirm.isNotEmpty()) {

                if (passWord == passwordConfirm){
                    val res = db.insertUser(userName, passWord)
                    if (res > 0) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.sing_up), Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "erro no res", Toast.LENGTH_LONG
                        ).show()
                        binding.edtUserName.setText("")
                        binding.edtPassWord.setText("")
                        binding.edtConfirmPassWord.setText("")
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.password_dont_t_match), Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_insert_all_required_fields), Toast.LENGTH_LONG
                ).show()
                binding.edtUserName.setText("")
                binding.edtPassWord.setText("")
                binding.edtConfirmPassWord.setText("")

            }
        }


    }
}
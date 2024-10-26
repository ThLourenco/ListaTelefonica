package com.example.applistatelefonica.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.applistatelefonica.R
import com.example.applistatelefonica.database.DBHelper
import com.example.applistatelefonica.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

        private lateinit var binding: ActivityLoginBinding
        private lateinit var sharedPreferences: SharedPreferences
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
            binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

            val db = DBHelper(this)
            sharedPreferences = application.getSharedPreferences("login", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", "")

            if (username != null) {

                    binding.edtUserName.setText(username)

            }

            binding.btnLogin.setOnClickListener {

                val userName = binding.edtUserName.text.toString()
                val passWord = binding.edtPassWord.text.toString()
                val logged = binding.ckbLogged.isChecked

                if (userName.isNotEmpty() && passWord.isNotEmpty()) {
                    if (db.login(userName, passWord)) {
                        if (logged) {
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putString("username", userName)
                            editor.apply()
                        }
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.login_erro), Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.please_insert_all_required_fields,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            binding.txtSingUp.setOnClickListener {
                val intent = Intent(this, SingUpActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
}
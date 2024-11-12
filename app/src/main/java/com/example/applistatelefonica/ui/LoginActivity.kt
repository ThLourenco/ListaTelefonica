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
            val getUsername = sharedPreferences.getString("username", "")

            val logged = binding.ckbLogged.isChecked
            if(logged) {
                binding.edtUserName.setText(getUsername)
            }


            binding.btnLogin.setOnClickListener {

                //estou inserindo o valor referente ao editText
                val userName = binding.edtUserName.text.toString()
                val passWord = binding.edtPassWord.text.toString()

                //verificando se os valores nao estao vazios
                if (userName.isNotEmpty() && passWord.isNotEmpty()) {
                    // vamos puxar a funçao de login para checar os dados dentro do banco de dados.
                    if (db.login(userName, passWord)) {
                        //se tudo ocorrer bem vamos salvar em charedpreferences o login caso o chebox esteja ativado
                        if (logged) {
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putString("username", userName)
                            editor.apply()
                        }

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()

                    } else { // caso o login nao esteja correto aparece a mensagem de erro
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.login_erro), Toast.LENGTH_LONG
                        ).show()
                    }
                } else { //caso os dados nao foram preenchido aparece a mensagem de texto
                    Toast.makeText(
                        applicationContext,
                        R.string.please_insert_all_required_fields,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            //aos clicar no texto vamos para outra pagina
            binding.txtSingUp.setOnClickListener {
                val intent = Intent(this, SingUpActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
}
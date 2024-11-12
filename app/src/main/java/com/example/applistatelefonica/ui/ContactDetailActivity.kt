package com.example.applistatelefonica.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.applistatelefonica.R
import com.example.applistatelefonica.database.DBHelper
import com.example.applistatelefonica.databinding.ActivityContactDetailBinding
import com.example.applistatelefonica.model.ContactModel

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailBinding
    private lateinit var db: DBHelper
    private var contactModel = ContactModel()

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var imageId: Int = -1
    private val REQUEST_PHONE_CALL = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(applicationContext) //instanciando o dbHelper (parte do banco de dados)

        val i = intent
        val id = i.extras?.getInt("id") //O operador ?. permite que você acesse os extras de forma segura. Se o extra não existir, id será null.

        if (id != null) { //caso o id não seja null, vamos buscar o metodo getContact e retornar os valores sobre o id (dados armazenados no banco de dados)
            contactModel = db.getContact(id)
            populate()

        } else {
            finish() //o código encerra a Activity com finish(), pois não faz sentido continuar sem o id.
        }

        //update
        binding.btnSave.setOnClickListener {
            val res = db.updateContact(
                id = contactModel.id,
                name = binding.editName.text.toString(),
                address = binding.editAddress.text.toString(),
                email = binding.editEmail.text.toString(),
                phone = binding.editPhone.text.toString().toInt(),
                imageId = imageId
            )

            if (res > 0) { //estou checando se o update foi realizado
                Toast.makeText(applicationContext, "Update OK", Toast.LENGTH_LONG).show()
                setResult(1, i)// setResult esta anexado ao launcher
                finish()
            } else {
                Toast.makeText(applicationContext, "Update Fail", Toast.LENGTH_LONG).show()
                setResult(0, i)// setResult esta anexado ao launcher
                finish()
            }

        }

        //deletando
        binding.btnDelete.setOnClickListener {

            val res = db.deleteContact(contactModel.id)

            if (res > 0) { // estou sempre checando se a funçao esta sendo executado corretamente
                Toast.makeText(applicationContext, "Delete OK", Toast.LENGTH_LONG).show()
                setResult(1, i) // setResult esta anexado ao launcher
                finish()
            } else {
                Toast.makeText(applicationContext, "Delete Fail", Toast.LENGTH_LONG).show()
                setResult(0, i) // setResult esta anexado ao launcher
                finish()
            }


        }

        // ------------- layout xml com a parte de btn editar e btn back
        //sao dois linear layout que ficam visivel ao apertar determinado botao
        binding.btnCancel.setOnClickListener {
            binding.layoutEdit.visibility = View.GONE
            binding.layoutEditDelete.visibility = View.VISIBLE
            populate()
            changeEditStatus(false)
        }

        binding.btnEdit.setOnClickListener {
            binding.layoutEdit.visibility = View.VISIBLE
            binding.layoutEditDelete.visibility = View.GONE
            changeEditStatus(true)
        }


        //AÇAO AO CLICAR NA FOTO PARA MUDAR A IMAGEM REFERENTE AO CONTATO

        binding.imgContact.setOnClickListener {
            if (binding.editName.isEnabled) {
                launcher.launch(
                    Intent(
                        applicationContext,
                        ContactImageSelectionActivity::class.java
                    )
                )
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            // 1 indica sucesso e 0 indica cancelamento
            if (it.data != null && it.resultCode == 1) {
                if (it.data?.extras != null) {
                    imageId = it.data?.getIntExtra("id", 0)!!
                    binding.imgContact.setImageResource((imageId!!)) // (!!) são um operador chamado "not-null assertion"
                    //estou garantindo que a variavel nao é nula
                }

            } else {
                imageId = -1
                binding.imgContact.setImageResource(R.drawable.default_avatar)
            }
        }

        // AÇOES DOS ICONES ENCONTRADOS AO LADO DO EDIT TEXT

        binding.iconEMail.setOnClickListener {

            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "text/plaun"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(contactModel.email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, arrayOf("Enviar para " + contactModel.name))
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Sent by Lista Telefonica APP")

            try {
                startActivity(Intent.createChooser(emailIntent, "Chosse Email Client"))
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

        }


        binding.iconPhone.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_PHONE_CALL
                )

            } else {
                val dialIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactModel.phone))
                startActivity(dialIntent)
            }
        }


    }


    private fun changeEditStatus(status: Boolean) {

        binding.editName.isEnabled = status
        binding.editAddress.isEnabled = status
        binding.editPhone.isEnabled = status
        binding.editEmail.isEnabled = status


    }

    private fun populate() {

        binding.editName.setText(contactModel.name)
        binding.editAddress.setText(contactModel.address)
        binding.editEmail.setText(contactModel.email)
        binding.editPhone.setText(contactModel.phone.toString())

        if (contactModel.imageId > 0) {
            binding.imgContact.setImageResource(contactModel.imageId)
        } else {
            binding.imgContact.setImageResource(R.drawable.default_avatar)
        }
    }
}
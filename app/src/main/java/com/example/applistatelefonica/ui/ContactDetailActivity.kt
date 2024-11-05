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
    private var imageId: Int? = -1
    private val REQUEST_PHONE_CALL = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        val id = i.extras?.getInt("id")
        db = DBHelper(applicationContext)

        if(id != null){
            contactModel =  db.getContact(id)
            populate()

        }else {
            finish()
        }
            binding.btnBack.setOnClickListener{
                setResult(0,i)
                finish()
            }
            binding.btnSave.setOnClickListener {
                val res = db.updateContact(
                    id = contactModel.id,
                    name = binding.editName.text.toString(),
                    address = binding.editAddress.text.toString(),
                    email = binding.editEmail.text.toString(),
                    phone = binding.editPhone.text.toString().toInt(),
                    imageId = contactModel.imageId
                )

                if (res > 0) {
                    Toast.makeText(applicationContext, "Update OK", Toast.LENGTH_LONG).show()
                    setResult(1, i)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Update Fail", Toast.LENGTH_LONG).show()
                    setResult(0, i)
                    finish()
                }

            }
            binding.btnCancel.setOnClickListener {
                binding.layoutEdit.visibility = View.GONE
                binding.layoutEditDelete.visibility = View.VISIBLE
                populate()
                changeEditStatus(false)
            }

            binding.btnDelete.setOnClickListener {

                val res = db.deleteContact(contactModel.id)
                if (res > 0) {
                    Toast.makeText(applicationContext, "Delete OK", Toast.LENGTH_LONG).show()
                    setResult(1, i)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Delete Fail", Toast.LENGTH_LONG).show()
                    setResult(0, i)
                    finish()
                }


            }

            binding.btnEdit.setOnClickListener {
                binding.layoutEdit.visibility = View.VISIBLE
                binding.layoutEditDelete.visibility = View.GONE
                changeEditStatus(true)
            }

            binding.imgContact.setOnClickListener {
                launcher.launch(
                    Intent(
                        applicationContext,
                        ContactImageSelectionActivity::class.java
                    )
                )

            }

            launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.data != null && it.resultCode == 1) {
                    imageId = it.data?.extras?.getInt("id")
                    binding.imgContact.setImageDrawable(resources.getDrawable(imageId!!))
                } else {
                    imageId = -1
                    binding.imgContact.setImageResource(R.drawable.default_avatar)
                }
            }

            binding.iconEMail.setOnClickListener{

                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.type = "text/plaun"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(contactModel.email))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, arrayOf("Enviar para "+contactModel.name))
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Sent by Lista Telefonica APP")

                try {
                    startActivity(Intent.createChooser(emailIntent,"Chosse Email Client"))
                }catch (e: Exception){
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                }

            }


            binding.iconPhone.setOnClickListener{
                if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)

                }else{
                    val dialIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:"+contactModel.phone))
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

        if(contactModel.imageId > 0) {
            binding.imgContact.setImageDrawable(resources.getDrawable(contactModel.imageId))
        }else{
            binding.imgContact.setImageResource(R.drawable.default_avatar)
        }
    }
}
package com.example.applistatelefonica.ui
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.applistatelefonica.database.DBHelper
import com.example.applistatelefonica.databinding.ActivityMainBinding
import com.example.applistatelefonica.model.ContactModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactlist: ArrayList<ContactModel>
    private lateinit var adapter: ArrayAdapter<ContactModel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this)

       contactlist =  dbHelper.getAllContact()

        adapter = ArrayAdapter<ContactModel>(applicationContext, android.R.layout.simple_gallery_item, contactlist)
        binding.listViewContact.adapter = adapter
    }
}
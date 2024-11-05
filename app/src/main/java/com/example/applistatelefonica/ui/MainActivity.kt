package com.example.applistatelefonica.ui
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.applistatelefonica.database.DBHelper
import com.example.applistatelefonica.databinding.ActivityMainBinding
import com.example.applistatelefonica.model.ContactModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactlist: ArrayList<ContactModel>
    private lateinit var adapter: ArrayAdapter<ContactModel>
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        loadList()

        binding.listViewContact.setOnItemClickListener { _, _, position, _ ->
            val i = Intent(this, ContactDetailActivity::class.java)
            i.putExtra("id", contactlist[position].id)
            result.launch(i)
        }

         binding.floatBtnAdd.setOnClickListener{
             result.launch(Intent(applicationContext, NewContatctActivity::class.java))
         }

        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.data!= null && it.resultCode==1){
                loadList()
            }else if(it.data!= null && it.resultCode==0){
                Toast.makeText(applicationContext,"Operation Canceled", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadList() {

        contactlist =  dbHelper.getAllContact()
        adapter = ArrayAdapter<ContactModel>(applicationContext, android.R.layout.simple_gallery_item, contactlist)
        binding.listViewContact.adapter = adapter
    }
}
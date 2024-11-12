package com.example.applistatelefonica.ui
import android.content.Intent
import android.os.Bundle

import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.applistatelefonica.adapter.ContactListAdapter
import com.example.applistatelefonica.adapter.ContactOnClickListener
import com.example.applistatelefonica.R
import com.example.applistatelefonica.database.DBHelper
import com.example.applistatelefonica.databinding.ActivityMainBinding
import com.example.applistatelefonica.model.ContactModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactlist: List<ContactModel>
    //private lateinit var adapter: ArrayAdapter<ContactModel> lista
    private lateinit var adaptter: ContactListAdapter // recyclerview
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var dbHelper: DBHelper
    private var ascDesc: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        binding.recyclerContacts.layoutManager = LinearLayoutManager(applicationContext)
        loadList()

        //quando estava usando uma list e nao um recyclerView
       /* binding.listViewContact.setOnItemClickListener { _, _, position, _ ->
            val i = Intent(this, ContactDetailActivity::class.java)
            i.putExtra("id", contactlist[position].id)
            result.launch(i)
        }*/

        // --------------- açao para os float button

        //botao de add
         binding.floatBtnAdd.setOnClickListener{
             result.launch(Intent(applicationContext, NewContatctActivity::class.java))
         }

        //botao de mudar a ordem da lista
        binding.floatBtnOrden.setOnClickListener{

            if(ascDesc){
                binding.floatBtnOrden.setImageResource(R.drawable.baseline_arrow_upward_24)
            }else{
                binding.floatBtnOrden.setImageResource(R.drawable.baseline_arrow_downward_24)
            }

            ascDesc = !ascDesc
            contactlist = contactlist.reversed()
            placeAdapter() // atualizar a lista sempre que o precesso de mudar a ordem da lista for executada
        }

        //forma de manter a lista atualizada
        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

            //se der tudo certo ao puxar o resultado instanciamos a lista.
            if(it.data!= null && it.resultCode==1){
                loadList()
            }else if(it.data!= null && it.resultCode==0){
                Toast.makeText(applicationContext,"Operation Canceled", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun placeAdapter() {
        adaptter = ContactListAdapter(contactlist, ContactOnClickListener { contact ->
            val i = Intent(this, ContactDetailActivity::class.java)
            //estou savalndo o valor com o nome id e o resultado.
            i.putExtra("id", contact.id)
            result.launch(i)
        })
        binding.recyclerContacts.adapter = adaptter

    }
    private fun loadList() {

        //instanciando a lista.
        contactlist = dbHelper.getAllContact().sortedWith(compareBy{it.name})

        placeAdapter()


//        contactlist =  dbHelper.getAllContact()
//        adapter = ArrayAdapter<ContactModel>(applicationContext, android.R.layout.simple_gallery_item, contactlist)
//        binding.listViewContact.adapter = adapter
    }
}
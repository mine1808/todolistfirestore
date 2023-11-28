package com.example.todolistfirestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistfirestore.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val notesCollectionRef = firestore.collection("notes")
    private val noteListLiveData: MutableLiveData<List<Contacts>> by lazy {
        MutableLiveData<List<Contacts>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            btnAdd.setOnClickListener{
                startActivity(Intent(
                    this@MainActivity, DetailActivity::class.java))
            }
        }
        observeContacts()
        getAllContacts()
    }

    private fun getAllContacts(){
        observeBudgetsChange()
    }

    override fun onResume() {
        super.onResume()
        getAllContacts()
    }

    private fun observeBudgetsChange(){
        notesCollectionRef.addSnapshotListener{ snapshots, error ->
            if (error != null){
                Log.d("MainActivity", "Error listen for budget changes:", error)
            }
            val budgets = snapshots?.toObjects(Contacts::class.java)
            if (budgets != null){
                noteListLiveData.postValue(budgets)
            }
        }
    }

    private fun observeContacts(){
        noteListLiveData.observe(this) { notes ->
            val adapterContacts = ContactsAdapter(notes){ note ->
                startActivity(Intent(this@MainActivity, DetailActivity::class.java)
                    .putExtra("note", note)
                )
            }
            adapterContacts.setOnClickDeleteListener { note ->
                deleteContacts(note)
            }

            with(binding){
                recyclerView.apply {
                    adapter = adapterContacts
                    layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }
    }

    private fun deleteContacts(note: Contacts){
        if(note.id.isEmpty()) {
            Log.d("MainActivity", "Error delete item: budget Id is empty!")
            return
        }
        notesCollectionRef.document(note.id).delete().addOnFailureListener {
            Log.d("MainActivity", "Error delete budget", it)
        }
        Toast.makeText(this@MainActivity, "Contact terhapus",
            Toast.LENGTH_SHORT).show()
    }
}
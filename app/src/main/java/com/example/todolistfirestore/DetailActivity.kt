package com.example.todolistfirestore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todolistfirestore.databinding.ActivityDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class DetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val notesCollectionRef = firestore.collection("notes")
    private var updateId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            btnBack.setOnClickListener {
                onBackPressed()
            }

            if (intent.hasExtra("note")){
                val note = intent.getSerializableExtra("note") as Contacts

                updateId = note.id
                editName.setText(note.name)
                editNumber.setText(note.number)

                btnSave.setOnClickListener{
                    val name = editName.text.toString()
                    val number = editNumber.text.toString()

                    val editedContacts = Contacts(
                        name = name,
                        number = number
                    )

                    updateContacts(editedContacts)
                    returnToMainActivity(editedContacts)
                    Toast.makeText(this@DetailActivity, "Contact berubah",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            else{
                btnSave.setOnClickListener{
                    if (editName.text.toString().isNotEmpty() &&
                        editNumber.text.toString().isNotEmpty()) {

                        val name = editName.text.toString()
                        val number = editNumber.text.toString()

                        val newContacts = Contacts(
                            name = name,
                            number = number
                        )
                        insertContacts(newContacts)
                        Toast.makeText(this@DetailActivity, "Contact tersimpan",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else{
                        if (editName.text.toString().isEmpty()){
                            editName.error = "Nama wajib diisi"
                        }
                        if (editNumber.text.toString().isEmpty()){
                            editNumber.error = "Nomor wajib diisi"
                        }
                    }
                }
            }
        }
    }

    private fun returnToMainActivity(note: Contacts) {
        val detailIntent = Intent().apply {
            putExtra("editeContacts", note)
        }
        setResult(RESULT_OK, detailIntent)
        finish()
    }
    private fun insertContacts(note: Contacts){
        notesCollectionRef.add(note).addOnSuccessListener{
                documentReference ->
            val createdBudgetId = documentReference.id
            note.id = createdBudgetId
            documentReference.set(note).addOnFailureListener{
                Log.d("MainActivity", "Error update budget id : ", it)
            }
        }.addOnFailureListener{
            Log.d("MainActivity", "Error update budget id : ", it)
        }
    }

    private fun updateContacts(note: Contacts) {
        note.id = updateId
        notesCollectionRef.document(updateId).set(note).addOnFailureListener{
            Log.d("MainActivity", "Error update budget", it)
        }
    }
}
package com.example.petadoptionfinals.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petadoptionfinals.databinding.ActivityAddPetBinding
import com.example.petadoptionfinals.databinding.ToolbarTitleBinding
import com.example.petadoptionfinals.model.petModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileWriter

class AddPetActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddPetBinding
    private lateinit var toolbarBinding : ToolbarTitleBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPetBinding.inflate(layoutInflater)
        toolbarBinding = ToolbarTitleBinding.bind(binding.root)

        setContentView(binding.root)

        binding.addContact.setOnClickListener {
            if (addContact()) {
                writeData()
                startActivity(Intent(this@AddPetActivity, MainActivity::class.java))
            }
        }
    }


    private fun inputRequirements(name: String, email: String, phone: String): Boolean {
        if (name.isEmpty()) {
            binding.inName.error = "Input missing"
            return false
        }

        if (email.isEmpty()) {
            binding.inEmail.error = "Input missing"
            return false
        }

        if (phone.isEmpty()) {
            binding.inPhone.error = "Input missing"
            return false
        }

        return true
    }

    private fun addContact(): Boolean {
        val name = binding.inName.text.toString()
        val email = binding.inEmail.text.toString()
        val phone = binding.inPhone.text.toString()

        database = FirebaseDatabase.getInstance().getReference("Pets")
        val petModel = petModel(name, email, phone)
        database.child(name).setValue(petModel).addOnSuccessListener {
            binding.inName.text.clear()
            binding.inEmail.text.clear()
            binding.inPhone.text.clear()
            Toast.makeText(this, "Pet Added", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

        if (!inputRequirements(name, email, phone)) {
            return false
        }

        return true
    }

    private fun writeData() {
        val path: File = this.filesDir
        val file: File = File(path, "datafile.txt")
        val stream: FileWriter = FileWriter(file, true)
        try {
            stream.write("${binding.inName.text}, ${binding.inEmail.text}, ${binding.inPhone.text}\n")

        } catch (e: Exception) {

        } finally {
            stream.close()
        }
    }
}

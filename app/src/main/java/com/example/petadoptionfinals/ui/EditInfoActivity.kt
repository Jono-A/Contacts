package com.example.petadoptionfinals.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.petadoptionfinals.databinding.ActivityEditInfoBinding
import com.example.petadoptionfinals.databinding.ToolbarTitleBinding
import java.io.File
import java.lang.Exception


class EditInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditInfoBinding
    private lateinit var toolbarBinding: ToolbarTitleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val position = intent.getIntExtra("position", 0)

        binding = ActivityEditInfoBinding.inflate(layoutInflater)
        toolbarBinding = ToolbarTitleBinding.bind(binding.root)

        setContentView(binding.root)

        toolbarBinding.toolbarTitle.text = "Edit Contact"

        binding.editContact.setOnClickListener {
            if (performEditContact()) {
                editData(position)
                startActivity(Intent(this@EditInfoActivity, MainActivity::class.java))

            }
        }

    }

    fun editData(position:Int){

        val path : File = this.filesDir
        val file : File = File(path, "datafile.txt")
        val tempFile : File = File.createTempFile("temp", null, this.filesDir)

        try {
            val lines = ArrayList<String>()

            // Read the file and modify the specific line
            file.forEachLine { line ->
                val items = line.split(",")
                if (lines.size == position) {
                    // Modify the contact at the given position
                    val oldName = items.getOrNull(0) ?: ""
                    val editName = binding.inName.text.toString().takeUnless { it.isEmpty() } ?: oldName

                    val oldEmail = items.getOrNull(1) ?: ""
                    val editEmail = binding.inEmail.text.toString().takeUnless { it.isEmpty() } ?: oldEmail

                    val oldPhone = items.getOrNull(2) ?: ""
                    val editPhone = binding.inPhone.text.toString().takeUnless { it.isEmpty() } ?: oldPhone

                    val modifiedLine = "$editName,$editEmail,$editPhone"
                    lines.add(modifiedLine)
                } else {
                    lines.add(line)
                }
            }


            file.printWriter().use { out ->
                lines.forEach { line ->
                    out.println(line)
                }
            }

            Toast.makeText(this, "Contact edited successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to edit contact", Toast.LENGTH_SHORT).show()
        }
    }


    private fun inputRequirements(name: String, email: String, phone: String): Boolean {
        if (name.isEmpty()) {
            binding.inName.error = "Name field is required!"
            return false
        }

        if (email.isEmpty()) {
            binding.inEmail.error = "Email field is required!"
            return false
        }

        if (phone.isEmpty()) {
            binding.inPhone.error = "Phone field is required!"
            return false
        }

        return true
    }

    private fun performEditContact(): Boolean {
        val name = binding.inName.text.toString()
        val email = binding.inEmail.text.toString()
        val phone = binding.inPhone.text.toString()

        if (!inputRequirements(name, email, phone)) {
            return false
        }
        return true
    }

}
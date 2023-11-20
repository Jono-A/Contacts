package com.example.petadoptionfinals.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petadoptionfinals.databinding.ActivityAddPetBinding
import com.example.petadoptionfinals.databinding.ToolbarTitleBinding
import com.example.petadoptionfinals.model.petModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddPetActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddPetBinding
    private lateinit var toolbarBinding : ToolbarTitleBinding
    private lateinit var database : DatabaseReference
    private lateinit var ImageUri : Uri

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

        binding.addImage.setOnClickListener{

            selectImage()
        }



    }




    private fun selectImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            binding.firebaseImage.setImageURI(ImageUri)
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
        val progressDialog = ProgressDialog (this)
        progressDialog.setMessage("Uploading Pet Image...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(ImageUri).
        addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener {

                database = FirebaseDatabase.getInstance().getReference("Pets")
                val petModel = petModel(name, email, phone, it.toString())
                database.child(name).setValue(petModel).addOnSuccessListener {
                    binding.inName.text.clear()
                    binding.inEmail.text.clear()
                    binding.inPhone.text.clear()
                    Toast.makeText(this, "Pet Added", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

            binding.firebaseImage.setImageURI(null)
            Toast.makeText(this@AddPetActivity, "Successfully uploaded", Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing) progressDialog.dismiss()
        }.addOnFailureListener{

            if (progressDialog.isShowing)progressDialog.dismiss()
            Toast.makeText(this@AddPetActivity,"Failed",Toast.LENGTH_SHORT).show()
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

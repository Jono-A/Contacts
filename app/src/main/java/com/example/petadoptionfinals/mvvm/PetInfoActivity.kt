package com.example.petadoptionfinals.mvvm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petadoptionfinals.R
import com.example.petadoptionfinals.databinding.ActivityPetInfoBinding
import com.example.petadoptionfinals.databinding.ToolbarTitleBinding
import com.example.petadoptionfinals.model.petModel
import com.example.petadoptionfinals.ui.EditInfoActivity
import com.example.petadoptionfinals.ui.MainActivity
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class PetInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPetInfoBinding
    private lateinit var toolbarBinding : ToolbarTitleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPetInfoBinding.inflate(layoutInflater)
        toolbarBinding = ToolbarTitleBinding.bind(binding.root)

        setContentView(binding.root)

        toolbarBinding.toolbarTitle.text = "Contact Information"

        val position = intent.getIntExtra("position", 0)

        binding.btnEdit.setOnClickListener {

            startActivity(Intent(this@PetInfoActivity, EditInfoActivity::class.java))
        }


        val students : petModel? = intent.getParcelableExtra("contact")

        //activity_contact_info
        if (students != null) {
            binding.profilePicture.setImageDrawable(this.getDrawable(R.drawable.studentavatar))
            binding.Name.text = students?.name
            binding.Email.text = students?.email
            binding.Phone.text = students?.phone
        }

        //long click Email
        binding.Email.setOnLongClickListener {
            val email = binding.Email.text.toString()
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
            startActivity(emailIntent)
            true
        }

        //long click Phone Number
        binding.Phone.setOnLongClickListener {
            val phoneNumber = binding.Phone.text.toString()
            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivity(dialIntent)
            true
        }

        //dialog pop up
        binding.btnDelete.setOnClickListener{
            confirmAction(position)
        }
    }

    //confirm delete dialog
    fun confirmAction(position: Int):Boolean{
        val builder : android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Delete Contact")
        builder.setMessage("Are you sure you want to delete this contact?")
        builder.setPositiveButton("Yes"){
                dialog, _ ->
            dialog.dismiss()
            Toast.makeText(this, "Contact Deleted", Toast.LENGTH_SHORT).show()
            deleteItem(position)

            this.startActivity(Intent(this, MainActivity::class.java))
        }
        builder.setNegativeButton("No") { dialog, _  ->
            dialog.dismiss()
        }
        val dialog: android.app.AlertDialog = builder.create()
        dialog?.show()
        return false
    }

    fun deleteItem(position:Int){

        val path : File = this.filesDir
        val file : File = File(path, "datafile.txt")
        val tempFile : File = File.createTempFile("temp", null,this.filesDir)

        try {
            val reader = BufferedReader(FileReader(file))
            val writer = BufferedWriter(FileWriter(tempFile))
            var line : String?
            var currentLine = 0

            while (reader.readLine().also {
                    line = it
                } != null){
                if(currentLine == position){

                } else{
                    val str = line.toString()
                    writer.write(str + '\n')
                }
                currentLine++
            }
            reader.close()
            writer.close()

            file.delete()
            tempFile.renameTo(file)
        } catch (e: Exception){
            Toast.makeText(this, "Action Failed", Toast.LENGTH_SHORT).show() //failed to delete | Toast
        }
    }

}
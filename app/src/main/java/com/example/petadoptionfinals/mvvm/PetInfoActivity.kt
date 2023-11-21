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
import com.example.petadoptionfinals.model.user
import com.example.petadoptionfinals.ui.AddPetActivity
import com.example.petadoptionfinals.ui.EditInfoActivity
import com.example.petadoptionfinals.ui.MainActivity
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.FirebaseDatabase
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class PetInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetInfoBinding
    private lateinit var toolbarBinding: ToolbarTitleBinding

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


        val students: petModel? = intent.getParcelableExtra("contact")

        //activity_contact_info
        if (students != null) {
            binding.profilePicture.setImageDrawable(this.getDrawable(R.drawable.studentavatar))
            binding.Name.text = students?.name
            binding.inGender.text = students?.gender
            binding.inBreed.text = students?.breed
            binding.inUserInfo.text = students?.userinfo
        }

        //long click Email
        binding.inGender.setOnLongClickListener {
            val gender = binding.inGender.text.toString()
            val genderIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$gender"))
            startActivity(genderIntent)
            true
        }

        //long click Phone Number
        binding.inBreed.setOnLongClickListener {
            val breed = binding.inBreed.text.toString()
            val breedIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$breed"))
            startActivity(breedIntent)
            true
        }

        binding.inUserInfo.setOnLongClickListener {
            val UserInfo = binding.inUserInfo.text.toString()
            val UserInfoIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$UserInfo"))
            startActivity(UserInfoIntent)
            true
        }
        //dialog pop up
        binding.btnDelete.setOnClickListener {
            if (students != null) {
                confirmAction(students)
            }
            intent.getStringExtra("name").toString()
        }
    }

    //confirm delete dialog
    fun confirmAction(students: petModel): Boolean {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Delete Contact")
        builder.setMessage("Are you sure you want to delete this contact?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(this, "Contact Deleted", Toast.LENGTH_SHORT).show()
            deleteItem(students)

            this.startActivity(Intent(this, MainActivity::class.java))
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: android.app.AlertDialog = builder.create()
        dialog?.show()
        return false
    }

    fun deleteItem(students: petModel) {

        val databaseReference =
            students.name?.let { FirebaseDatabase.getInstance().getReference("Pets").child(it) }
        val mTask = databaseReference?.removeValue()

        mTask?.addOnSuccessListener {
            Toast.makeText(this, "Data has been erased", Toast.LENGTH_LONG).show()

            val intent = Intent(this, PetViewModel::class.java)
            finish()
            startActivity(intent)
        }?.addOnFailureListener { error ->
            Toast.makeText(this, "Delete Data error ${error.message}", Toast.LENGTH_LONG).show()

        }

    }
}
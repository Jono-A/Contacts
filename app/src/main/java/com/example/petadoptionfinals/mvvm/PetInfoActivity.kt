package com.example.petadoptionfinals.mvvm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.petadoptionfinals.R
import com.example.petadoptionfinals.databinding.ActivityPetInfoBinding
import com.example.petadoptionfinals.databinding.ToolbarTitleBinding
import com.example.petadoptionfinals.model.petModel
import com.example.petadoptionfinals.model.user
import com.example.petadoptionfinals.ui.AddPetActivity
import com.example.petadoptionfinals.ui.EditInfoActivity
import com.example.petadoptionfinals.ui.MainActivity
import com.example.petadoptionfinals.ui.PetsAdapters
import com.google.firebase.Firebase
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class PetInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetInfoBinding
    private lateinit var toolbarBinding: ToolbarTitleBinding
    private val authFirebase = Firebase.auth //**
    private lateinit var  students: petModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPetInfoBinding.inflate(layoutInflater)
        toolbarBinding = ToolbarTitleBinding.bind(binding.root)

        setContentView(binding.root)

        toolbarBinding.toolbarTitle.text = "Contact Information"

        val position = intent.getIntExtra("position", 0)



        students = intent.getParcelableExtra("contact")!!
        //Toast.makeText(this@PetInfoActivity, students.userinfo, Toast.LENGTH_SHORT).show()
        referenceDatabase()

        //activity_pet_info
        if (students != null) {
            Glide.with(this@PetInfoActivity)
                .load(students.imageUrl)
                .into(binding.profilePicture)
            //binding.profilePicture.setImageDrawable(this.getDrawable(R.drawable.studentavatar))
            binding.Name.text = students?.name
            binding.inGender.text = students?.gender
            binding.inBreed.text = students?.breed
            //binding.inUserInfo.text = students?.userinfo
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

       /* binding.inUserInfo.setOnLongClickListener {
            val UserInfo = binding.inUserInfo.text.toString()
            val UserInfoIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$UserInfo"))
            startActivity(UserInfoIntent)
            true
        } */
        //dialog pop up
        binding.btnDelete.setOnClickListener {
            if (students != null) {
                confirmAction(students)
            }
            intent.getStringExtra("name").toString()
        }
    }

    private fun referenceDatabase() {
        //1. reference the database to the object holding lists
        //2. use the class ValueEventListener
        lateinit var data: user

        val listListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              data = snapshot.getValue<user>()!!
                binding.inUserName.text = data?.name    //add user info
                binding.inPetOwnerphoneNum.text=data?.phoneNum
                binding.inPetEmail.text=data?.email
            }

            override fun onCancelled(error: DatabaseError) {
                //  readStatus.value = ReadListsState.Error(error.message)
            }

        }

        var petReference = students.userinfo?.let {
            Firebase.database.reference.child("user").child(
                it
            )
        }
        petReference?.addValueEventListener(listListener)
    }

    //confirm delete dialog
    fun confirmAction(students: petModel): Boolean {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Adopt")
        builder.setMessage("Are you sure you want to adopt this Pet?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(this, "Pet Adopted", Toast.LENGTH_SHORT).show()
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
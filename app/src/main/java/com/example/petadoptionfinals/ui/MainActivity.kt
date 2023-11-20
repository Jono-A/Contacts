package com.example.petadoptionfinals.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petadoptionfinals.databinding.ActivityMainBinding
import com.example.petadoptionfinals.databinding.ToolbarTitleBinding
import com.example.petadoptionfinals.model.petModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import java.io.File
import java.util.Scanner

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbarBinding: ToolbarTitleBinding
    private lateinit var contactsAdapters: PetsAdapters

    private val studentsList = ArrayList<petModel>()
    var doesFileExist : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        toolbarBinding = ToolbarTitleBinding.bind(binding.root)

        setContentView(binding.root)

        referenceDatabase()
        binding.rvList.layoutManager = LinearLayoutManager(this)


        binding.addButton.setOnClickListener{
           val intent = Intent(this, AddPetActivity::class.java)
            startActivity(intent)
        }
    }

    private fun referenceDatabase() {
        //1. reference the database to the object holding lists
        //2. use the class ValueEventListener
        val listListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //clear the existing arraylist before adding data retrieved from firebase
                //this will ensure that data will not be duplicated everytime onDataChange executes
                studentsList.clear()
                //traverse through the list retrieved from firebase and add each item to your arraylist
                for(data in snapshot.children) {
                    data.getValue<petModel>()?.let { studentsList.add(it)
                    }
                }
                contactsAdapters = PetsAdapters(this@MainActivity, studentsList)
                binding.rvList.adapter = contactsAdapters
                //send back the arraylist to the activity/fragment to be handled by the adapter
              //  readStatus.value = ReadListsState.Default(studentsList)
            }

            override fun onCancelled(error: DatabaseError) {
              //  readStatus.value = ReadListsState.Error(error.message)
            }

        }
        var petReference = Firebase.database.reference.child("Pets")
        petReference.addValueEventListener(listListener)
    }




    }







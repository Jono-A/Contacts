package com.example.petadoptionfinals.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petadoptionfinals.databinding.ActivityMainBinding
import com.example.petadoptionfinals.databinding.ToolbarTitleBinding
import com.example.petadoptionfinals.model.petModel
import java.io.File
import java.util.Scanner

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbarBinding: ToolbarTitleBinding

    private val studentsList = ArrayList<petModel>()
    var doesFileExist : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        toolbarBinding = ToolbarTitleBinding.bind(binding.root)

        setContentView(binding.root)

        createFile(this)
        readData(this)

        binding.rvList.layoutManager = LinearLayoutManager(this)
        val contactsAdapters = PetsAdapters(this, studentsList)
        binding.rvList.adapter = contactsAdapters

        binding.addButton.setOnClickListener{
           val intent = Intent(this, AddPetActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createFile(context : Context){
        var path : File = context.filesDir
        var file : File = File(path, "datafile.txt")
        try {
            doesFileExist = file.exists()
            if (!(doesFileExist)) {
                file.createNewFile()
            } else {
            }
        }catch (e: Exception){

        }

        }

    private fun readData(context: Context) {
        var path : File = context.filesDir
        var file : File = File(path, "datafile.txt")

        val sc : Scanner = Scanner(file)

        var array : List<String> = listOf()

        while (sc.hasNextLine()){
            val line = sc.nextLine()
            val delim = ","
            array = line.split(delim)
            studentsList.add(petModel(array[0], array[1], array[2]))

    }

        }

    }







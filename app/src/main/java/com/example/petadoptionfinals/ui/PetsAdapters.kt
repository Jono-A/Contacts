package com.example.petadoptionfinals.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.petadoptionfinals.R
import com.example.petadoptionfinals.databinding.ItemStudentBinding
import com.example.petadoptionfinals.model.petModel
import com.example.petadoptionfinals.mvvm.PetInfoActivity
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class PetsAdapters(private val context : Context, var studentsList : ArrayList<petModel>) :
    RecyclerView.Adapter<PetsAdapters.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    //+
    fun updatedStudentList(updatedStudentList: ArrayList<petModel>) {

        studentsList = updatedStudentList
    }

    override fun getItemCount(): Int {
        return studentsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        studentsList[position].let {
            holder.bind(it, position)
        }

    }

    class ViewHolder(

        private val binding: ItemStudentBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        //item student
        fun bind(contacts: petModel, position: Int) {
            binding.ivProfile.setImageDrawable(context.getDrawable(R.drawable.studentavatar))
            binding.tvName.text = contacts.name
            binding.tvEmail.text = contacts.email
            binding.tvPhone.text = contacts.phone

            binding.llData.setOnClickListener {
                val intent = Intent(context, PetInfoActivity::class.java)
                intent.putExtra("contact", contacts)
                intent.putExtra("position", position)

                context.startActivity(intent)
            }

          binding.llData.setOnLongClickListener{
                show_promptWhy()
            }
        }

        private fun show_promptWhy() : Boolean {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Choose an action")
            builder.setMessage("Choose...")
            builder.setPositiveButton("Edit") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(context, "Edit here!", Toast.LENGTH_SHORT).show()

                val intent = Intent(context, EditInfoActivity::class.java)
                intent.putExtra( "position", position)
                context.startActivity(intent)
            }
            builder.setNegativeButton("Delete") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(context, "Delete this Contact?", Toast.LENGTH_SHORT).show()
                show_areYouSure()
            }
            val dialog: AlertDialog? = builder.create()
            dialog?.show()
            return false
        }

        private fun show_areYouSure() : Boolean {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Confirm Delete")
            builder.setMessage("Are you sure you want to delete?")
            builder.setPositiveButton("Yes") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(context, "Contact Deleted", Toast.LENGTH_SHORT).show()
                deleteItem(position, context)
                context.startActivity(Intent(context, MainActivity::class.java))
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(context, "Delete Cancelled", Toast.LENGTH_SHORT).show()
            }
            val dialog: AlertDialog? = builder.create()
            dialog?.show()
            return false
        }

        private fun deleteItem(position : Int, context: Context) {

            val path : File = context.filesDir
            val file : File = File(path, "datafile.txt")
            val tempFile : File = File.createTempFile("temp", null, context.filesDir)

            try {
                val reader = BufferedReader(FileReader(file))
                val writer = BufferedWriter(FileWriter(tempFile))
                var line: String?
                var currLine = 0

                while (reader.readLine().also { line = it } != null) {
                    if (currLine == position){
                        Toast.makeText(context, "Contact Deleted.", Toast.LENGTH_SHORT).show()
                    } else {
                        val str = line.toString()
                        writer.write(str + '\n')
                    }
                    currLine++
                }

                reader.close()
                writer.close()

                file.delete()
                tempFile.renameTo(file)
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to Delete Contact", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

            }

    }
}
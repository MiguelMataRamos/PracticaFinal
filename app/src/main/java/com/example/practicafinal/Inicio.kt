package com.example.practicafinal

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.values

class Inicio : AppCompatActivity() {
    private lateinit var db_ref: DatabaseReference
    private var user: Usuario? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        db_ref = FirebaseDatabase.getInstance().reference
        var auth = FirebaseAuth.getInstance()

        //coger el usuario de la base de datos y guardarlo en la variable user como objeto de la clase Usuario
        db_ref.child("Usuarios").child(auth.uid!!).get().addOnSuccessListener {
            user = it.getValue(Usuario::class.java)

        }



    }
}
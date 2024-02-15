package com.example.practicafinal

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class Autor : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autor)
        supportActionBar?.hide()

        var boton = findViewById<Button>(R.id.regresar)

        boton.setOnClickListener {
            var sp = PreferenceManager.getDefaultSharedPreferences(this)
            var admin = sp.getString("admin", "0")
            if (admin == "1"){
                var intent = Intent(this, Administrador::class.java)
                startActivity(intent)
            }else{
                var intent = Intent(this, Cliente::class.java)
                startActivity(intent)
            }
        }

    }
}
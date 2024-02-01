package com.example.practicafinal

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PantallaCarga : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_carga)
        var sp = PreferenceManager.getDefaultSharedPreferences(this@PantallaCarga)

        var db_ref = FirebaseDatabase.getInstance().reference
        var user:Usuario? = null

        CoroutineScope(Dispatchers.IO).launch {
            user = Utilidades.cogerUsuario(db_ref)
            sp.edit().apply(){
                putString("nombre_user", user?.nombre)
                putString("email_user", user?.email)
                putString("admin_user", user?.admin)
                putString("id_user", user?.id)
                apply()
            }

            withContext(Dispatchers.Main){
                if (user?.admin == "1") {
                    var intent = Intent(this@PantallaCarga, InicioAdmin::class.java)
                    startActivity(intent)
                }
            }


        }



    }
}
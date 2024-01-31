package com.example.practicafinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.practicafinal.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding
    private lateinit var autentication: FirebaseAuth
    private var email: String? = null
    private var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        autentication = FirebaseAuth.getInstance()

        bind.textViewNoCuenta.setOnClickListener {
            var intent = Intent(this, Registrarse::class.java)
            startActivity(intent)
        }

        bind.buttonLogin.setOnClickListener {
            if (cogerEmail() != null && cogerPassword() != null) {
                autentication.signInWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            var intent = Intent(this, Ver::class.java)
                            startActivity(intent)
                        } else {
                            bind.textInputLayoutEmail.error = "Email o contraseña incorrectos"
                            bind.textInputLayoutPass.error = "Email o contraseña incorrectos"
                        }
                    }
            }
        }

    }

    private fun cogerEmail(): String? {
        return if (bind.mail.text.toString().isNullOrBlank()){
            bind.textInputLayoutEmail.error = "Email esta vacio"
            null
        }else{
            bind.textInputLayoutEmail.error = null
            email = bind.mail.text.toString()
            email
        }

    }
    private fun cogerPassword(): String? {
        return if (bind.pass.text.toString().isNullOrBlank()) {
            bind.textInputLayoutPass.error = "Contraseña esta vacia"
            null
        }else{
            bind.textInputLayoutPass.error = null
            password = bind.pass.text.toString()
            password
        }

    }


}
package com.example.practicafinal

import com.google.firebase.database.FirebaseDatabase

class Utilidades{
    companion object{

        fun subirUsuario(usuario: Usuario){
            var db_ref = FirebaseDatabase.getInstance().reference
            db_ref.child("Usuarios").child(usuario.id).setValue(usuario)
        }
    }


}
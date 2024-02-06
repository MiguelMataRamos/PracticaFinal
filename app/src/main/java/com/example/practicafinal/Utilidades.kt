package com.example.practicafinal

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class Utilidades(context: Context) {
    companion object {
        fun subirUsuario(usuario: Usuario) {
            var db_ref = FirebaseDatabase.getInstance().reference
            db_ref.child("Usuarios").child(usuario.id).setValue(usuario)
        }
        suspend fun cogerUsuario(db_ref: DatabaseReference):Usuario?{
            var user:Usuario? = null
            val datasnapshot = db_ref.child("Usuarios").child(FirebaseAuth.getInstance().uid!!).get().await()
            user = datasnapshot.getValue(Usuario::class.java)
            return user
        }

        suspend fun guardarImagenCarta(idGenerado: String, urlimg: Uri): String {
            var st = FirebaseStorage.getInstance().reference

            lateinit var urlimagenfirebase: Uri

            urlimagenfirebase = st.child("Tienda").child("ImagenesCartas").child(idGenerado)
                .putFile(urlimg).await().storage.downloadUrl.await()

            return urlimagenfirebase.toString()

        }

        fun subirCarta(nuevacarta: Carta) {
            var db_ref = FirebaseDatabase.getInstance().reference
            db_ref.child("Tienda").child("Cartas").child(nuevacarta.id!!).setValue(nuevacarta)
        }
    }


}
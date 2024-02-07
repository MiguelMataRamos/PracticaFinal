package com.example.practicafinal

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class Utilidades(context: Context) {
    companion object {
        var db_ref = FirebaseDatabase.getInstance().reference
        var st = FirebaseStorage.getInstance().reference
        fun subirUsuario(usuario: Usuario) {
            db_ref.child("Tienda").child("Usuarios").child(usuario.id).setValue(usuario)
        }
        suspend fun cogerUsuario(db_ref: DatabaseReference):Usuario?{
            var user:Usuario? = null
            val datasnapshot = db_ref.child("Tienda").child("Usuarios").child(FirebaseAuth.getInstance().uid!!).get().await()
            user = datasnapshot.getValue(Usuario::class.java)
            return user
        }

        suspend fun guardarImagenCarta(idGenerado: String, urlimg: Uri): String {

            lateinit var urlimagenfirebase: Uri

            urlimagenfirebase = st.child("Tienda").child("ImagenesCartas").child(idGenerado)
                .putFile(urlimg).await().storage.downloadUrl.await()

            return urlimagenfirebase.toString()

        }
        suspend fun guardarImagenEvento(idGenerado: String, urlimg: Uri): String {

            lateinit var urlimagenfirebase: Uri

            urlimagenfirebase = st.child("Tienda").child("ImagenesEventos").child(idGenerado)
                .putFile(urlimg).await().storage.downloadUrl.await()

            return urlimagenfirebase.toString()

        }

        fun subirCarta(nuevacarta: Carta) {
            db_ref.child("Tienda").child("Cartas").child(nuevacarta.id!!).setValue(nuevacarta)
        }
        fun subirEvento(nuevoevento: Evento) {
            db_ref.child("Tienda").child("Eventos").child(nuevoevento.id!!).setValue(nuevoevento)
        }


        fun animacion_carga(contexto: Context): CircularProgressDrawable {
            val animacion = CircularProgressDrawable(contexto)
            animacion.strokeWidth = 5f
            animacion.centerRadius = 30f
            animacion.start()
            return animacion
        }
        val transicion = DrawableTransitionOptions.withCrossFade(500)
        fun opcionesGlide(context: Context): com.bumptech.glide.request.RequestOptions {
            val options = com.bumptech.glide.request.RequestOptions()
                .placeholder(animacion_carga(context))
                .fallback(R.drawable.fotodef)
                .error(R.drawable.fotodef)
            return options
        }

        fun showPopupMenuOptions(view: View, context:Context) {
            val popupMenu = PopupMenu(context, view)
            popupMenu.menuInflater.inflate(R.menu.menu_opciones, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.cerrarsesion -> {
                        // Aquí va tu código para la opción 1
                        //cierra la sesion
                        val auth = FirebaseAuth.getInstance()
                        auth.signOut()
                        //redirige a la pantalla de login
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }




    }


}
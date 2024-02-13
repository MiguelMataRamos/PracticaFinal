package com.example.practicafinal

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.preference.PreferenceManager
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class Utilidades(context: Context) {
    companion object {
        var db_ref = FirebaseDatabase.getInstance().reference
        var st = FirebaseStorage.getInstance().reference
        var auth = FirebaseAuth.getInstance()


        fun cogerAdmin(context: Context): String {
            var sp = PreferenceManager.getDefaultSharedPreferences(context)
            var admin = sp.getString("admin", "0")
            return admin!!
        }

        fun obtenerListaCartas(db_reff: DatabaseReference): MutableList<Carta> {
            var lista = mutableListOf<Carta>()

            db_reff.child("Tienda").child("Cartas")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { hijo: DataSnapshot ->
                            val pojo_carta = hijo.getValue(Carta::class.java)
                            lista.add(pojo_carta!!)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }
                })

            return lista
        }

        fun obtenerListaEventos(): MutableList<Evento> {
            var lista = mutableListOf<Evento>()

            db_ref.child("Tienda").child("Eventos")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { hijo: DataSnapshot ->
                            val pojo_evento = hijo.getValue(Evento::class.java)
                            lista.add(pojo_evento!!)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }
                })

            return lista
        }

        fun existeCarta(cartas: List<Carta>, nombre: String): Boolean {
            return cartas.any {
                it.nombre!!.lowercase() == nombre.lowercase()
            }
        }

        fun existeEvento(eventos: List<Evento>, nombre: String): Boolean {
            return eventos.any {
                it.nombre!!.lowercase() == nombre.lowercase()
            }
        }


        suspend fun cogerUsuario(): Usuario? {
            var user: Usuario? = null
            val datasnapshot =
                db_ref.child("Tienda").child("Usuarios").child(FirebaseAuth.getInstance().uid!!)
                    .get().await()
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

        suspend fun guardarImagenUser(urlimg: Uri): String {
            lateinit var urlimagenfirebase: Uri

            urlimagenfirebase = st.child("Tienda").child("Usuarios").child(auth.uid.toString())
                .putFile(urlimg).await().storage.downloadUrl.await()

            return urlimagenfirebase.toString()

        }

        fun subirCarta(nuevacarta: Carta) {
            db_ref.child("Tienda").child("Cartas").child(nuevacarta.id!!).setValue(nuevacarta)
        }

        fun subirEvento(nuevoevento: Evento) {
            db_ref.child("Tienda").child("Eventos").child(nuevoevento.id!!).setValue(nuevoevento)
        }

        fun subirPedido(pedido: Pedido) {
            db_ref.child("Tienda").child("Pedidos").child(pedido.id!!).setValue(pedido)
        }

        fun subirUsuario(usuario: Usuario) {
            db_ref.child("Tienda").child("Usuarios").child(usuario.id).setValue(usuario)
        }


        private fun animacion_carga(contexto: Context): CircularProgressDrawable {
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

        fun showPopupMenuOptions(view: View, context: Context) {
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

        fun apuntarseEvento(itemActual: Evento, contexto: Context) {
            var aforo = itemActual.aforoactual!!.toInt()

            aforo++
            db_ref.child("Tienda").child("Eventos").child(itemActual.id!!).child("aforoactual")
                .setValue(aforo.toString())

            Toast.makeText(contexto, "Te has apuntado al evento", Toast.LENGTH_SHORT).show()
        }

        //coge la fecha actual y la formatea xx/xx/xxxx
        fun cogerFecha(): String {
            val c = java.util.Calendar.getInstance()
            val dia = c.get(java.util.Calendar.DAY_OF_MONTH)
            val mes = c.get(java.util.Calendar.MONTH) + 1
            val anio = c.get(java.util.Calendar.YEAR)
            return "$dia/$mes/$anio"
        }

        fun venderPedido(contexto: Context, itemActual: Pedido) {
            db_ref.child("Tienda").child("Pedidos").child(itemActual.id!!).child("estado")
                .setValue("1")
            db_ref.child("Tienda").child("Cartas").child(itemActual.idcarta!!).child("disponible")
                .setValue("0")
            Toast.makeText(contexto, "Pedido vendido", Toast.LENGTH_SHORT).show()
        }

        fun denegarPedido(contexto: Context, itemActual: Pedido) {
            //borrar de la base de datos
            db_ref.child("Tienda").child("Pedidos").child(itemActual.id!!).removeValue()
            //poner carta como disponible
            db_ref.child("Tienda").child("Cartas").child(itemActual.idcarta!!).child("disponible")
                .setValue("1")
            Toast.makeText(contexto, "Pedido denegado", Toast.LENGTH_SHORT).show()

        }


    }


}
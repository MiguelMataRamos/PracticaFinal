package com.example.practicafinal

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class CartaAdaptador(
    private var lista_cartas: MutableList<Carta>,
    private val fragmentoActual: String
) :
    RecyclerView.Adapter<CartaAdaptador.CartaViewHolder>(), Filterable {
    private lateinit var contexto: Context
    private var lista_filtrada = lista_cartas

    class CartaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombre)
        val precio: TextView = itemView.findViewById(R.id.precio)
        val categoria: TextView = itemView.findViewById(R.id.categoria)
        val imagen: ImageView = itemView.findViewById(R.id.img)
        val disponible: View = itemView.findViewById(R.id.disponible)
        val comprar: Button = itemView.findViewById(R.id.comprar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartaViewHolder {
        val vista_item =
            LayoutInflater.from(parent.context).inflate(R.layout.item_carta, parent, false)
        contexto = parent.context
        return CartaViewHolder(vista_item)
    }

    override fun getItemCount(): Int = lista_filtrada.size

    override fun onBindViewHolder(holder: CartaViewHolder, position: Int) {
        val item_actual = lista_filtrada[position]
        holder.nombre.text = item_actual.nombre
        Log.v("nombre", item_actual.nombre.toString())
        holder.precio.text = item_actual.precio
        holder.categoria.text = item_actual.categoria

        if (Utilidades.cogerAdmin(contexto) == "0") {
            if (fragmentoActual == "user") {
                holder.comprar.visibility = View.GONE
            }else{
                holder.comprar.visibility = View.VISIBLE
            }
            holder.disponible.visibility = View.INVISIBLE
        } else {
            holder.disponible.visibility = View.VISIBLE
            holder.comprar.visibility = View.INVISIBLE
        }


        if (item_actual.disponible == "1") {
            holder.disponible.background = contexto.getDrawable(R.drawable.fondo_disponible)
        } else if (item_actual.disponible == "0") {
            holder.disponible.background = contexto.getDrawable(R.drawable.fondo_nodisponible)
        } else {
            holder.disponible.background = contexto.getDrawable(R.drawable.fondo_pedido)
        }

        val URL: String? = when (item_actual.imagen) {
            "" -> null
            else -> item_actual.imagen
        }

        Glide.with(contexto)
            .load(URL)
            .apply(Utilidades.opcionesGlide(contexto))
            .transition(Utilidades.transicion)
            .into(holder.imagen)


        holder.itemView.setOnLongClickListener {

            val admin = Utilidades.cogerAdmin(contexto)

            if (admin == "1") {
                val popupMenu = PopupMenu(contexto, it)
                popupMenu.menuInflater.inflate(R.menu.menu_edit_del, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.edit -> {
                            // Aquí va tu código para la opción 1
                            val bundle = Bundle()
                            bundle.putString("nombre", item_actual.nombre)
                            bundle.putString("precio", item_actual.precio)
                            bundle.putString("categoria", item_actual.categoria)
                            bundle.putString("imagen", item_actual.imagen)
                            bundle.putString("id", item_actual.id)

                            // Crear una instancia del fragment, establecer los argumentos y abrir el fragment
                            val fragment = EditarCartaFragment()
                            fragment.arguments = bundle
                            val transaction =
                                (contexto as Administrador).supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.fragment_container, fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()
                            true
                        }

                        R.id.del -> {
                            //Se avisa al usuario si quirere borrar ese objeto
                            val builder = AlertDialog.Builder(contexto)
                            builder.setTitle("Eliminar")
                            builder.setMessage("¿Estas seguro de que quieres eliminar este objeto?")
                            builder.setPositiveButton("Si") { dialog, which ->
                                //se elimina el objeto de la base de datos
                                val db_ref = FirebaseDatabase.getInstance().reference
                                db_ref.child("Tienda").child("Cartas").child(item_actual.id!!)
                                    .removeValue()
                                //se elimina el objeto de la lista
                                lista_filtrada.removeAt(position)
                                notifyDataSetChanged()
                                Toast.makeText(contexto, "Carta eliminada", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            builder.setNegativeButton("No") { dialog, which ->
                                //se cancela la eliminacion
                            }
                            builder.show()
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()

            }
            true


        }

        holder.comprar.setOnClickListener {
            //alerta para confirmar la compra
            val builder = AlertDialog.Builder(contexto)
            builder.setTitle("Comprar")
            builder.setMessage("¿Estas seguro de que quieres comprar este objeto?. Se enviara una peticion al administrador y este te confirmara el pedido")
            builder.setPositiveButton("Si") { dialog, which ->
                //se envia la peticion al administrador
                var idpedido = Utilidades.db_ref.child("Tienda").child("Pedidos").push().key
                idpedido = idpedido + "_" + item_actual.nombre

                var fecha = Utilidades.cogerFecha()

                var pedido = Pedido(
                    idpedido,
                    Utilidades.auth.uid,
                    item_actual.id,
                    item_actual.nombre,
                    item_actual.precio!!.toDouble(),
                    fecha
                )
                Utilidades.subirPedido(pedido)
                Toast.makeText(contexto, "Solicitud de pedido enviada", Toast.LENGTH_SHORT).show()
                //poner como no procesando esa carta en la base de datos y en la lista
                Utilidades.db_ref.child("Tienda").child("Cartas").child(item_actual.id!!)
                    .child("disponible")
                    .setValue("2")


            }
            builder.setNegativeButton("No") { dialog, which ->
                //se cancela la compra

            }
            builder.show()
        }


    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val busqueda = p0.toString().lowercase()
                var sp = PreferenceManager.getDefaultSharedPreferences(contexto)
                var admin = sp.getString("admin", "0")

                Log.d("ADMIN", admin.toString())

                if (admin == "1") {
                    if (busqueda.isEmpty()) {
                        lista_filtrada = lista_cartas
                    } else {
                        lista_filtrada = (lista_cartas.filter {
                            it.nombre.toString().lowercase().contains(busqueda)
                        }) as MutableList<Carta>
                    }
                } else {
                    if (busqueda.isEmpty()) {
                        lista_filtrada =
                            (lista_cartas.filter { it.disponible == "1" }) as MutableList<Carta>
                    } else {
                        lista_filtrada = (lista_cartas.filter {
                            it.disponible == "1" && it.nombre.toString().lowercase()
                                .contains(busqueda)
                        }) as MutableList<Carta>
                    }
                }


                val filterResults = FilterResults()
                filterResults.values = lista_filtrada

                return filterResults

            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                notifyDataSetChanged()
            }

        }
    }
}
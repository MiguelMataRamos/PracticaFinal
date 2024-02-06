package com.example.practicafinal

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class CartaAdaptador(private var lista_cartas: MutableList<Carta>) :
    RecyclerView.Adapter<CartaAdaptador.CartaViewHolder>(), Filterable {
    private lateinit var contexto: Context
    private var lista_filtrada = lista_cartas

    class CartaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombre)
        val precio: TextView = itemView.findViewById(R.id.precio)
        val categoria: TextView = itemView.findViewById(R.id.categoria)
        val imagen: ImageView = itemView.findViewById(R.id.img)
        val disponible: View = itemView.findViewById(R.id.disponible)

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
        holder.precio.text = item_actual.precio
        holder.categoria.text = item_actual.categoria

        if (item_actual.disponible) {
            holder.disponible.setBackgroundColor(contexto.resources.getColor(R.color.green))
        } else {
            holder.disponible.setBackgroundColor(contexto.resources.getColor(R.color.red))
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
            AlertDialog.Builder(contexto)
                .setTitle("Borrar")
                .setMessage("¿Estás seguro de que quieres borrar " + item_actual.nombre!!.uppercase() + "?")
                .setPositiveButton("Sí") { _, _ ->
                    val carta = lista_filtrada[position]
                    FirebaseDatabase.getInstance().getReference("Tienda/Cartas/${carta.id}")
                        .removeValue()
                    lista_filtrada.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(contexto, "Carta borrada con éxito", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
            true
        }


    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val busqueda = p0.toString().lowercase()
                if (busqueda.isEmpty()) {
                    lista_filtrada = lista_cartas
                } else {
                    lista_filtrada = (lista_cartas.filter {
                        it.nombre.toString().lowercase().contains(busqueda)
                    }) as MutableList<Carta>
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
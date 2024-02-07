package com.example.practicafinal

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class EventoAdaptador(private var lista_eventos: MutableList<Evento>) :
    RecyclerView.Adapter<EventoAdaptador.EventoViewHolder>(), Filterable {
    private lateinit var contexto: Context
    private var lista_filtrada = lista_eventos


    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombre)
        val fecha: TextView = itemView.findViewById(R.id.fecha)
        val precio: TextView = itemView.findViewById(R.id.precio)
        val aforo: TextView = itemView.findViewById(R.id.aforo_actual)
        val aforomax: TextView = itemView.findViewById(R.id.aforo_max)
        val disponible = itemView.findViewById<View>(R.id.disponible)
        val img = itemView.findViewById<ImageView>(R.id.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): EventoViewHolder {
        val vista_item =
            LayoutInflater.from(parent.context).inflate(R.layout.item_evento, parent, false)
        contexto = parent.context
        return EventoViewHolder(vista_item)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val item_actual = lista_filtrada[position]
        holder.nombre.text = item_actual.nombre
        holder.fecha.text = item_actual.fecha
        holder.precio.text = item_actual.precio.toString()
        holder.aforo.text = item_actual.aforoactual.toString()
        holder.aforomax.text = item_actual.aforomax.toString()

        if (item_actual.aforoactual <= item_actual.aforomax.toString().toDouble()/2!!) {
            holder.disponible.background = contexto.getDrawable(R.color.green)
        } else if (item_actual.aforoactual.toString().toDouble() == item_actual.aforomax.toString().toDouble()) {
            holder.disponible.background = contexto.getDrawable(R.color.red)
        }else{
            holder.disponible.background = contexto.getDrawable(R.color.orange)
        }

        val URL: String? = when (item_actual.imagen) {
            "" -> null
            else -> item_actual.imagen
        }

        Glide.with(contexto)
            .load(URL)
            .apply(Utilidades.opcionesGlide(contexto))
            .transition(Utilidades.transicion)
            .into(holder.img)


        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(contexto)
                .setTitle("Borrar")
                .setMessage("¿Estás seguro de que quieres borrar " + item_actual.nombre!!.uppercase() + "?")
                .setPositiveButton("Sí") { _, _ ->
                    val evento = lista_filtrada[position]
                    FirebaseDatabase.getInstance().getReference("Tienda/Eventos/${evento.id}")
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

    override fun getItemCount(): Int = lista_filtrada.size
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val busqueda = p0.toString().lowercase()
                if (busqueda.isEmpty()) {
                    lista_filtrada = lista_eventos
                } else {
                    lista_filtrada = (lista_eventos.filter {
                        it.nombre.toString().lowercase().contains(busqueda)
                    }) as MutableList<Evento>
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
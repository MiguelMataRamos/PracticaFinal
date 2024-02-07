package com.example.practicafinal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventoAdaptador(private var lista_eventos: MutableList<Evento>) :
    RecyclerView.Adapter<EventoAdaptador.EventoViewHolder>() {
    private lateinit var contexto: Context
    private var lista_filtrada = lista_eventos


    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombre)
        val fecha: TextView = itemView.findViewById(R.id.fecha)
        val precio: TextView = itemView.findViewById(R.id.precio)
        val aforo: TextView = itemView.findViewById(R.id.aforo_actual)
        val aforomax: TextView = itemView.findViewById(R.id.aforo_max)
        val disponible = itemView.findViewById<View>(R.id.disponible)
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
    }

    override fun getItemCount(): Int = lista_filtrada.size
}
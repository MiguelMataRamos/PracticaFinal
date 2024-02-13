package com.example.practicafinal

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PedidoAdaptador(private var lista_pedidos: MutableList<Pedido>) :
    RecyclerView.Adapter<PedidoAdaptador.PedidoViewHolder>() {
    private lateinit var contexto: Context
    private var lista_filtrada = lista_pedidos

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id_pedido: TextView = itemView.findViewById(R.id.id_pedido)
        val id_carta: TextView = itemView.findViewById(R.id.id_carta)
        val id_cliente: TextView = itemView.findViewById(R.id.id_cliente)
        val precio: TextView = itemView.findViewById(R.id.precio)
        val nombre_carta: TextView = itemView.findViewById(R.id.nombre_carta)
        val fecha: TextView = itemView.findViewById(R.id.fecha)
        val vender: Button = itemView.findViewById(R.id.vender)
        val denegar: Button = itemView.findViewById(R.id.denegar)
        val desplegable: View = itemView.findViewById(R.id.desplegable)
        val btn_desplegar: ImageView = itemView.findViewById(R.id.boton_desplegable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val vista_item =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false)
        contexto = parent.context
        return PedidoViewHolder(vista_item)
    }

    override fun getItemCount() = lista_filtrada.size

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val item_actual = lista_filtrada[position]
        holder.id_pedido.text = item_actual.id
        holder.id_carta.text = item_actual.idcarta
        holder.id_cliente.text = item_actual.idusuario
        holder.precio.text = item_actual.precio.toString()
        holder.nombre_carta.text = item_actual.nombrecarta
        holder.fecha.text = item_actual.fecha

        holder.btn_desplegar.setOnClickListener {
            if (holder.desplegable.visibility == View.VISIBLE) {
                holder.desplegable.visibility = View.GONE
                holder.btn_desplegar.setImageResource(R.drawable.abrir)
            } else {
                holder.desplegable.visibility = View.VISIBLE
                holder.btn_desplegar.setImageResource(R.drawable.cerrar)
            }
        }

        holder.vender.setOnClickListener {
            AlertDialog.Builder(contexto)
                .setTitle("Vender pedido")
                .setMessage("¿Estás seguro de que quieres vender este pedido?. La carta se pondra como no disponible.")
                .setPositiveButton("Sí") { dialog, which ->
                    Utilidades.venderPedido(contexto, item_actual)
                    holder.desplegable.visibility = View.GONE
                    holder.btn_desplegar.setImageResource(R.drawable.abrir)
                }
                .setNegativeButton("No") { dialog, which -> }
                .show()
        }

        holder.denegar.setOnClickListener {
            AlertDialog.Builder(contexto)
                .setTitle("Denegar pedido")
                .setMessage("¿Estás seguro de que quieres denegar este pedido?. La carta se pondra como disponible para todos los usuarios.")
                .setPositiveButton("Sí") { dialog, which ->
                    Utilidades.denegarPedido(contexto, item_actual)
                    holder.desplegable.visibility = View.GONE
                    holder.btn_desplegar.setImageResource(R.drawable.abrir)
                }
                .setNegativeButton("No") { dialog, which -> }
                .show()
        }

    }


}
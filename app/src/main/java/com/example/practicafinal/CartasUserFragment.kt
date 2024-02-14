package com.example.practicafinal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practicafinal.databinding.FragmentCartasBinding
import com.example.practicafinal.databinding.FragmentCartasUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartasUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartasUserFragment : Fragment() {
    private lateinit var bind: FragmentCartasUserBinding
    private lateinit var lista: MutableList<Carta>
    private lateinit var db_ref: DatabaseReference
    private lateinit var recycler: RecyclerView
    private lateinit var adaptador: CartaAdaptador

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        bind = FragmentCartasUserBinding.inflate(layoutInflater)
        db_ref = FirebaseDatabase.getInstance().reference
        lista = mutableListOf()

        //se coge la lista de cartas que pertenecen al usuario actual
        //recorre cada pedido y si la id del cliente es igual a la del usriaio actual se añade a la lista de cartas dicha carta la cual se cogera de la base de datos
        db_ref.child("Tienda").child("Pedidos").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                snapshot.children.forEach { hijo: DataSnapshot? ->
                    //comprueba que el pedido pertenece al usuario actual
                    if (hijo!!.child("idusuario").value.toString() == FirebaseAuth.getInstance().uid.toString()) {
                        //se coge la carta del pedido
                        var cartas = db_ref.child("Tienda").child("Cartas")
                        cartas.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                //se coge la carta y se añade a la lista
                                var carta = snapshot.getValue(Carta::class.java)
                                if (carta != null) {
                                    Log.v("CARTA", carta.toString())
                                    lista.add(carta)
                                    adaptador.notifyDataSetChanged()
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("ERROR", error.message)
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })

        //se crea el adaptador y se le pasa la lista de productos
        adaptador = CartaAdaptador(lista)
        //se le pasa el adaptador al recycler
        recycler = bind.scCartas
        recycler.adapter = adaptador
        //se le pasa el layout manager
        recycler.layoutManager = LinearLayoutManager(applicationContext@ context)
        //se le dice que el tamaño del recycler no cambiara
        recycler.setHasFixedSize(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return bind.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartasUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartasUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
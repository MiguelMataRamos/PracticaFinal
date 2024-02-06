package com.example.practicafinal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practicafinal.databinding.ActivityAdministradorBinding
import com.example.practicafinal.databinding.FragmentAdminCartasBinding
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
 * Use the [AdminCartasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminCartasFragment : Fragment() {
    private lateinit var bind: FragmentAdminCartasBinding
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
        bind = FragmentAdminCartasBinding.inflate(layoutInflater)

        db_ref = FirebaseDatabase.getInstance().reference
        lista = mutableListOf()

        //se coge la lista de productos de la base de datos
        db_ref.child("Tienda").child("Cartas").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                snapshot.children.forEach { hijo: DataSnapshot? ->
                    val pojocarta = hijo!!.getValue(Carta::class.java)
                    lista.add(pojocarta!!)
                }
                recycler.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
        })

        //se crea el adaptador y se le pasa la lista de productos
        adaptador = CartaAdaptador(lista)
        //se le pasa el adaptador al recycler
        recycler = bind.scCartas
        recycler.adapter = adaptador
        //se le pasa el layout manager
        recycler.layoutManager = LinearLayoutManager(applicationContext@context)
        //se le dice que el tamaño del recycler no cambiara
        recycler.setHasFixedSize(true)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment with binding
        return bind.root


    }

    override fun onStart() {
        super.onStart()
        bind.btnAgregarCarta.setOnClickListener {
            val fragment = AddCartaFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartasAdminFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminCartasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
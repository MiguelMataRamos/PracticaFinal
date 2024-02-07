package com.example.practicafinal

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.practicafinal.databinding.FragmentAddEventoBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddEventoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEventoFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var db_ref: DatabaseReference
    private lateinit var bind: FragmentAddEventoBinding
    private val urlimg: String? = null


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        bind = FragmentAddEventoBinding.inflate(layoutInflater)
        db_ref = FirebaseDatabase.getInstance().reference

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return bind.root
    }

    override fun onStart() {
        super.onStart()

        bind.card.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

        bind.btnCrear.setOnClickListener {
            if (validar()){
                var id_generado: String? = db_ref.child("Tienda").child("Eventos").push().key
                var nombre = bind.etNombre.text.toString()
                var precio = bind.etPrecio.text.toString()
                var fecha = bind.fecha.text.toString()
                var disponible = bind.chkDisponible.isChecked
                launch {
                    var urlimgfirebase = Utilidades.guardarImagenCarta(id_generado!!, urlimg!!)
                    var nuevacarta = Carta(id_generado, nombre, categoria, precio,disponible, urlimgfirebase)

                    Utilidades.subirCarta(nuevacarta)
                }

                Toast.makeText(requireContext(), "Carta guardada con exito", Toast.LENGTH_SHORT).show()
                limpiar()
            }
        }
    }

    private fun validar(): Boolean {


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddEventoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddEventoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
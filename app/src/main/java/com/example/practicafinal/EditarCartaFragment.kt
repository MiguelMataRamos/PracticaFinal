package com.example.practicafinal

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.practicafinal.databinding.FragmentEditarCartaBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditarCartaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarCartaFragment : Fragment() {
    lateinit var bind: FragmentEditarCartaBinding


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentEditarCartaBinding.inflate(layoutInflater)

        // Obtener los datos del Bundle

        val nombre = arguments?.getString("nombre")
        val precio = arguments?.getString("precio")
        val categoria = arguments?.getString("categoria")
        val imagen = arguments?.getString("imagen")
        val disponible = arguments?.getBoolean("disponible")

        Log.i("Categoria", categoria.toString())

        val categorias = arrayOf("Selecciona categoria", "Blanco", "Negro", "Azul", "Verde")
        val adapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, categorias)
        bind.spCat.adapter = adapter

        // Encontrar el índice de la categoría en el array de categorías
        val index = categorias.indexOf(categoria)

        // Establecer la categoría como la opción seleccionada en el Spinner
        bind.spCat.setSelection(index)


        // Usar los datos obtenidos...
        bind.etNombre.setText(nombre)
        bind.etPrecio.setText(precio)
        bind.chkDisponible.isChecked = disponible!!
        bind.imgEditar.setImageURI(imagen!!.toUri())

        Glide.with(requireContext())
            .load(imagen)
            .apply(Utilidades.opcionesGlide(requireContext()))
            .transition(Utilidades.transicion)
            .into(bind.imgEditar)


        return bind.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditarCartaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditarCartaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
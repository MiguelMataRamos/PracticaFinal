package com.example.practicafinal

import android.R
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.practicafinal.databinding.FragmentAddCartaBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddCartaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCartaFragment : Fragment() {
    private lateinit var bind: FragmentAddCartaBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        bind = FragmentAddCartaBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //infla el spinner con las categorias
        val categorias = arrayOf("Selecciona categoria", "Blanco", "Negro", "Azul", "Verde")
        val adapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, categorias)
        bind.spCat.adapter = adapter


        // Inflate the layout for this fragment
        return bind.root
    }

    fun validar():Boolean{
        var nombre = true
        var precio = true
        var categoria = true
        var foto = true

        if (bind.etNombre.text.isNullOrBlank()){
            bind.tilNombre.error = "La carta debe tener un nombre"
            nombre = false
        }else{
            bind.tilNombre.error = null
            nombre = true
        }

        if (bind.etPrecio.text.isNullOrBlank() || bind.etPrecio.text.toString().toDouble() < 0){
            bind.tilPrecio.error = "La carta debe tener un precio"
            precio = false
        }else{
            bind.tilPrecio.error = null
            precio = true
        }

        if (bind.spCat.selectedItemPosition == 0){
            Toast.makeText(requireContext(), "Selecciona una categoria", Toast.LENGTH_SHORT).show()
            categoria = false
        }else{
            categoria = true
        }



        return nombre && precio && categoria

    }






    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddCartaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddCartaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
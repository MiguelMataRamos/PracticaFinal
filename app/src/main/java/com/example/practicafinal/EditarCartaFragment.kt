package com.example.practicafinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
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

        // Usar los datos obtenidos...
        bind.etNombre.setText(nombre)
        bind.etPrecio.setText(precio)
        bind.chkDisponible.isChecked = disponible!!
        bind.img.setImageURI(imagen!!.toUri())


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
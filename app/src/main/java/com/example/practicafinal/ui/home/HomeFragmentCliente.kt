package com.example.practicafinal.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practicafinal.R

class HomeFragmentCliente : Fragment() {

    companion object {
        fun newInstance() = HomeFragmentCliente()
    }

    private lateinit var viewModel: HomeFragmentClienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_fragment_cliente, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeFragmentClienteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
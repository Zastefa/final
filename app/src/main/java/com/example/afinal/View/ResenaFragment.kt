package com.example.afinal.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.afinal.databinding.FragmentResaBinding

class ResenaFragment : Fragment() {

    private var _binding: FragmentResaBinding? = null
    private val binding get() = _binding!! // Propiedad para acceder al binding de forma segura

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aquí puedes inicializar tus vistas y configurar el RecyclerView, etc.
        // Por ejemplo:
        // binding.recyclerView.layoutManager = LinearLayoutManager(context)
        // binding.recyclerView.adapter = tuAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpiar el binding para evitar fugas de memoria
    }
}
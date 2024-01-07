package com.example.house.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.house.R
import com.example.house.databinding.FragmentProfileBinding

class profileFragment : Fragment() {

    // Declare the binding object
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using data binding
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Access the views using the binding object in onViewCreated or later
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val view = binding.floatingButton
        view.setOnClickListener {

            findNavController().navigate(R.id.action_profileFragment_to_newProductFragment2)

        }}
}



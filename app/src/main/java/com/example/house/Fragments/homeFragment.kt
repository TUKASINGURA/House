package com.example.house.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.house.Chating.ChatActivity
import com.example.house.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
private lateinit var binding: FragmentHomeBinding
    // Declare the binding object
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using data binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    // Access the views using the binding object in onViewCreated or later
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.texthome4.setOnClickListener{
            startActivity( Intent(activity, ChatActivity::class.java) )
        }
            }
        }


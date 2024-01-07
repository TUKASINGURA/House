package com.example.house.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.house.Recyler.MyAdapter
import com.example.house.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class homeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: MyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Cloud Firestore
        db = FirebaseFirestore.getInstance()

        // Initialize RecyclerView
        val adapter = MyAdapter()
        val recyclerView = binding.ReyclerViewHome
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
//            // Get Cloud Firestore data
//            getData()
    }
//    private fun getData() {
//        db.collection("Products")
//            .orderBy("timestamp", Query.Direction.DESCENDING)
//            .addSnapshotListener { value, error ->
//                if (error != null) {
//                    // Handle error
//                } else {
//                    val dataList = value?.documents
//                    if (dataList != null) {
//                        val modelDataList = dataList.map { it.toObject(ModelClass::class.java) }
//                        adapter.setData(modelDataList)
//                    }
//                }
//            }
//    }}
}


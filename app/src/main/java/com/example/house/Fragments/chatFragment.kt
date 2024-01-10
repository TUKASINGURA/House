package com.example.house.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.house.DataModel.Message
import com.example.house.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class chatFragment : Fragment(){

    // Declare the binding object
    private lateinit var binding: FragmentChatBinding
    private lateinit var messagesAdapter: ArrayAdapter<String>
    private lateinit var userMessagesRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using data binding
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Access the views using the binding object in onViewCreated or later
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the messages adapter
        messagesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        binding.chatListView.adapter = messagesAdapter

        // Get a reference to the user's messages
        auth = FirebaseAuth.getInstance()
        //  userMessagesRef = FirebaseDatabase.getInstance().reference.child("messages").child(auth.currentUser?.uid ?: "")
        userMessagesRef = FirebaseDatabase.getInstance().reference.child("messages")

        // Set up the send button click listener
        binding.sendButton.setOnClickListener {
            val message = binding.messageInputEditText.text.toString()

            // Add the message to the user's messages
            val newMessageRef = userMessagesRef.push()
            newMessageRef.setValue(Message(message, auth.currentUser?.uid ?: "", System.currentTimeMillis()))

            // Clear the input field
            binding.messageInputEditText.text.clear()
        }

        // Set up the database listener
        userMessagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // Get the message value from the snapshot
                val message = snapshot.getValue(Message::class.java)

                // Add the message to the adapter
                if (message != null) {
                    //messagesAdapter.add("${message.senderId}: ${message.content}")
                    messagesAdapter.add(" ${message.content}")
                    messagesAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle message changes if needed
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Handle message removal if needed
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle message movement if needed
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if needed
            }
        })
    }
}

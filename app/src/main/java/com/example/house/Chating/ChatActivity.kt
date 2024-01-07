package com.example.house.Chating

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.house.databinding.ActivityChatBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var messagesAdapter: ArrayAdapter<String>
    private lateinit var database: DatabaseReference
    private lateinit var presenceRef: DatabaseReference
    private lateinit var connectedRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the messages adapter
       // messagesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        messagesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        binding.chatListView.adapter = messagesAdapter

        // Get a reference to the database
        database = Firebase.database.reference.child("messages")

        // Get a reference to the user's presence status
        auth = Firebase.auth
        presenceRef = Firebase.database.reference.child("users").child(auth.currentUser?.uid ?: "")

        // Get a reference to the connection status
        connectedRef = Firebase.database.reference.child(".info/connected")

        // Set up the send button click listener
        binding.sendButton.setOnClickListener {
            val message = binding.messageInputEditText.text.toString()

            // Add the message to the database
            val newMessageRef = database.push()
            newMessageRef.setValue(message)

            // Clear the input field
            binding.messageInputEditText.text.clear()
        }

        // Set up the database listener
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // Get the message value from the snapshot
                val message = snapshot.getValue(String::class.java)

                // Add the message to the adapter
                if (message != null) {
                    messagesAdapter.add(message)
                    messagesAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Not used in this tutorial
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Not used in this tutorial
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Not used in this tutorial
            }

            override fun onCancelled(error: DatabaseError) {
                // Not used in this tutorial
            }
        })

        // Set up the presence listener
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false

                if (connected) {
                    // User is online
                    presenceRef.setValue(true)
                    presenceRef.onDisconnect().setValue(false)
                } else {
                    // User is offline
                    presenceRef.setValue(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Not used in this tutorial
            }
        })

        // Set up the presence indicator
        val presenceRef = Firebase.database.reference.child("users").child(auth.currentUser?.uid ?: "")
        presenceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val online = snapshot.getValue(Boolean::class.java) ?: false

                if (online) {
                    // User is online
                   // binding.presenceImageView.setImageResource(R.drawable.ic_online)
                } else {
                    // User is offline
                   // binding.presenceImageView.setImageResource(R.drawable.ic_offline)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Not used in this tutorial
            }
        })
    }
}

package com.example.house.Fragments


import android.app.ProgressDialog
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.house.R
import com.example.house.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class signUpFragment<CheckBox> : Fragment() {
    private var userEmail: String = ""
    lateinit var db: DocumentReference
    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Access the views using the binding object in onViewCreated or later
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance().document("UserDetails/credentials")
        binding.signUpButton.setOnClickListener{
            signUpUser()
            store(userEmail)
        }
        binding.LoginButton.setOnClickListener{
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        val passwordEditText = binding.passwordEditText
        val passwordToggleCheckBox = binding.passwordToggleCheckBox
        passwordToggleCheckBox.setOnCheckedChangeListener { _, isChecked ->
            // Show or hide the password based on the CheckBox state
            val inputType = if (isChecked) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            passwordEditText.inputType = inputType
            // Move the cursor to the end of the text to update the inputType
            passwordEditText.text?.let { passwordEditText.setSelection(it.length) }
        }

    }

    private fun signUpUser() {
        val email: String = binding.emailEditText.text.toString().trim()
        val password: String = binding.passwordEditText.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            val builder = AlertDialog.Builder(requireContext())
            // Use HTML formatting to make the title bold
            val title = "<b>Notification Message</b>"
            builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
            builder.setMessage("Please enter the Email Address\nBecause you can't LOGIN when no email is Provided")
            builder.setPositiveButton("OKAY") { dialog, which ->
                dialog.dismiss()
                binding.emailEditText.requestFocus()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            val builder = AlertDialog.Builder(requireContext())
            val title = "<b>Notification Message</b>"
            builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
            builder.setMessage("Please enter the Password\nBecause the password field can't be Empty")
            builder.setPositiveButton("OKAY") { dialog, which ->
                dialog.dismiss()
                binding.passwordEditText.requestFocus()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
            return
        }
        // Set the user's email
        userEmail = email

        // Show progress dialog
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Signing Up")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        // Create a new user in Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                requireActivity()
            ) { task ->
                // Dismiss progress dialog when the task is complete
                progressDialog.dismiss()

                if (task.isSuccessful) {
                    FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                    FirebaseAuth.getInstance().signOut()
                    findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                } else {
                    // If sign up fails, display a message to the user.
                    val builder = AlertDialog.Builder(requireContext())
                    val title = "<b>Notification Message</b>"
                    builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
                    builder.setMessage("Failed to SIGN UP\nThis could be due to the wrong details used\nOr Weak Password used\nOr Your Email is Already registered")
                    builder.setPositiveButton("OKAY") { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.setNegativeButton("LOGIN") { dialog, which ->
                        dialog.dismiss()
                        findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
    }

    private fun store(userEmail: String) {
        val userNameLogin = binding.usernameLogin.text.toString().trim()
        val contactLogin = binding.contactLogin.text.toString().trim()
        if (TextUtils.isEmpty(userNameLogin)) {
            val builder = AlertDialog.Builder(requireContext())
            val title = "<b>Notification Message</b>"
            builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
            builder.setMessage("Please enter the UserName\nBecause UserName Can't be Empty")
            builder.setPositiveButton("OKAY") { dialog, which ->
                dialog.dismiss()
                binding.usernameLogin.requestFocus()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()//    lateinit var db: FirebaseFirestore
            return
        }
        if (TextUtils.isEmpty(contactLogin)) {
            val builder = AlertDialog.Builder(requireContext())
            val title = "<b>Notification Message</b>"
            builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
            builder.setMessage("Please Fill In the Contact")
            builder.setPositiveButton("OKAY") { dialog, which ->
                dialog.dismiss()
                binding.contactLogin.requestFocus()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()//    lateinit var db: FirebaseFirestore
            return
        }
        val items = hashMapOf(
            "UserNameLogin" to userNameLogin,
            "ContactLogin" to contactLogin,
            "UserEmail" to userEmail
        )

        db.collection("User_Details")
            .add(items)
            .addOnSuccessListener { void: DocumentReference? ->
// text to be displayed on the screen
            }.addOnFailureListener { exception: java.lang.Exception ->
                Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_LONG).show()
            }
    }}

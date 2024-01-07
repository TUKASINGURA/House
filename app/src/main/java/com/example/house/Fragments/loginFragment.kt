package com.example.house.Fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.house.R
import com.example.house.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class loginFragment: Fragment() {
    private lateinit var progressDialog: ProgressDialog
    lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpButtonLogin.setOnClickListener{
            signUpUser()
        }
        binding.forgotPasswordLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
        val passwordEditText = binding.passwordEditTextLogin
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
        val email: String = binding.emailEditTextLogin.text.toString().trim()
        val password: String = binding.passwordEditTextLogin.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            val builder = AlertDialog.Builder(requireContext())
            val title = "<b>Notification Message</b>"
            builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
            builder.setMessage("Please enter the Email\nBecause you can not login when the Email is Empty")
            builder.setPositiveButton("OKAY") { dialog, which ->
                dialog.dismiss()
                binding.emailEditTextLogin.requestFocus()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()//    lateinit var db: FirebaseFirestore
            return
        }
        if (TextUtils.isEmpty(password)) {
            val builder = AlertDialog.Builder(requireContext())
            // Use HTML formatting to make the title bold
            val title = "<b>Notification Message</b>"
            builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
            builder.setMessage("Please enter the Password\nBecause you can not login when the PASSWORD is not Provided")
            builder.setPositiveButton("OKAY") { dialog, which ->
                dialog.dismiss()
                binding.passwordEditTextLogin.requestFocus()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
            return
        }
        // Show progress dialog
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Logging IN")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        // Create a new user in Firebase Authentication
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                requireActivity()
            ) { task ->
                // Dismiss progress dialog when the task is complete
                progressDialog.dismiss()

                if (task.isSuccessful) {
                    if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                        val builder = AlertDialog.Builder(requireContext())
                        val title = "<b>Notification Message</b>"
                        builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
                        builder.setMessage("Login failed! \nMake sure you verify your email? Before Logging in")
                        builder.setPositiveButton("OKAY") { dialog, which ->
                            dialog.dismiss()
                        }
                        val dialog = builder.create()
                        dialog.show()

                    }
                } else {
                    // If login fails, display a message to the user.
                    val builder = AlertDialog.Builder(requireContext())
                    val title = "<b>Notification Message</b>"
                    builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
                    builder.setMessage("Failed to LOGIN\nThis could be Due to the wrong details used\nOr the You Haven't registered")
                    builder.setPositiveButton("OKAY") { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.setNegativeButton("SIGN UP") { dialog, which ->
                        dialog.dismiss()
                        findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
    }

}


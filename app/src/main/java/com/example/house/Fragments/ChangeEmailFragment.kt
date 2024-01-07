package com.example.firebaseemailchange

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.house.databinding.FragmentChangeEmailBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangeEmailFragment : Fragment() {
    private lateinit var binding: FragmentChangeEmailBinding
    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using data binding
        binding = FragmentChangeEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changeEmailButton.setOnClickListener {
            changeEmail()
        }
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage("Changing email...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    private fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    private fun changeEmail() {
        val currentEmail = binding.currentEmailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val newEmail = binding.newEmailEditText.text.toString()

        // Check if email is empty
        if (currentEmail.isEmpty() || !isValidEmail(currentEmail)) {
            showToast("Invalid current email address")
            return
        }

        // Check if password is empty
        if (password.isEmpty()) {
            showToast("Password cannot be empty")
            return
        }

        // Check if new email is empty or has incorrect format
        if (newEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            showToast("Invalid new email address")
            return
        }

        val user = FirebaseAuth.getInstance().currentUser

        // Show progress dialog
        showProgressDialog()

        // Sign in the user
        FirebaseAuth.getInstance().signInWithEmailAndPassword(currentEmail, password)
            .addOnCompleteListener { signInTask ->
                // Hide progress dialog
                hideProgressDialog()

                if (signInTask.isSuccessful) {
                    // Re-authenticate the user (optional)
                    val credential = EmailAuthProvider.getCredential(currentEmail, password)
                    user?.reauthenticate(credential)
                        ?.addOnCompleteListener { reauthenticateTask ->
                            if (reauthenticateTask.isSuccessful) {
                                // Update the email
                                user.updateEmail(newEmail)
                                    .addOnCompleteListener { updateEmailTask ->
                                        // Hide progress dialog
                                        hideProgressDialog()

                                        if (updateEmailTask.isSuccessful) {
                                            // Email updated successfully
                                            showToast("Email Updated Successfully")
                                        } else {
                                            // Handle email update errors
                                            val exception = updateEmailTask.exception
                                            exception?.let {
                                                showToast("Failed to Update the email: ${it.message}")
                                                Log.e("ChangeEmailFragment", "Failed to update email", it)
                                            }
                                        }
                                    }
                            } else {
                                // Handle re-authentication errors
                                val exception = reauthenticateTask.exception
                                exception?.let {
                                    showToast("Re-authentication failed: ${it.message}")
                                    Log.e("ChangeEmailFragment", "Re-authentication failed", it)
                                }
                            }
                        }
                } else {
                    // Handle sign-in errors
                    val exception = signInTask.exception
                    exception?.let {
                        showToast("Sign-in failed: ${it.message}")
                        Log.e("ChangeEmailFragment", "Sign-in failed", it)
                    }
                }
            }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$"
        return email.trim().matches(emailRegex.toRegex())
    }

//    private fun isValidEmail(email: String): Boolean {
//        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
//        return email.trim().matches(emailRegex.toRegex())
//    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}

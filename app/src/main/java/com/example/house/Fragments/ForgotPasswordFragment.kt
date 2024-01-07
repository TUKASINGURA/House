import android.app.ProgressDialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.house.R
import com.example.house.databinding.FragmentForgotPasswordBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
class ForgotPasswordFragment : Fragment() {
    private lateinit var emailEditText: EditText
    private lateinit var resetButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    // Declare the binding object
    private lateinit var binding: FragmentForgotPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using data binding
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }
    // Access the views using the binding object in onViewCreated or later
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEditText = binding.editTextEmail
        resetButton = binding.buttonReset
        mAuth = FirebaseAuth.getInstance()

        resetButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                val builder = AlertDialog.Builder(requireContext())
                // Use HTML formatting to make the title bold
                val title = "<b>Notification Message</b>"
                builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
                builder.setMessage("Please enter the Email Address\nTo reset your Password")
                builder.setPositiveButton("OKAY") { dialog, which ->
                    dialog.dismiss()
                    binding.editTextEmail.requestFocus()
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                    findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                }
                val dialog = builder.create()
                dialog.show()
            } else {
                showProgressDialog()
                checkIfEmailExists(email)
            }
        }
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Resetting Password")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    private fun checkIfEmailExists(email: String) {
        mAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                hideProgressDialog() // Hide the progress dialog after checking
                if (task.isSuccessful) {
                    showProgressDialog()
                    resetPassword(email)
                }
            }
            .addOnFailureListener { exception ->
                val builder = AlertDialog.Builder(requireContext())
                // Use HTML formatting to make the title bold
                val title = "<b>Notification Message</b>"
                builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
                builder.setMessage("Error while checking email registration.\n\nEnsure the Email Proided is a correct Email\nPlease try again.")
                builder.setPositiveButton("OKAY") { dialog, which ->
                    dialog.dismiss()
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
    }
    private fun resetPassword(email: String) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task: Task<Void?> ->
                hideProgressDialog() // Hide the progress dialog after password reset attempt
                if (task.isSuccessful) {
                    val builder = AlertDialog.Builder(requireContext())
                    // Use HTML formatting to make the title bold
                    val title = "<b>Notification Message</b>"
                    builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
                    builder.setMessage("Password reset email has been sent.\nAnd Ensure the Email is the one you used to register\nCheck your email inbox and email spam.")
                    builder.setPositiveButton("OKAY") { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                } else {
                    val builder = AlertDialog.Builder(requireContext())
                    // Use HTML formatting to make the title bold
                    val title = "<b>Notification Message</b>"
                    builder.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY))
                    builder.setMessage("Failed to send password reset email. \nCheck your email and try again.")
                    builder.setPositiveButton("OKAY") { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
    }
}

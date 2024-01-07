package com.example.house.Fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.house.databinding.FragmentNewProductBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage


class newProductFragment : Fragment() {
    lateinit var db: DocumentReference
    lateinit var imageView: ImageView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var binding: FragmentNewProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings
        imageView = binding.productDescriptionImage
        button = binding.addImage
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        db = FirebaseFirestore.getInstance().document("Customer/Products")

        binding.submitItem.setOnClickListener {
            store()
        }
    }

    private fun store() {
        val productDescription = binding.productDescription.toString().trim()
        val productContact = binding.ProductcontactNumber.toString().trim()
        val price = binding.thePriceOfProduct.toString().trim()
        val location = binding.ProductLocation.toString().trim()

        if (!productDescription.isEmpty() && !productContact.isEmpty() && !price.isEmpty() && !location.isEmpty()) {
            try {
                val storage = FirebaseStorage.getInstance().reference
                if (imageUri != null) {
                    val fileReference = storage.child("ProductImages").child(imageUri?.lastPathSegment ?: "default_image.jpg")
                    fileReference.putFile(imageUri!!).addOnSuccessListener {
                        val items = hashMapOf(
                            "ProductDescription" to productDescription,
                            "ProductContact" to productContact,
                            "Price" to price,
                            "Location" to location,
                            "ProductImageName" to imageUri?.lastPathSegment
//                            "ProductImageUrl" to it.metadata!!.reference!!.downloadUrl.toString()
                        )

                        db.collection("Products")
                            .add(items)
                            .addOnSuccessListener {
                                    void: DocumentReference? -> Toast.makeText(requireContext(), "Successfully uploaded to the database :)", Toast.LENGTH_LONG).show()
                            }.addOnFailureListener {
                                    exception: java.lang.Exception -> Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_LONG).show()
                            }
                    }.addOnFailureListener {
                            exception: Exception -> Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Please select an image.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please fill up the fields :(", Toast.LENGTH_LONG).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }
}

//class newProductFragment : Fragment() {
//    lateinit var db: FirebaseFirestore
//    lateinit var imageView: ImageView
//    lateinit var button: Button
//    private val pickImage = 100
//    private var imageUri: Uri? = null
//    lateinit var binding: FragmentNewProductBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout using data binding
//        binding = FragmentNewProductBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    // Access the views using the binding object in onViewCreated or later
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Function for handling the image from the files
//        imageView = binding.productDescriptionImage
//        button = binding.addImage
//        button.setOnClickListener {
//            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            startActivityForResult(gallery, pickImage)
//        }
//
//        // Firestore
//        db = FirebaseFirestore.getInstance()
//
//        binding.submitItem.setOnClickListener {
//            store()
//        }
//    }
//    private fun store() {
//        val productDescription = binding.productDescription.text.toString().trim()
//        val productContact = binding.ProductcontactNumber.text.toString().trim()
//        val price = binding.thePriceOfProduct.text.toString().trim()
//        val location = binding.ProductLocation.text.toString().trim()
//
//        if (!productDescription.isEmpty() && !productContact.isEmpty() && !price.isEmpty() && !location.isEmpty()) {
//            try {
//                // Upload the image to Cloud Storage and get the download URL
//                if (imageUri != null) {
//                    uploadImageToStorage(productDescription, productContact, price, location)
//                } else {
//                    // If no image selected, add other details to Firestore directly
//                    addToFirestore(productDescription, productContact, price, location, null)
//                }
//            } catch (e: Exception) {
//                Log.e("StoreFunction", "Error: ${e.message}", e)
//                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
//            }
//        } else {
//            Toast.makeText(requireContext(), "Please fill up the fields :(", Toast.LENGTH_LONG).show()
//        }
//    }

//    private fun uploadImageToStorage(
//        productDescription: String,
//        productContact: String,
//        price: String,
//        location: String
//    ) {
//
//        val storageRef = FirebaseStorage.getInstance().reference
//        val imageRef = storageRef.child("product_images/${System.currentTimeMillis()}_image.jpg")
//
//        imageRef.putFile(imageUri!!)
//            .addOnSuccessListener {
//                // Image uploaded successfully, get the download URL
//                imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
//                    // Add other details and the image URL to Firestore
//                    addToFirestore(productDescription, productContact, price, location, imageUrl.toString())
//                }
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_LONG).show()
//            }
//    }
//
//    private fun addToFirestore(
//        productDescription: String,
//        productContact: String,
//        price: String,
//        location: String,
//        imageUrl: String?
//    ) {
//        val items = hashMapOf(
//            "ProductDescription" to productDescription,
//            "ProductContact" to productContact,
//            "Price" to price,
//            "Location" to location,
//            "ProductImage" to imageUrl
//        )
//
//        // Add the items to Firestore
//        db.collection("Products")
//            .add(items)
//            .addOnSuccessListener { void: DocumentReference? ->
//                Toast.makeText(requireContext(), "Successfully uploaded to the database :)", Toast.LENGTH_LONG).show()
//            }
//            .addOnFailureListener { exception: java.lang.Exception ->
//                Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_LONG).show()
//            }
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
//            imageUri = data?.data
//            imageView.setImageURI(imageUri)
//        }
//    }
//}

//import android.app.Activity.RESULT_OK
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.Toast
//import com.example.house.databinding.FragmentNewProductBinding
//import com.google.firebase.firestore.DocumentReference
//import com.google.firebase.firestore.FirebaseFirestore
//
//class newProductFragment : Fragment() {
//    lateinit var db: DocumentReference
//    lateinit var imageView: ImageView
//    lateinit var button: Button
//    private val pickImage = 100
//    private var imageUri: Uri? = null
//    lateinit var binding: FragmentNewProductBinding
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout using data binding
//        binding = FragmentNewProductBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    // Access the views using the binding object in onViewCreated or later
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        //fuction for handling the image from the files
//        imageView= binding.productDescriptionImage
//        button= binding.addImage
//        button.setOnClickListener {
//            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            startActivityForResult(gallery, pickImage)
//        }
//
////handling the firestore
//        db = FirebaseFirestore.getInstance().document("Customer/Products")
//
//        binding.submitItem.setOnClickListener {
//                view: View? -> store ()
//    }
//
////end of functionality of the function that is handling the image from the files
//    }
//private fun store () {
//    val productImage= binding.productDescriptionImage
//    val productDescription= binding.productDescription.toString().trim()
//    val productContact= binding.ProductcontactNumber.toString().trim()
//    val price= binding.thePriceOfProduct.toString().trim()
//    val location= binding.ProductLocation.toString().trim()
//
//    if (!productDescription.isEmpty() && !productContact.isEmpty() && !price.isEmpty()&&!location.isEmpty()) {
//        try {
//
//                 val items = hashMapOf(
////                "ProductImage" to productImage,
//                "ProductDescription" to productDescription,
//                "ProductContact" to productContact,
//                //items.put("gender", gender)
//                "Price" to price,
//                "Location" to location
//            )
//
//            db.collection("Products")
//                //.document(items.toString())
//                //.set(items)
//                .add(items)
//                .addOnSuccessListener {
//                        void: DocumentReference? -> Toast.makeText(requireContext(), "Successfully uploaded to the database :)", Toast.LENGTH_LONG).show()
//                }.addOnFailureListener {
//                        exception: java.lang.Exception -> Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_LONG).show()
//                }
//        }catch (e:Exception) {
//            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
//        }
//    }else {
//        Toast.makeText(requireContext(), "Please fill up the fields :(", Toast.LENGTH_LONG).show()
//    }
//        }
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode == pickImage) {
//            imageUri = data?.data
//            imageView.setImageURI(imageUri)
//        }
//    }
//}




//val storage = FirebaseStorage.getInstance().reference
//
//val productImageName = product.get("ProductImageName") as String
//
//val productImageReference = storage.child("ProductImages").child(productImageName)
//
//val productImageUrl = productImageReference.downloadUrl.toString()
//
//In this example, product represents a single product retrieved from the Firebase Firestore database.
//The code first retrieves the image filename from the product data, then generates the full image URL using the Firebase Storage downloadUrl property.
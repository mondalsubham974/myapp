package com.example.myapplication

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.auth.viewmodel.RequestCodes
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : Fragment() {

    var usersRefference: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    private val RequestCode = 438
    private var imageUri: Uri? = null
    private var storageRef: StorageReference? = null
    private var coverChecker: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refUser =
            FirebaseDatabase.getInstance().reference.child("User").child(firebaseUser!!.uid)


        refUser.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user:Users? = p0.getValue(Users::class.java)
                    if (context != null){
                        view.settings_username.text = user!!.username
                        Picasso.get().load(user.profile).placeholder(R.drawable.blank_profile_picture).into(view.settings_profile)
                        Picasso.get().load(user.cover).placeholder(R.drawable.blank_profile_picture).into(view.settings_cover)
                    }

                }
            }

        })
        view.settings_profile.setOnClickListener {
            pickImage()
        }
        view.settings_cover.setOnClickListener {
            coverChecker = "cover"
            pickImage()
        }
        return view
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,RequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK && data?.data != null){
            imageUri = data.data
            Toast.makeText(context, "Uploading...",Toast.LENGTH_LONG).show()
            UploadImageToDatabase()
        }
    }

    private fun UploadImageToDatabase() {
        val progressBar = ProgressDialog(context)
        progressBar.setMessage("image is uploading, please wait....")
        progressBar.show()

        if (imageUri != null){
            val fileRef = storageRef!!.child(System.currentTimeMillis().toString()+".jpg")

            val uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful){
                    task.exception.let {
                        throw it!!
                    }

                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val downloadUri = task.result
                    val mUri = downloadUri.toString()

                    if (coverChecker == "cover"){
                        val mapCoverImg = HashMap<String, Any>()
                        mapCoverImg["cover"]
                        usersRefference?.updateChildren(mapCoverImg)
                        coverChecker = ""
                    }
                    else{
                        val mapProfileImg = HashMap<String, Any>()
                        mapProfileImg["Profile"]
                        usersRefference?.updateChildren(mapProfileImg)
                        coverChecker = ""
                    }
                    progressBar.dismiss()
                }
            }
        }
    }

}
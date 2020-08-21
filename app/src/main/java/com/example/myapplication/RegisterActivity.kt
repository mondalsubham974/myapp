package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUser: DatabaseReference
    private var firebaseUserId: String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        register_button.setOnClickListener{
            RegisterUser()
        }
        register_textView.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun RegisterUser() {
        val email: String = register_email.text.toString()
        val username: String = register_username.text.toString()
        val password: String = register_password.text.toString()

        if(email == ""){
            Toast.makeText(this,"plese enter email",Toast.LENGTH_LONG).show()
        }
        if(username == ""){
            Toast.makeText(this,"plese enter username",Toast.LENGTH_LONG).show()
        }
        if(password == ""){
            Toast.makeText(this,"plese enter password",Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        firebaseUserId = mAuth.currentUser!!.uid
                        refUser = FirebaseDatabase.getInstance().reference.child("User").child(firebaseUserId)
                        val userHashMap = HashMap<String,Any>()
                        userHashMap["uid"] = firebaseUserId
                        userHashMap["username"] = username
                        userHashMap["search"] = username.toLowerCase()
                        userHashMap["status"] = ""
                        userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/kotha1.appspot.com/o/blank_profile_picture.png?alt=media&token=83baffd3-722b-48ae-9c71-1d21207182b2"
                        userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/kotha1.appspot.com/o/cover_profile_image.png?alt=media&token=4098ee58-d5e0-4c86-965e-705983178eac"
                        userHashMap["brithday"] = ""+"/"+""+"/"+""
                        userHashMap["hometown"] = "bankura"
                        userHashMap["country"] = "sent"
                        userHashMap["relegion"] = "sent"
                        userHashMap["Relationship"] = "sent"
                        userHashMap["Profesion"] = "sent"
                        userHashMap["online"] = "true"

                        refUser.updateChildren(userHashMap).addOnCompleteListener {task ->
                            if (task.isSuccessful){
                                val intent = Intent(this,MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                                finish()
                            }

                        }
                    }
                    else{
                        Toast.makeText(this,"Error Message:"+task.exception!!.message.toString()
                            ,Toast.LENGTH_LONG).show()
                    }

            }

        }
    }

}

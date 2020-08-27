package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_visit_profile.*
var userIdVisit:String?= ""
class VisitProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_profile)
        intent = intent
        userIdVisit = intent.getStringExtra("Visit_id")

        val refUser = FirebaseDatabase.getInstance().reference.child("User").child(userIdVisit.toString())


        refUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    val user = p0.getValue(Users::class.java)
                    settings_username.text = user?.username
                    Log.d("MainActivity", "user->${user?.username}")
                    Picasso.get().load(user?.profile).placeholder(R.drawable.blank_profile_picture).into(settings_profile)
                    Picasso.get().load(user?.cover).placeholder(R.drawable.cover_profile_image).into(settings_cover)
                }
            }

        })
    }
}
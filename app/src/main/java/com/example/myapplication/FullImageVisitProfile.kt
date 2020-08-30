package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_visit_profile.*
import kotlinx.android.synthetic.main.activity_visit_profile.*
var userIdVisit:String?= ""
class FullImageVisitProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_visit_profile)
        intent = intent
        userIdVisit = intent.getStringExtra("Visit_id")
        Log.d("msg","messages -> $userIdVisit")
        val refUser = FirebaseDatabase.getInstance().reference.child("Chats").child(userIdVisit.toString())
        Log.d("msg","refUser -> $refUser")

        refUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                    val user = p0.getValue(Users::class.java)
                    Picasso.get().load(user?.profile).placeholder(R.drawable.blank_profile_picture).into(full_image_visit_profile)
                    Log.d("msg", "profile -> ${user?.profile}")


            }

        })
    }
}
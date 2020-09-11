package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_image.*
import kotlinx.android.synthetic.main.activity_visit_profile.*
var messages:String?= ""
class FullImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)
        val rightimage: ImageView? = findViewById(R.id.message_right_image)
        val leftimage: ImageView? = findViewById(R.id.message_left_image)



        val refUser = FirebaseDatabase.getInstance().reference.child("Chats")
        Log.d("msgsss","messages -> $refUser")

        refUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

        }

            override fun onDataChange(p0: DataSnapshot) {
                val chat = p0.getValue(Chat::class.java)
                Log.d("chat","messages -> $chat")
                if (chat!!.message == "" && chat.url != "") {
                    Picasso.get().load(chat.url).into(rightimage)
                    Picasso.get().load(chat.url).into(leftimage)
                }
            }

        })
    }
}
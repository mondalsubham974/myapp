package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class FriendActivity : AppCompatActivity() {
    private var userAdapter: UserAdapter? = null
    private var mUsers: List<Users>? = null
    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? =null
    private var mCurrent_state:String = "not_friend"
    private var mProfileSendReqButton: Button? =null
    private var mProfilecanButton: Button? =null
    var firebaseUserId = FirebaseAuth.getInstance().currentUser
    private var mAuth = FirebaseAuth.getInstance()
    private var senderUserId:String?= mAuth.currentUser?.uid
    private var recevierUserId:String? = Intent().extras?.get("visit user id").toString()
    private var FriendRequestRef = FirebaseDatabase.getInstance().reference.child("FriendRequests")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        recyclerView = findViewById(R.id.search_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        searchEditText = findViewById(R.id.search_bar)
        mProfileSendReqButton = findViewById(R.id.add_friend)

        mUsers = ArrayList()
        retrieveAllUsers()


        mProfileSendReqButton?.setOnClickListener {

            sendFriendRequest()


        }

    }

    private fun retrieveAllUsers() {
        val firebaseUserId = FirebaseAuth.getInstance().currentUser
        val refUsers = FirebaseDatabase.getInstance().reference.child("User")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                if (searchEditText?.text.toString() == ""){
                    for (snapshot in p0.children){
                        val user: Users = snapshot.getValue(Users::class.java)!!
                        if (!(user.uid).equals(firebaseUserId)){
                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }

                }
                userAdapter = UserAdapter(this@FriendActivity,mUsers!!,false)
                recyclerView?.adapter = userAdapter
            }

        })

    }
    private fun sendFriendRequest() {
        FriendRequestRef.child(senderUserId!!).child(recevierUserId!!).child("request_type")
            .setValue("sent").addOnCompleteListener {task ->

            }



    }
}

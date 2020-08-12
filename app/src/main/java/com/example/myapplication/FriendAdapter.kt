package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class FriendAdapter(private var context: Context, private val friendList:List<String>,
                     private val isChatCheck:Boolean): RecyclerView.Adapter<RequestAdapter.ViewHolder?>() {
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RequestAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_request_item_layout, viewGroup, false)
        return RequestAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RequestAdapter.ViewHolder, i: Int) {
        val user = friendList[i]

        //TODO: I don't like using this method, but because of the way your data is stored in firebase this is the only way to get the user details.
        //TODO: you may need to change the backend firebase data to have a new class like  -->  ReceivedUser(userId, username, userLogo)
        val dbRef = FirebaseDatabase.getInstance().reference.child("User/$user")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val friendUser = snapshot.getValue(Users::class.java)
                holder.displayName.text = friendUser!!.username
                Picasso.get().load(friendUser.profile).placeholder(R.drawable.blank_profile_picture)
                    .into(holder.displayImage)
            }
        })
    }

}
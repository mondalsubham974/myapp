package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class FriendAdapter(private val mcontext: Context, private val friendList:List<String>,
                    private val isChatCheck:Boolean):RecyclerView.Adapter<FriendAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mcontext).inflate(R.layout.activity_friend_item_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val user = friendList[i]
        val dbRef = FirebaseDatabase.getInstance().reference.child("User/$user")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val confirmFriendUser = snapshot.getValue(Users::class.java)
                holder.usernameTxt.text = confirmFriendUser!!.username
                Picasso.get().load(confirmFriendUser.profile).placeholder(R.drawable.blank_profile_picture).into(holder.profileImageView)
            }
        })
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var usernameTxt: TextView = itemView.findViewById(R.id.activity_user_item_layout_username)
        var profileImageView: CircleImageView = itemView.findViewById(R.id.activity_user_item_layout_ProfileImage)
        var onlineImageView: CircleImageView = itemView.findViewById(R.id.activity_user_item_layout_online)
        var offlineImageView: CircleImageView = itemView.findViewById(R.id.activity_user_item_layout_offline)
        var lastMessageTxt: TextView = itemView.findViewById(R.id.activity_user_item_layout_lastMessage)
        var addFriendButton: Button = itemView.findViewById(R.id.add_friend)

    }
}
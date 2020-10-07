package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatListAdapter(private val mcontext: Context, private val chatfragmentList:List<String>,
                      private val isChatCheck:Boolean): RecyclerView.Adapter<ChatListAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mcontext).inflate(R.layout.chatlist, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatfragmentList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val user = chatfragmentList[i]
        val dbRef = FirebaseDatabase.getInstance().reference.child("User/$user")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val confirmFriendUser= snapshot.getValue(Users::class.java)
                holder.usernameTxt.text = confirmFriendUser!!.username
                Picasso.get().load(confirmFriendUser.profile)
                    .placeholder(R.drawable.blank_profile_picture).into(holder.profileImageView)
                holder.itemView.setOnClickListener {
                    val intent = Intent(mcontext,MessageChatActivity::class.java)

                    intent.putExtra("Visit_id",confirmFriendUser.uid)
                    mcontext.startActivity(intent)
                }
            }

        })
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var usernameTxt: TextView = itemView.findViewById(R.id.chatlist_username)
        var profileImageView: CircleImageView = itemView.findViewById(R.id.chatlist_ProfileImage)


    }


}
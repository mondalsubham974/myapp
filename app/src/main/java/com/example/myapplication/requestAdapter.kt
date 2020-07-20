package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class requestAdapter(private val mcontext: Context, private val mUsers:List<Users>,
    private val isChatCheck:Boolean): RecyclerView.Adapter<FriendAdapter.ViewHolder?>(){




    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FriendAdapter.ViewHolder {
        val view: View = LayoutInflater.from(mcontext).inflate(R.layout.activity_request_item_layout,viewGroup,false)
        return FriendAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: FriendAdapter.ViewHolder,i: Int) {
        val user: Users = mUsers[i]
        holder.usernameTxt.text = user.username
        Picasso.get().load(user.profile).placeholder(R.drawable.blank_profile_picture).into(holder.profileImageView)
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var usernameTxt: TextView = itemView.findViewById(R.id.activity_request_item_layout_username)
        var profileImageView: CircleImageView = itemView.findViewById(R.id.activity_request_item_layout_ProfileImage)
        var onlineImageView: CircleImageView = itemView.findViewById(R.id.activity_user_item_layout_online)
        var offlineImageView: CircleImageView = itemView.findViewById(R.id.activity_user_item_layout_offline)
        var lastMessageTxt: TextView = itemView.findViewById(R.id.activity_user_item_layout_lastMessage)
        var confirmFriendButton: Button = itemView.findViewById(R.id.activity_request_item_layout_confirm_friend)
        var cancleFriendButton: Button = itemView.findViewById(R.id.activity_request_item_layout_cancel_button)

    }

}
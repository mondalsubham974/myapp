package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val mcontext: Context, private val mUsers:List<Users>,
                  private val isChatCheck:Boolean):RecyclerView.Adapter<UserAdapter.ViewHolder?>()
{
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view: View = LayoutInflater.from(mcontext).inflate(R.layout.user_search_item_layout,viewGroup,false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, i: Int) {
        val user: Users = mUsers[i]
        holder.usernameTxt.text = user.username
        Picasso.get().load(user.profile).placeholder(R.drawable.blank_profile_picture).into(holder.profileImageView)
        checkAddFriendStatus(user.uid,holder.addFriendButton)
        if (!(firebaseUser)!!.equals(user.uid)){

            holder.addFriendButton.setOnClickListener{
                if (holder.addFriendButton.text.toString()== "Add Friend"){
                    firebaseUser?.uid.let {it ->
                        FirebaseDatabase.getInstance().reference
                            .child("Add Friend").child(it.toString())
                            .child("Receive").child(user.uid)
                            .setValue(true)

                    }
                }
                else{

                        firebaseUser?.uid.let {it ->
                            FirebaseDatabase.getInstance().reference
                                .child("Add Friend").child(it.toString())
                                .child("Receive").child(user.uid)
                                .removeValue()

                        }

                }
            }

        }
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var usernameTxt: TextView = itemView.findViewById(R.id.activity_user_item_layout_username)
        var profileImageView: CircleImageView = itemView.findViewById(R.id.activity_user_item_layout_ProfileImage)
        var onlineImageView: CircleImageView = itemView.findViewById(R.id.activity_user_item_layout_online)
        var offlineImageView: CircleImageView = itemView.findViewById(R.id.activity_user_item_layout_offline)
        var lastMessageTxt: TextView = itemView.findViewById(R.id.activity_user_item_layout_lastMessage)
        var addFriendButton: Button = itemView.findViewById(R.id.add_friend)

    }

    private fun checkAddFriendStatus(uid: String,addFriend:Button){
        val addFriendRef = firebaseUser?.uid.let { it->
            FirebaseDatabase.getInstance().reference
                .child("Add Friend").child(it.toString())
                .child("Receive")
        }
        addFriendRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(uid).exists()){
                    addFriend.text = "Cancel"
                }
                else{
                    addFriend.text = "Add Friend"
                }
            }
            override fun onCancelled(error: DatabaseError) {


            }



        })
    }



}
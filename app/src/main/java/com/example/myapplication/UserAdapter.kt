package com.example.myapplication

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val mcontext: Context, private val mUsers:List<Users>,
                  private val isChatCheck:Boolean):RecyclerView.Adapter<UserAdapter.ViewHolder?>()
{
    private var firebaseUser: String? = FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mcontext).inflate(R.layout.user_search_item_layout,viewGroup,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val user: Users = mUsers[i]
        holder.usernameTxt.text = user.username
        Picasso.get().load(user.profile).placeholder(R.drawable.blank_profile_picture).into(holder.profileImageView)
        holder.itemView.setOnClickListener {
            val option = arrayOf<CharSequence>(
                "Send Message",
                "Visit Profile"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(mcontext)
            builder.setTitle("What do you want?")
            builder.setItems(option, DialogInterface.OnClickListener{ dialog, position ->
                if (position == 0){
                    val intent = Intent(mcontext,MessageChatActivity::class.java)
                    intent.putExtra("Visit_id",user.uid)
                    mcontext.startActivity(intent)

                }
                if (position == 1){

                }
            })
            builder.show()
        }

        holder.addFriendButton.setOnClickListener{
            if (holder.addFriendButton.text.toString()== "Add Friend"){
                firebaseUser?.let {it ->
                    FirebaseDatabase.getInstance().reference
                        .child("Add Friend").child(user.uid)
                        .child("Receive").child(it)
                        .setValue(true)


                }
                holder.addFriendButton.text = "Cancel"
            }
            else if (holder.addFriendButton.text.toString()== "Friend"){
                firebaseUser?.let {it ->
                    FirebaseDatabase.getInstance().reference
                        .child("Confirm Friends").child(it)
                        .child("Friends").child(user.uid)
                        .setValue(true)
                    FirebaseDatabase.getInstance().reference
                        .child("Confirm Friends").child(user.uid)
                        .child("Friends").child(it)
                        .setValue(true)

                }
                holder.addFriendButton.text = "Friend"
            }

            else {
                firebaseUser?.let {it ->
                    FirebaseDatabase.getInstance().reference
                        .child("Add Friend").child(user.uid)
                        .child("Receive").child(it)
                        .removeValue()

                }
                holder.addFriendButton.text = "Add Friend"


            }

            if (firebaseUser != user.uid){
                return@setOnClickListener
            }
            else{
                holder.addFriendButton.visibility = INVISIBLE
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
}
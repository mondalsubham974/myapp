package com.example.myapplication

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class FriendAdapter(private val mcontext: Context, private val friendList:List<String>,
                    private val isChatCheck:Boolean):RecyclerView.Adapter<FriendAdapter.ViewHolder?>() {
    private var firebaseUser: String? = FirebaseAuth.getInstance().currentUser?.uid
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
                            intent.putExtra("Visit_id",confirmFriendUser!!.uid)
                            mcontext.startActivity(intent)

                        }
                        if (position == 1){
                            val intent = Intent(mcontext, VisitProfileActivity::class.java)
                            intent.putExtra("Visit_id", confirmFriendUser!!.uid)
                            mcontext.startActivity(intent)
                        }
                    })
                    builder.show()

                }
                holder.unFriend!!.setOnClickListener {

                    val pB = ProgressDialog(mcontext)

                    pB.setMessage("please wait....")

                    pB.show()

                    firebaseUser?.let { it ->

                        FirebaseDatabase.getInstance().reference

                            .child("Confirm Friends").child(it)

                            .child("Friends").child(confirmFriendUser!!.uid)

                            .removeValue()

                        FirebaseDatabase.getInstance().reference

                            .child("Confirm Friends").child(confirmFriendUser.uid)

                            .child("Friends").child(it)

                            .removeValue()

                        pB.dismiss()
                    }

                }



        }






    })
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var usernameTxt: TextView = itemView.findViewById(R.id.activity_friend_item_layout_username)
        var profileImageView: CircleImageView = itemView.findViewById(R.id.activity_friend_item_layout_ProfileImage)
        var unFriend: Button? = itemView.findViewById(R.id.unFriend)

    }

}
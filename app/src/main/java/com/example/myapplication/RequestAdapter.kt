package com.example.myapplication

//RequestAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


//I also changed the list from Users to string
class RequestAdapter(private var context: Context, private val requestList:List<String>,
                     private val isChatCheck:Boolean): RecyclerView.Adapter<RequestAdapter.ViewHolder?>() {
    private var firebaseUser: String? = FirebaseAuth.getInstance().currentUser?.uid



    override fun onCreateViewHolder(viewGroup:  ViewGroup, viewType: Int):ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_request_item_layout, viewGroup, false)
        return ViewHolder(view)

    }



    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val user = requestList[i]

        //TODO: I don't like using this method, but because of the way your data is stored in firebase this is the only way to get the user details.
        //TODO: you may need to change the backend firebase data to have a new class like  -->  ReceivedUser(userId, username, userLogo)
        val dbRef = FirebaseDatabase.getInstance().reference.child("User/$user")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val friendUser = snapshot.getValue(Users::class.java)
                holder.displayName.text = friendUser!!.username
                Picasso.get().load(friendUser.profile).placeholder(R.drawable.blank_profile_picture).into(holder.displayImage)
                holder.confirmFriendButton.setOnClickListener{

                    firebaseUser?.let {
                        FirebaseDatabase.getInstance().reference
                            .child("Confirm Friends").child(it)
                            .child("Friends").child(friendUser.uid)
                            .setValue(true)
                        FirebaseDatabase.getInstance().reference
                            .child("Confirm Friends").child(friendUser.uid)
                            .child("Friends").child(it)
                            .setValue(true)
                        FirebaseDatabase.getInstance().reference
                            .child("Add Friend").child(it)
                            .child("Receive").child(friendUser.uid)
                            .removeValue()

                        }


                    }

                holder.cancelFriendButton.setOnClickListener{
                    firebaseUser?.let {it ->
                        FirebaseDatabase.getInstance().reference
                            .child("Add Friend").child(it)
                            .child("Receive").child(friendUser.uid)
                            .removeValue()

                    }


                }



            }
        })
    }

    override fun getItemCount(): Int {
        return requestList.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var displayName: TextView = itemView.findViewById<View>(R.id.activity_request_item_layout_username) as TextView
        var displayImage: CircleImageView = itemView.findViewById<View>(R.id.activity_request_item_layout_ProfileImage) as CircleImageView
        var cancelFriendButton: TextView = itemView.findViewById(R.id.activity_request_item_layout_cancel_button)
        var confirmFriendButton: Button = itemView.findViewById(R.id.activity_request_item_layout_confirm_friend)
    }
}


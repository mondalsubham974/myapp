package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.RequestAdapter.RequestViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class RequestAdapter(private var ctx: Context, private val requestList:List<Users>,
                     private val isChatCheck:Boolean): RecyclerView.Adapter<RequestViewHolder>() {
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var mAuth: FirebaseAuth? = null
    var mDatabaseReference: DatabaseReference? = null


    override fun onCreateViewHolder(viewGroup:  ViewGroup, viewType: Int): RequestViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.activity_request_item_layout, viewGroup, false)
        return RequestViewHolder(view)

    }



    override fun onBindViewHolder(holder: RequestViewHolder, i: Int) {
        val user = requestList[i]
        holder.displayName.text = user.username
        Picasso.get().load(user.profile).placeholder(R.drawable.blank_profile_picture).into(holder.displayImage)

        /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,ProfileActivity.class);
                intent.putExtra("user_id",requestList.get(position));
            }
        }); */
    }

    override fun getItemCount(): Int {
        return requestList.size
    }
    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var displayName: TextView = itemView.findViewById<View>(R.id.activity_request_item_layout_username) as TextView
        var displayImage: CircleImageView = itemView.findViewById<View>(R.id.activity_request_item_layout_ProfileImage) as CircleImageView

    }
}


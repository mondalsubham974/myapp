package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_message_chat.*
import kotlinx.android.synthetic.main.message_item_right.*
import java.util.ArrayList

class ChatAdapter(private val mcontext: Context, private val mChatList:List<Chat>,
                  private val imageUrl:String): RecyclerView.Adapter<ChatAdapter.ViewHolder?>() {
    private val firebaseUser  = FirebaseAuth.getInstance().currentUser!!.uid

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var showChat: TextView? =itemView.findViewById(R.id.message_chat)
        var profileImage: CircleImageView? = itemView.findViewById(R.id.message_left_profile)
        var leftimage: ImageView? = itemView.findViewById(R.id.message_left_image)
        var rightimage: ImageView? = itemView.findViewById(R.id.message_right_image)
        var textSeen: TextView? = itemView.findViewById(R.id.message_right_seen)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup,i:  Int): ViewHolder {
        return if (i == 1){
            val view = LayoutInflater.from(mcontext).inflate(R.layout.message_item_right, viewGroup, false)
            ViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(mcontext).inflate(R.layout.message_item_left, viewGroup, false)
            ViewHolder(view)
        }
    }
    //images messages
    override fun onBindViewHolder(holder: ViewHolder,i: Int) {
        val chat = mChatList[i]

        Picasso.get().load(imageUrl).into(holder.profileImage)
        holder.rightimage?.setOnClickListener {
            val intent = Intent(mcontext,FullImageActivity::class.java)
            intent.putExtra("url",chat.url)
            mcontext.startActivity(intent)
        }
        holder.leftimage?.setOnClickListener {
            val intent = Intent(mcontext,FullImageActivity::class.java)
            intent.putExtra("url",chat.url)
            mcontext.startActivity(intent)
        }

        if (chat.message == "sent you an image." && chat.url != ""){
            val lp = holder.textSeen!!.layoutParams as RelativeLayout.LayoutParams
            lp.setMargins(0,245,10,0)
            holder.textSeen!!.layoutParams = lp
        }

        //images messages righside
        if (chat.message == "" && chat.url != ""){
            if (chat.sender == firebaseUser){
                holder.showChat!!.visibility = View.GONE
                holder.rightimage!!.visibility = View.VISIBLE
                Picasso.get().load(chat.url).into(holder.rightimage)
                Log.d("msg","msg->${holder.rightimage}")
            }
            //images messages leftside
            else {
                holder.showChat!!.visibility = View.GONE
                holder.leftimage!!.visibility = View.VISIBLE
                Picasso.get().load(chat.url).into(holder.leftimage)
                Log.d("msg","msg->${holder.leftimage}")
            }

        }
        else{
            holder.showChat!!.text = chat.message
        }
        //text messages

        //sent and seen message
        if (i == mChatList.size-1){
            if (chat.isseen){
                holder.textSeen?.text = "seen"

                if (chat.message == "sent you an image." && chat.url != ""){
                    val lp:RelativeLayout.LayoutParams = holder.textSeen!!.layoutParams as RelativeLayout.LayoutParams
                    lp.setMargins(0,245,10,0)
                    holder.textSeen!!.layoutParams = lp
                }
            }
            else{
                holder.textSeen?.text = "sent"

                if (chat.message == "sent you an image." && chat.url != ""){
                    val lp = holder.textSeen!!.layoutParams as RelativeLayout.LayoutParams
                    lp.setMargins(0,245,10,0)
                    holder.textSeen!!.layoutParams = lp
                }
            }

        }
        else{
            holder.textSeen?.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return mChatList.size
    }

    override fun getItemViewType(i: Int): Int {
        return if (mChatList[i].sender == firebaseUser){
            1
        }
        else{
            0
        }


    }




}
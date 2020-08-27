package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(private val mcontext: Context, private val mChatList:List<Chat>,
                  private val imageUrl:String): RecyclerView.Adapter<ChatAdapter.ViewHolder?>() {
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var showChat: TextView = itemView.findViewById(R.id.message_chat)
        var profileImage: CircleImageView = itemView.findViewById(R.id.message_left_profile)
        var leftimage: ImageView = itemView.findViewById(R.id.message_left_image)
        var rightimage: ImageView = itemView.findViewById(R.id.message_right_image)
        var textSeen: TextView = itemView.findViewById(R.id.message_right_seen)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position:  Int): ViewHolder {
       return if (position == 1){
           val view = LayoutInflater.from(mcontext).inflate(R.layout.message_item_right, viewGroup, false)
           return ViewHolder(view)
       }
        else{
           val view = LayoutInflater.from(mcontext).inflate(R.layout.message_item_left, viewGroup, false)
           return ViewHolder(view)
       }
    }
    //images messages
    override fun onBindViewHolder(holder: ViewHolder,i: Int) {
        val chat = mChatList[i]

        Picasso.get().load(chat.url).into(holder.profileImage)

        //images messages righside
        if (chat.message.equals("sent you an image.") && !chat.url.equals("")){
            if (chat.sender == firebaseUser!!.uid){
                holder.showChat.visibility = View.GONE
                holder.rightimage.visibility = View.VISIBLE
                Picasso.get().load(chat.url).into(holder.rightimage)
                Log.d("msg","msg->${chat.url}")
            }
            //images messages leftside
            else if (chat.sender != firebaseUser!!.uid){
                holder.showChat.visibility = View.GONE
                holder.rightimage.visibility = View.VISIBLE
                Picasso.get().load(chat.url).into(holder.leftimage)
                Log.d("msg","msg->${chat.url}")
            }
        }
        //text messages
        else{
            holder.showChat.text = chat.message
        }
        //sent and seen message
        if (i == mChatList.size-1){
            if (chat.isseen){
                holder.textSeen.text = "seen"

                if (chat.message.equals("sent you an image.") && !chat.url.equals("")){
                    val lp = holder.textSeen.layoutParams
                    holder.textSeen.layoutParams = lp
                }
            }
            else{
                holder.textSeen.text = "sent"

                if (chat.message.equals("sent you an image.") && !chat.url.equals("")){
                    val lp = holder.textSeen.layoutParams
                    holder.textSeen.layoutParams = lp
                }
            }

        }
        else{
            holder.textSeen.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return mChatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)

        return if (mChatList[position].sender.equals(firebaseUser)){
            1
        }
        else{
            0
        }


    }
}
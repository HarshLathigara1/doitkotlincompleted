package lathigara.harsh.doitkotlin.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_message_row.view.*
import lathigara.harsh.doitkotlin.R
import lathigara.harsh.doitkotlin.model.ChatMessage
import lathigara.harsh.doitkotlin.model.User

class LatestMessageRow(val chatMessage:ChatMessage,val context: Context) :Item<GroupieViewHolder>(){

    var chatPartnerUSer:User? = null
    override fun getLayout(): Int {

        return R.layout.latest_message_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtLatestMessageMessage.text = chatMessage.text
        val chatPartnerId:String
        if (chatMessage.fromId ==  FirebaseAuth.getInstance().uid){
            chatPartnerId = chatMessage.toId

        }else{
            chatPartnerId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUSer = p0.getValue(User::class.java)
                chatPartnerUSer!!.userName
                viewHolder.itemView.txtUserNameLatest.text = chatPartnerUSer?.userName
               // Picasso.get().load(chatPartnerUSer?.profileImage).into(viewHolder.itemView.latestMessageImage)
                Glide.with(context.applicationContext).load(chatPartnerUSer?.profileImageUrl).into(viewHolder.itemView.latestImage)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })



    }


}
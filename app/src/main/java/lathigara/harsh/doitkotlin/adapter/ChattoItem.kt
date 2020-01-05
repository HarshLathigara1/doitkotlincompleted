package lathigara.harsh.doitkotlin.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import lathigara.harsh.doitkotlin.R
import lathigara.harsh.doitkotlin.model.User

class ChattoItem(val righttext:String,val user:User,val context: Context): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
      return  R.layout.chat_to_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.text.text = righttext
        val uri = user.profileImageUrl
        Glide.with(context).load(uri).into(viewHolder.itemView.chat_to_row)
    }
}
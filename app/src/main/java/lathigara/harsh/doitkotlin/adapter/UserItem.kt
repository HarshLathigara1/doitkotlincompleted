package lathigara.harsh.doitkotlin.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import lathigara.harsh.doitkotlin.R
import lathigara.harsh.doitkotlin.model.User

class UserItem( val user:User,private val context: Context):Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtUserName.text = user.userName
        Glide.with(context.applicationContext).load(user.profileImageUrl).into(viewHolder.itemView.userProfile)
    }

}

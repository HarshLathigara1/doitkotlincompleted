package lathigara.harsh.doitkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView

import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.*
import lathigara.harsh.doitkotlin.adapter.UserItem
import lathigara.harsh.doitkotlin.model.User

class NewMessageActivity : AppCompatActivity() {

    companion object{
        val USER_KEY:String = "USER_KEY"
    }
    var image:CircleImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "select User"
        image = findViewById(R.id.userProfile)


        fetchUser()

    }

    private fun fetchUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach {

                    val user = it.getValue(User::class.java)
                    //val email:String = user!!.email
                    if (user != null) {

                        adapter.add(UserItem(user,applicationContext))
                        recylerOfUser.adapter = adapter
                    }


                }

                 adapter.setOnItemClickListener{ item, view ->
                     val userItem = item as UserItem
                     val intent = Intent(view.context,ChatLogActivity::class.java)
                     intent.putExtra(USER_KEY,userItem.user.userName)
                     intent.putExtra(USER_KEY,userItem.user)
                     startActivity(intent)
                     finish()
                 }


            }

        })
    }
}

package lathigara.harsh.doitkotlin

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import lathigara.harsh.doitkotlin.model.User
import java.util.*

class MainActivity : AppCompatActivity() {
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        button.setOnClickListener {
            registerUser()


        }
        imageAdd.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Toast.makeText(this, "Photo Selected", Toast.LENGTH_LONG).show()
            uri = data.data

            val bitMap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
           // val bitmapDrawable = BitmapDrawable(bitMap)

            circlImageselectedPhoto.setImageBitmap(bitMap)
            imageAdd.alpha = 0f

        }
    }

    fun registerUser() {
        val email = edtEmail.text.toString()
        val pass = edtPassword.text.toString()
        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener


                    Toast.makeText(applicationContext, "done", Toast.LENGTH_LONG).show()
                    uploadImageToFirebase()

                }

        } else {
            Toast.makeText(
                applicationContext,
                "Please add userName and Password",
                Toast.LENGTH_LONG
            ).show()
        }


    }

    fun uploadImageToFirebase() {
        if(uri == null)return
        val fileNmae = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileNmae")
        ref.putFile(uri!!).addOnSuccessListener {
            Toast.makeText(this, "added To Firebase", Toast.LENGTH_LONG).show()
            Log.d("Register","sucessfull${it.metadata?.path}")
            ref.downloadUrl.addOnSuccessListener {
                it.toString()
                Log.d("Register","File Location${it}")
                saveUserToRealTimeDatabase(it.toString())

            }
        }

    }
    private fun saveUserToRealTimeDatabase(profileimageUrl:String){
        val uid =  FirebaseAuth.getInstance().uid ?: ""

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, edtUserName.text.toString(),profileimageUrl )
        ref.setValue(user)
            .addOnSuccessListener {

                val intent = Intent(this,LatestMessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                Toast.makeText(this,"User Added To Database",Toast.LENGTH_LONG).show()

            }
    }
}

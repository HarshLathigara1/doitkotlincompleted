package lathigara.harsh.doitkotlin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(var uid:String,var userName:String,var profileImageUrl:String):Parcelable{
    constructor() :this("","","")
}


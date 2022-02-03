package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
class User(var name: String = "", var screenName: String = "", var publicImageUrl: String = "") :
    Parcelable {

    // Should be var instead of val because we start out not knowing what they are and then we need
    // to change it once we parse the json

    // companion object is something that we can reference without create a new instance of this
    // class. Companion object is like a "static place" to put static variables and methods.
    companion object {
        // takes in a json object and converts it into a user object for us right
        fun fromJson(jsonObject: JSONObject): User {
            // start with a generic user that doesn't have anything defined for it, but we're
            // going to have to start populating this
            val user = User()
            user.name = jsonObject.getString("name")
            user.screenName = jsonObject.getString("screen_name")
            user.publicImageUrl = jsonObject.getString("profile_image_url_https")
            return user
        }
    }
}
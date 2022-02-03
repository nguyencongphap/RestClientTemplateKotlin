package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject

@Parcelize
class Tweet(var body: String = "", var createdAt: String = "", var user: User? = null):
    Parcelable {

    companion object {

        // converts a single json object into Tweet
        fun fromJson(jsonObject: JSONObject) : Tweet {
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            // the user is not actually just a string or an integer. It's User class object.
            // So, instead of getting the string, we want to use the static method
            // fromJson of the User class that we defined.
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            return tweet
        }

        // converts a list of json object into a list of Tweets
        fun fromJsonArray(jsonArray: JSONArray): List<Tweet> {
            // Since we set a parameter to be 25 in making the API call, we know that
            // whenever we make one API call, we get back a list of most recent 25 tweets.
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()) {
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }

        fun getFormattedTimestamp(createdAt : String) : String {
            return TimeFormatter.getTimeDifference(createdAt)
        }
    }
}
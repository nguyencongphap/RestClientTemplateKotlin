package com.codepath.apps.restclienttemplate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet

// takes in a list of tweets because it needs to know what it's actually converting to be
// appropriate for the RecyclerView to display.
// For each adapter we always need ViewHolder to actually reference the views. So,
// this class extends the abstract class RecyclerView.Adapter<TweetsAdapter.ViewHolder>()
// and we have to define the class TweetsAdapter.ViewHolder inside TweetsAdapter
class TweetsAdapter (val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    // inflating the layout that we want to use for each of the items to display in RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {

        // we need a context to do a lot of things
        val context = parent.context
        // get inflater with the context that we just got
        val inflater = LayoutInflater.from(context)

        // Inflate our item layout
        val view = inflater.inflate(R.layout.item_tweet, parent, false)

        // return a ViewHolder with the view that we just inflated
        // because this method expects a ViewHolder to be returned
        return ViewHolder(view)
    }


    // Populating data into the item through the ViewHolder
    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        // Get the data model (where to fill data into) based on the position of THIS
        // (the tweet data object being processed by onBindViewHolder) in the input
        // tweets List.
        val tweet: Tweet = tweets.get(position)

        // Set the specific views in the ViewHolder based on the data model.
        // User might be null if some kind of json parsing went wrong. So, we add a safe check
        // by using the questionmark. This way, if the user was not parsed successfully, the
        // name will end up being blank. This helps us avoid that nasty NoPointerException
        holder.tvUsername.text = tweet.user?.name
        holder.tvTweetBody.text = tweet.body
        // use 3rd party library Glide to load images easily
        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).into(holder.ivProfileImage)
    }


    // Tells the adapter how many views are going to be in our recyclerview.
    override fun getItemCount(): Int {
        return tweets.size
    }


    // Clean all elements of the recycler
    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }


    // the ViewHolder class always takes in a View. We're going to be using this ViewHolder
    // class later in OnBindViewHolder when we actually tell each view how to populate
    // themselves and how to show data based on tweets
    // This class needs to reference each of the views in the item_tweet.xml so that it can
    // put the data in the right places.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
    }
}
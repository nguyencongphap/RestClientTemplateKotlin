package com.codepath.apps.restclienttemplate

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class TimelineActivity : AppCompatActivity() {

    // Before we call that populateHomeTimeLine method, we need an instance of the TwitterClient
    // class
    lateinit var client:TwitterClient

    // Get reference to RecyclerView
    lateinit var rvTweets: RecyclerView

    // Get reference to adapter
    lateinit var  adapter: TweetsAdapter

    lateinit var swipeContainer: SwipeRefreshLayout

    // List of tweets to hold all of the data after making the API call and parsing its result JSON
    val tweets = ArrayList<Tweet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        // we need to initialize the var client because we did tell Kotlin by "lateinit"
        // that we're going to eventually initialize it.
        client = TwitterApplication.getRestClient(this)

        swipeContainer = findViewById(R.id.swipeContainer)
        // Set a listener: we have to say "hey, what's actually going to happen" when we refresh
        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing timeline")
            // Call populateHomeTimeline because this method is making the API call to retrieve
            // the most recent tweets.
            populateHomeTimeline()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        // once our layout has been inflated by setContentView, we get the RecyclerView
        rvTweets = findViewById(R.id.rvTweets)

        // Initialize our adapter
        adapter = TweetsAdapter(tweets)

        // Give our RecyclerView a layout manager
        rvTweets.layoutManager = LinearLayoutManager(this)
        // Set the adapter for the RecyclerView
        rvTweets.adapter = adapter

        // Call populateHomeTimeline once var client is initialized
        populateHomeTimeline();
    }

    // onCreateOptionsMenu and onOptionsItemSelected are two methods we have to override
    // to get the menu going
    // onCreateOptionsMenu inflates the menu resource file want to use so that that menu
    // can be associated with this TimelineActivity. Like when we say:
    // "hey, inside this activity, this is the layout menu that we should use"
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the right menu resource file
        menuInflater.inflate(R.menu.menu_main, menu)
        // return false means that the above menu won't be inflated and this menu won't
        // be shown. So, we need to return true.
        return true
    }

    val startForResult1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent
        }
    }

    // Create a callback
    // ActivityOne.java, time to handle the result of the sub-activity
    val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        result -> if (result.resultCode == Activity.RESULT_OK) {

            val data: Intent? = result.data

            // Get data from our intent (our tweet)
            val tweet = result.data?.getParcelableExtra("tweet") as Tweet

            // Update timeline
            // Modifying the data source of tweets
            tweets.add(0, tweet)

            // Update adapter
            adapter.notifyItemInserted(0)
            rvTweets.smoothScrollToPosition(0)
        }
    }

    // Define what happens when a specific type of item in the menu is clicked.
    // Handles clicks on menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        Log.i(TAG, "Entered onOptionsItemSelected")

        if (item.itemId == R.id.compose) {
            // Navigate to compose screen
            val intent = Intent(this, ComposeActivity::class.java)
            // startActivityForResult is like startActivity, but startActivityForResult also
            // expects some result back.
            // REQUEST_CODE can be any integer we want to use. REQUEST_CODE is important
            // because we need to use REQUEST_CODE in the onActivityResult to tell ourselves
            // that hey, this is us coming back from the activity we first launched and asked for
//            startActivityForResult(intent, REQUEST_CODE)
            startForResult.launch(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    // This method utilizes the TwitterClient class to actually populate the home timeline
    fun populateHomeTimeline() {
        // call API endpoint statuses/home_timeline.json using the initialized var client
        client.getHomeTimeline(object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "onSuccess!")

                val jsonArray = json.jsonArray

                // Parse the jsonArray and make a list of tweets
                // good practice to put parsing process inside try-catch block
                try {
                    // Clear adapter before we retrieve the list of tweets.
                    // Without doing so, when we refresh, we're just going to add duplicate tweets
                    // because we don't know how many tweets are old, how many are new. So, when
                    // we get the most recent 25 tweets we should clear our current 25 tweets we
                    // currently have
                    adapter.clear()
                    val listOfNewTweetsRetrieved = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(listOfNewTweetsRetrieved)
                    // once finished filling data into tweets, notify the adapter that things have
                    // changed
                    adapter.notifyDataSetChanged()
                    // Now we call setRefreshing(false) to signal refresh has finished. "Hey, stop
                    // showing the refreshing indicator"
                    swipeContainer.setRefreshing(false)
                } catch (e: JSONException) {
                    Log.e(TAG, "JSON Exception $e")
                }

            }


            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure $statusCode")
            }

        })
    }


    // create a companion object so we can have a TAG variable for this class
    companion object {
        val TAG = "TimelineActivity"
        val REQUEST_CODE = 10
    }
}
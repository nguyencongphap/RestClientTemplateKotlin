package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet

class ComposeActivity : AppCompatActivity() {

    // Grab references to UI elements (or views)
    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)

        // Handling the user's click on the tweet button
        btnTweet.setOnClickListener {

            // Grab the content of edittext (etCompose)
            val tweetContent = etCompose.text.toString()

            // 1. Make sure the tweet isn't empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweets not allowed!", Toast.LENGTH_SHORT).show()
            }
            // 2. Make sure the tweet is under character count
            else if (tweetContent.length > 140) {
                Toast.makeText(this, "Tweet is too long! Limit is 140 characters", Toast.LENGTH_SHORT).show()
            }
            else {
                // TODO: Make an api call to Twitter to publish tweet
                Toast.makeText(this, tweetContent, Toast.LENGTH_SHORT).show()
            }



        }
    }
}
package com.example.memeshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent

class MainActivity : AppCompatActivity() {
    private var currentMemeUrl: String = ""
    private lateinit var shareButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the buttons using findViewById
        shareButton = findViewById(R.id.shareButton)
        nextButton = findViewById(R.id.nextButton)

        // Set click listeners for the buttons
        shareButton.setOnClickListener { shareMeme(it) }
        nextButton.setOnClickListener { nextMeme(it) }

        // No need to initialize ImageView using findViewById
        // The ImageView memeImageView is directly accessible using its ID
        loadMeme()
    }

    private fun loadMeme() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                currentMemeUrl = response.getString("url")
                Glide.with(this).load(currentMemeUrl).into(memeImageView)
            },
            Response.ErrorListener {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show()
            })

        queue.add(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this meme!")
        intent.putExtra(Intent.EXTRA_TEXT, currentMemeUrl)
        startActivity(Intent.createChooser(intent, "Share using"))
    }

    fun nextMeme(view: View) {
        loadMeme()
    }
}
}
package com.bignerdranch.android.lme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AddMorePage : AppCompatActivity() {

    private lateinit var noButton: Button
    private lateinit var yesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_more_page)

        noButton = findViewById(R.id.no_button)
        yesButton =  findViewById(R.id.yes_button)

        noButton.setOnClickListener {

        }

        yesButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
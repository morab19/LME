package com.bignerdranch.android.lme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.io.Serializable
import com.bignerdranch.android.lme.AssignmentClass as AssignmentClass1

class AddMorePage : AppCompatActivity() {

    private lateinit var noButton: Button
    private lateinit var yesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_more_page)

        noButton = findViewById(R.id.no_button)
        yesButton =  findViewById(R.id.yes_button)

        val listOfAssignments:MutableList<AssignmentClass1> = intent.getSerializableExtra("key") as MutableList<AssignmentClass1>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")
        val startTimeAbbreviation = intent.getStringExtra("startTimeZone")
        val endTimeAbbreviation = intent.getStringExtra("endTimeZone")

        noButton.setOnClickListener {
            val intent = Intent(this, RecommendedSchedulePage::class.java)
            intent.putExtra("key", listOfAssignments as Serializable)
            intent.putExtra("startTimeValue", startTimeValue)
            intent.putExtra("endTimeValue", endTimeValue)
            intent.putExtra("startTimeZone", startTimeAbbreviation)
            intent.putExtra("endTimeZone", endTimeAbbreviation)
            startActivity(intent)
        }

        yesButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("key", listOfAssignments as Serializable)
            intent.putExtra("startTimeValue", startTimeValue)
            intent.putExtra("endTimeValue", endTimeValue)
            intent.putExtra("startTimeZone", startTimeAbbreviation)
            intent.putExtra("endTimeZone", endTimeAbbreviation)
            startActivity(intent)
        }
    }
}
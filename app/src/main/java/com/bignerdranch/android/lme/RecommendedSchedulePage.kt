package com.bignerdranch.android.lme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RecommendedSchedulePage : AppCompatActivity() {

    private lateinit var recommendedSchedule: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommended_schedule_page)

        recommendedSchedule = findViewById(R.id.recommended_schedule)

        val listOfAssignments:MutableList<AssignmentClass> = intent.getSerializableExtra("key") as MutableList<AssignmentClass>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")
        val startTimeZone = intent.getStringExtra("startTimeZone")
        val endTimeZone = intent.getStringExtra("endTimeZone")

        recommendedSchedule.text = startTimeValue
    }
}
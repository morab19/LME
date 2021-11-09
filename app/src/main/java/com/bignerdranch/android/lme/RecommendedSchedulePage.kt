package com.bignerdranch.android.lme

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class RecommendedSchedulePage : AppCompatActivity() {

    private lateinit var recommendedSchedule: TextView
    private lateinit var restartButton: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommended_schedule_page)

        recommendedSchedule = findViewById(R.id.recommended_schedule)
        restartButton = findViewById(R.id.restart_button)

        //Information being passed around throughout the app.
        val listOfAssignments:MutableList<AssignmentClass> = intent.getSerializableExtra("key") as MutableList<AssignmentClass>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")
        val startTimeAbbreviation = intent.getStringExtra("startTimeZone")
        val endTimeAbbreviation = intent.getStringExtra("endTimeZone")

        val scheduleStartTime = LocalTime.parse(startTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val scheduleEndTime = LocalTime.parse(endTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        val onlyAssignmentsIndicator : Boolean = onlyAssignmentsDeterminer(listOfAssignments)
        val onlyEventsIndicator : Boolean = onlyEventsDeterminer(listOfAssignments)

        //If statement executes if the user only inputs 1 assignment.
        if((listOfAssignments.size == 1) && (listOfAssignments[0].booleanClass) ){

            recommendedSchedule.text = startTimeValue + " - " + endTimeValue + ": " + listOfAssignments[0].name
        }
        //If statement executes if the user only inputs 1 scheduled event.
        else if( (listOfAssignments.size == 1) && (!listOfAssignments[0].booleanClass) ){

            var eventStartTime = LocalTime.parse(listOfAssignments[0].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            var eventEndTime = LocalTime.parse(listOfAssignments[0].endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if( (eventStartTime == scheduleStartTime) && (eventEndTime == scheduleEndTime) ){

                recommendedSchedule.text = startTimeValue + " - " + endTimeValue + ": " + listOfAssignments[0].name
            }
            else if(scheduleStartTime.isBefore(eventStartTime) && scheduleEndTime.isAfter(eventEndTime)){

                recommendedSchedule.text = startTimeValue + " - " + listOfAssignments[0].startTime + ": Blank\n" + listOfAssignments[0].startTime + " - " + listOfAssignments[0].endTime + ": " + listOfAssignments[0].name +"\n" + listOfAssignments[0].endTime + " - " + endTimeValue + ": Blank"
            }
            else if(eventStartTime == scheduleStartTime){

                recommendedSchedule.text = startTimeValue + " - " + listOfAssignments[0].endTime +": " + listOfAssignments[0].name + "\n" + listOfAssignments[0].endTime + " - " + endTimeValue + ": Blank"
            }
            else{
                recommendedSchedule.text = startTimeValue + " - " + listOfAssignments[0].startTime + ": Blank\n" + listOfAssignments[0].startTime + " - " + listOfAssignments[0].endTime + ": " + listOfAssignments[0].name
            }
        }
        //If statement executes if the user only inputs scheduled Events
        else if(onlyEventsDeterminer(listOfAssignments)){
            recommendedSchedule.text = listOfAssignments.size.toString() + " number of Assignments"
        }
        //If statements executes if the user only inputs assignments.
        else if(onlyAssignmentsDeterminer(listOfAssignments)){
            recommendedSchedule.text = listOfAssignments.size.toString() + " number of scheduled events"
        }
        //recommendedSchedule.text = startTimeValue + "\n" + endTimeValue

        restartButton.setOnClickListener{
            val intent = Intent(this, TimeSchedulePage::class.java)
            startActivity(intent)
        }
    }

    private fun onlyAssignmentsDeterminer(listOfAssignments: MutableList<AssignmentClass>): Boolean{

        var indicator : Int = 0

        for (currentIndex in listOfAssignments) {

            if(!currentIndex.booleanClass){
                indicator = 1
            }
        }

        return indicator == 0
    }

    private fun onlyEventsDeterminer(listOfAssignments: MutableList<AssignmentClass>): Boolean{

        var indicator : Int = 0

        for (currentIndex in listOfAssignments) {

            if(currentIndex.booleanClass){
                indicator = 1
            }
        }

        return indicator == 0
    }

    private fun checkMatchingStartTimes(listOfAssignments: MutableList<AssignmentClass>, startTimeValue: String?): Boolean{

        for (currentIndex in listOfAssignments){

            if(currentIndex.startTime == startTimeValue){
                return true
            }
        }

        return false
    }

    private fun countNumberOfInputtedEvents(listOfAssignments: MutableList<AssignmentClass>): Int{

        var count : Int = 0

        for (currentIndex in listOfAssignments){

            if(currentIndex.booleanClass){

                count++
            }
        }

        return count
    }
}
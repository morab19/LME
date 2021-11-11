package com.bignerdranch.android.lme

import android.annotation.SuppressLint
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
    private lateinit var loopTest: TextView

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommended_schedule_page)

        recommendedSchedule = findViewById(R.id.recommended_schedule)
        restartButton = findViewById(R.id.restart_button)
        loopTest = findViewById(R.id.loop_test)

        //The information being passed around throughout the app.
        val listOfAssignments: MutableList<AssignmentClass> = intent.getSerializableExtra("key") as MutableList<AssignmentClass>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")
        val startTimeAbbreviation = intent.getStringExtra("startTimeZone")
        val endTimeAbbreviation = intent.getStringExtra("endTimeZone")

        val scheduleStartTime = LocalTime.parse(startTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val scheduleEndTime = LocalTime.parse(endTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        val onlyAssignmentsIndicator: Boolean = onlyAssignmentsDeterminer(listOfAssignments)
        val onlyEventsIndicator: Boolean = onlyEventsDeterminer(listOfAssignments)

        //If statement executes if the user only inputs 1 assignment.
        if ((listOfAssignments.size == 1) && (listOfAssignments[0].booleanClass)) {
            recommendedSchedule.text = startTimeValue + " - " + endTimeValue + ": " + listOfAssignments[0].name
        }
        //Else if statement executes if the user only inputs 1 scheduled event.
        else if ((listOfAssignments.size == 1) && (!listOfAssignments[0].booleanClass)) {

            var eventStartTime = LocalTime.parse(listOfAssignments[0].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            var eventEndTime = LocalTime.parse(listOfAssignments[0].endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if ((eventStartTime == scheduleStartTime) && (eventEndTime == scheduleEndTime)) {
                recommendedSchedule.text = startTimeValue + " - " + endTimeValue + ": " + listOfAssignments[0].name
            }
            else if (scheduleStartTime.isBefore(eventStartTime) && scheduleEndTime.isAfter(eventEndTime)) {
                recommendedSchedule.text = startTimeValue + " - " + listOfAssignments[0].startTime + ": Blank\n" + listOfAssignments[0].startTime + " - " + listOfAssignments[0].endTime + ": " + listOfAssignments[0].name + "\n" + listOfAssignments[0].endTime + " - " + endTimeValue + ": Blank"
            }
            else if (eventStartTime == scheduleStartTime) {
                recommendedSchedule.text = startTimeValue + " - " + listOfAssignments[0].endTime + ": " + listOfAssignments[0].name + "\n" + listOfAssignments[0].endTime + " - " + endTimeValue + ": Blank"
            }
            else {
                recommendedSchedule.text = startTimeValue + " - " + listOfAssignments[0].startTime + ": Blank\n" + listOfAssignments[0].startTime + " - " + listOfAssignments[0].endTime + ": " + listOfAssignments[0].name
            }
        }
        //Else if statement executes if the user only inputs scheduled events.
        else if (onlyEventsDeterminer(listOfAssignments)) {
            recommendedSchedule.text = eventsOnlyString(listOfAssignments, startTimeValue, endTimeValue)
        }
        //Else if statement executes if the user only inputs assignments.
        else if (onlyAssignmentsDeterminer(listOfAssignments)) {

            if(startTimeAbbreviation != endTimeAbbreviation){

            }
        }
        //Else statement executes if there is a mix of assignments and scheduled events.
        else{

        }
        //recommendedSchedule.text = startTimeValue + "\n" + endTimeValue

        restartButton.setOnClickListener {

            val intent = Intent(this, TimeSchedulePage::class.java)
            startActivity(intent)
        }
    }

    //This function is used when we are dealing with only scheduled events.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun eventsOnlyString(listOfAssignments: MutableList<AssignmentClass>, startTimeValue: String?, endTimeValue: String?): String {

        var loopBoolean: Int = 1

        //In this while loop we first sort the scheduled events.
        while (loopBoolean <= 5) {

            for (i in listOfAssignments.indices) {

                var outerStringTime = LocalTime.parse(listOfAssignments[i].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                for (j in i until listOfAssignments.size) {

                    var innerStringTime = LocalTime.parse(listOfAssignments[j].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                    if (outerStringTime.isAfter(innerStringTime)) {
                        var temp = listOfAssignments[i]
                        listOfAssignments[i] = listOfAssignments[j]
                        listOfAssignments[j] = temp
                    }
                }
            }

            loopBoolean++
        }

        //In this for loop we check to see if there are any unscheduled times between the scheduled events.
        for (i in listOfAssignments.indices) {

            var outerStringTime = LocalTime.parse(listOfAssignments[i].endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            var innerStringTime = LocalTime.parse(listOfAssignments[i+1].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if (outerStringTime != innerStringTime) {

                val c = AssignmentClass(
                    difficulty = 0,
                    name = "Blank",
                    booleanClass = true,
                    startTime = listOfAssignments[i].endTime,
                    endTime = listOfAssignments[i+1].startTime
                )

                listOfAssignments.add(i+1, c)
            }
        }

        var passedStartTimeValue = LocalTime.parse(startTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        var passedEndTimeValue = LocalTime.parse(endTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        var sortedStartTimeValue = LocalTime.parse(listOfAssignments[0].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        var sortedEndTimeValue = LocalTime.parse(listOfAssignments[listOfAssignments.size - 1].endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        //We check to see if there is an unscheduled time at the beginning of the schedule
        if(passedStartTimeValue != sortedStartTimeValue){

            val c = AssignmentClass(
                difficulty = 0,
                name = "Blank",
                booleanClass = true,
                startTime = startTimeValue.toString(),
                endTime = listOfAssignments[0].startTime
            )

            listOfAssignments.add(0, c)
        }

        //We check to see if there is an unscheduled time at the end of the schedule
        if(passedEndTimeValue != sortedEndTimeValue){

            val c = AssignmentClass(
                difficulty = 0,
                name = "Blank",
                booleanClass = true,
                startTime = listOfAssignments[listOfAssignments.size - 1].endTime,
                endTime = endTimeValue.toString()
            )

            listOfAssignments.add(listOfAssignments.size, c)
        }


        var varStr : String = ""

         for(index in listOfAssignments){
            varStr = varStr + index.startTime + " - " + index.endTime + ": " + index.name + "\n"
        }

        return varStr
    }


  /*  @RequiresApi(Build.VERSION_CODES.O)
    private fun sortAssignments(listOfAssignments: MutableList<AssignmentClass>): MutableList<AssignmentClass> {

        var startTimes = ArrayList<String>()

        for( currentIndex in listOfAssignments) {
            startTimes.add(currentIndex.startTime)
        }

        var sortedEmpList =  startTimes.sort()

      } */

    private fun numberOfIndexes(numOfEvents: Int): Int {

        var x : Int = 1
        var y : Int = 2

        while(y < numOfEvents){
            x++
            y++
        }

        return x + y
    }

    //Checks to see if there are only Assignments in the schedule.
    private fun onlyAssignmentsDeterminer(listOfAssignments: MutableList<AssignmentClass>): Boolean{

        var indicator : Int = 0

        for (currentIndex in listOfAssignments) {

            if(!currentIndex.booleanClass){
                indicator = 1
            }
        }

        return indicator == 0
    }

    //Checks to see if there are only scheduled events in the schedule.
    private fun onlyEventsDeterminer(listOfAssignments: MutableList<AssignmentClass>): Boolean{

        var indicator : Int = 0

        for (currentIndex in listOfAssignments) {

            if(currentIndex.booleanClass){
                indicator = 1
            }
        }

        return indicator == 0
    }

    //Checks to see if any events begin at the beginning of the schedule.
    private fun checkMatchingStartTimes(listOfAssignments: MutableList<AssignmentClass>, startTimeValue: String?): Boolean{

        for (currentIndex in listOfAssignments){

            if(currentIndex.startTime == startTimeValue){
                return true
            }
        }

        return false
    }

    //Check to see if any events end at the end of the schedule.
    private fun checkMatchingEndTimes(listOfAssignments: MutableList<AssignmentClass>, endTimeValue: String?): Boolean{

        for (currentIndex in listOfAssignments){

            if(currentIndex.endTime == endTimeValue){
                return true
            }
        }

        return false
    }

    //Counts the number of scheduled events in the schedule.
    private fun countNumberOfInputtedEvents(listOfAssignments: MutableList<AssignmentClass>): Int{

        var count : Int = 0

        for (currentIndex in listOfAssignments){

            if(!currentIndex.booleanClass){

                count++
            }
        }

        return count
    }

    //Counts the number of assignments in the schedule.
    private fun countNumberOfAssignments(listOfAssignments: MutableList<AssignmentClass>): Int{

        var count : Int = 0

        for (currentIndex in listOfAssignments){

            if(currentIndex.booleanClass){

                count++
            }
        }

        return count
    }
}
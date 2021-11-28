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
import kotlin.collections.ArrayList

class RecommendedSchedulePage : AppCompatActivity() {

    //Initialize all objects on the android screen we will be interacting with.
    private lateinit var recommendedSchedule: TextView
    private lateinit var restartButton: Button
    private lateinit var loopTest: TextView

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommended_schedule_page)

        // Initialize the ArrayList in the beginning to be passed
        // around throughout the entire app until we reach the end.
        recommendedSchedule = findViewById(R.id.recommended_schedule)
        restartButton = findViewById(R.id.restart_button)
        loopTest = findViewById(R.id.loop_test)

        //The information being passed around throughout the app.
        val listOfAssignments: MutableList<AssignmentClass> = intent.getSerializableExtra("key") as MutableList<AssignmentClass>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")
        val startTimeAbbreviation = intent.getStringExtra("startTimeZone")
        val endTimeAbbreviation = intent.getStringExtra("endTimeZone")

        //The start time and end time of the overall schedule is turned into time variables.
        val scheduleStartTime = LocalTime.parse(startTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val scheduleEndTime = LocalTime.parse(endTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        //If statement executes if the user only inputs 1 assignment.
        if ((listOfAssignments.size == 1) && (listOfAssignments[0].booleanClass)) {
            recommendedSchedule.text = startTimeValue + " - " + endTimeValue + ": " + listOfAssignments[0].name
        }
        //Else if statement executes if the user only inputs 1 scheduled event.
        else if ((listOfAssignments.size == 1) && (!listOfAssignments[0].booleanClass)) {

            val eventStartTime = LocalTime.parse(listOfAssignments[0].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val eventEndTime = LocalTime.parse(listOfAssignments[0].endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

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
            recommendedSchedule.text = assignmentsOnlyString(listOfAssignments, startTimeValue, endTimeValue)
        }
        //Else statement executes if there is a mix of assignments and scheduled events.
        else{
            recommendedSchedule.text = assignmentsAndEventsString(listOfAssignments, startTimeValue,endTimeValue)
        }

        //This button is for testing purposes and makes it easier to go back
        //and create a new scheduled instead of having to restart numerous times.
        restartButton.setOnClickListener {
            val intent = Intent(this, TimeSchedulePage::class.java)
            startActivity(intent)
        }
    }

    //This function is used when we are dealing with
    //a mix of assignments and scheduled events.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun assignmentsAndEventsString(
        listOfAssignments: MutableList<AssignmentClass>,
        startTimeValue: String?,
        endTimeValue: String?
    ): String {

        var listOfOnlyEvents : ArrayList<AssignmentClass> = grabEvents(listOfAssignments)
        var listOfOnlyAssignments : ArrayList<AssignmentClass> = grabAssignments(listOfAssignments)

        var loopBoolean = 1

        //In this while loop we first sort the scheduled events.
        while (loopBoolean <= 5) {

            for (i in listOfOnlyEvents.indices) {

                val outerStringTime = LocalTime.parse(listOfOnlyEvents[i].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                for (j in i until listOfOnlyEvents.size) {

                    val innerStringTime = LocalTime.parse(listOfOnlyEvents[j].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                    if (outerStringTime.isAfter(innerStringTime)) {
                        val temp = listOfOnlyEvents[i]
                        listOfOnlyEvents[i] = listOfOnlyEvents[j]
                        listOfOnlyEvents[j] = temp
                    }
                }
            }

            loopBoolean++
        }

        //In this for loop we check to see if there are any unscheduled times between the scheduled events.
        for (i in listOfOnlyEvents.indices) {

            val outerStringTime = LocalTime.parse(listOfOnlyEvents[i].endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val innerStringTime = LocalTime.parse(listOfOnlyEvents[i+1].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if (outerStringTime != innerStringTime) {
                val c = AssignmentClass(
                    difficulty = 0,
                    name = "Blank",
                    booleanClass = true,
                    startTime = listOfOnlyEvents[i].endTime,
                    endTime = listOfOnlyEvents[i+1].startTime
                )

                listOfOnlyEvents.add(i+1, c)
            }
        }

        val passedStartTimeValue = LocalTime.parse(startTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val passedEndTimeValue = LocalTime.parse(endTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val sortedStartTimeValue = LocalTime.parse(listOfOnlyEvents[0].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val sortedEndTimeValue = LocalTime.parse(listOfOnlyEvents[listOfOnlyEvents.size - 1].endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        //We check to see if there is an unscheduled time at the beginning of the schedule
        if(passedStartTimeValue != sortedStartTimeValue){
            val c = AssignmentClass(
                difficulty = 0,
                name = "Blank",
                booleanClass = true,
                startTime = startTimeValue.toString(),
                endTime = listOfOnlyEvents[0].startTime
            )

            listOfOnlyEvents.add(0, c)
        }

        //We check to see if there is an unscheduled time at the end of the schedule
        if(passedEndTimeValue != sortedEndTimeValue){
            val c = AssignmentClass(
                difficulty = 0,
                name = "Blank",
                booleanClass = true,
                startTime = listOfOnlyEvents[listOfOnlyEvents.size - 1].endTime,
                endTime = endTimeValue.toString()
            )

            listOfOnlyEvents.add(listOfOnlyEvents.size, c)
        }

        val totalMinutes : Int = mixedOnlyTotalMinutesDeterminer(listOfOnlyEvents)

        loopTest.text = totalMinutes.toString()

        return recommendedScheduleString(listOfOnlyEvents)
    }

    private fun grabEvents(
        listOfAssignments: MutableList<AssignmentClass>
    ): ArrayList<AssignmentClass> {

        val listOfOnlyEvents : ArrayList<AssignmentClass> = ArrayList()

        for(currentIndex in listOfAssignments){

            if(!currentIndex.booleanClass){
                listOfOnlyEvents.add(currentIndex)
            }
        }

        return listOfOnlyEvents
    }

    private fun grabAssignments(
        listOfAssignments: MutableList<AssignmentClass>
    ): ArrayList<AssignmentClass> {

        val listOfOnlyAssignments : ArrayList<AssignmentClass> = ArrayList()

        for(currentIndex in listOfAssignments){

            if(currentIndex.booleanClass){
                listOfOnlyAssignments.add(currentIndex)
            }
        }

        return listOfOnlyAssignments
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mixedOnlyTotalMinutesDeterminer(
        listOfEvents: MutableList<AssignmentClass>
    ): Int {

        var totalMinutes = 0

        for(currentIndex in listOfEvents){

            if(currentIndex.name == "Blank"){
                var currentIndexStartTime = LocalTime.parse(currentIndex.startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                var currentIndexEndTime = LocalTime.parse(currentIndex.endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                var finalHour = currentIndexEndTime.hour - currentIndexStartTime.hour
                var finalMinute = currentIndexEndTime.minute - currentIndexStartTime.minute

                if(finalMinute < 0){
                    finalMinute = finalMinute + 60
                    finalHour = finalHour - 1
                }

                totalMinutes = totalMinutes + (finalHour * 60) + finalMinute
            }
        }

        return totalMinutes
    }

    //This function is used when we are dealing with only assignments.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun assignmentsOnlyString(
        listOfAssignments: MutableList<AssignmentClass>,
        startTimeValue: String?,
        endTimeValue: String?
    ): String {

        val sameIntAcrossAllAssignments : Int = listOfAssignments[0].difficulty
        val sameIntOperationBoolean : Boolean = sameIntAcrossAllAssignmentsDeterminer(sameIntAcrossAllAssignments, listOfAssignments)
        val totalMinutes : Int = assignmentsOnlyTotalMinutesDeterminer(startTimeValue, endTimeValue)

        //If statement executes if all assignments have matching difficulties
        if(sameIntOperationBoolean){

            //If statement executes if the user only put 2 assignments.
            if(listOfAssignments.size == 2){

                val minutesPerAssignment : Int = totalMinutes / 2

                listOfAssignments[0].startTime = startTimeValue.toString()
                listOfAssignments[0].endTime = addTime(listOfAssignments[0].startTime, minutesPerAssignment)
                listOfAssignments[1].startTime = listOfAssignments[0].endTime
                listOfAssignments[1].endTime = addTime(listOfAssignments[1].startTime, minutesPerAssignment)
            }
            //Else statement executes if the user put more than 2 assignments.
            else{

                val minutesPerAssignment : Int = totalMinutes / listOfAssignments.size
                listOfAssignments[0].startTime = startTimeValue.toString()

                for(i in listOfAssignments.indices){

                    if(i == listOfAssignments.size - 1){
                        break
                    }

                    listOfAssignments[i].endTime = addTime(listOfAssignments[i].startTime, minutesPerAssignment)
                    listOfAssignments[i + 1].startTime = listOfAssignments[i].endTime
                }

                listOfAssignments[listOfAssignments.size - 1].endTime = endTimeValue.toString()
            }
        }
        //Else statement executes if we're dealing with more than 1 unique difficulty.
        else{

            var loopBoolean = 1

            while (loopBoolean <= 5) {

                for (i in listOfAssignments.indices) {

                    val outerDifficulty = listOfAssignments[i].difficulty

                    for (j in i until listOfAssignments.size) {

                        val innerDifficulty = listOfAssignments[j].difficulty

                        if (innerDifficulty < outerDifficulty) {
                            val temp = listOfAssignments[i]
                            listOfAssignments[i] = listOfAssignments[j]
                            listOfAssignments[j] = temp
                        }
                    }
                }

                loopBoolean++
            }

            val difficultyArrayList = arrayListOf<Int>()

            for(currentIndex in listOfAssignments){
                difficultyArrayList.add(currentIndex.difficulty)
            }

            val difficultyMinuteArrayList = difficultyMinuteAssigner(difficultyArrayList, totalMinutes)

            listOfAssignments[0].startTime = startTimeValue.toString()

            for(i in listOfAssignments.indices){

                if(i == listOfAssignments.size - 1){
                    break
                }


                listOfAssignments[i].endTime = addTime(listOfAssignments[i].startTime, difficultyMinuteArrayList[i])
                listOfAssignments[i + 1].startTime = listOfAssignments[i].endTime
            }

            listOfAssignments[listOfAssignments.size - 1].endTime = endTimeValue.toString()

        }

        return recommendedScheduleString(listOfAssignments)
    }

    private fun difficultyMinuteAssigner(
        difficultyArrayList: ArrayList<Int>,
        totalMinutes: Int,
    ): ArrayList<Int> {

        val timeArrayList = arrayListOf<Int>()

        for(i in difficultyArrayList){

            if(i == 1){
                timeArrayList.add(1)
            }
            else if(i == 2){
                timeArrayList.add(2)
            }
            else if(i == 3){
                timeArrayList.add(4)
            }
            else if(i == 4){
                timeArrayList.add(7)
            }
            else{//(i == 5)
                timeArrayList.add(10)
            }
        }

        while(timeArrayList.sum() != totalMinutes){

            for(i in timeArrayList.indices){
                timeArrayList[i]++

                if(timeArrayList.sum() == totalMinutes){
                    break
                }
            }
        }

        return timeArrayList
    }

    //This function is used to add minutes onto a given time.
    //(Ex. Adding 30 minutes to '11:45 AM' should give '12:15 PM'.
    private fun addTime(
        currentTime: String,
        minutes: Int
    ): String {

        val previousHourSubstring : String
        val previousMinuteSubstring : String
        var previousAbbreviationSubstring : String

        if(currentTime.length == 7){

            previousHourSubstring = currentTime.substring(0,1)			//grabs the '3' part in '3:00 PM'
            previousMinuteSubstring = currentTime.substring(2,4)		//grabs the '00' part in '3:00 PM'
            previousAbbreviationSubstring = currentTime.substring(5,7)	//grabs the 'PM' part in '3:00 PM'
        }
        else{
            previousHourSubstring = currentTime.substring(0,2)          //grabs the '10' part in '10:00 PM'
            previousMinuteSubstring = currentTime.substring(3,5)        //grabs the '00' part in '10:00 PM'
            previousAbbreviationSubstring = currentTime.substring(6,8)  //grabs the 'PM' part in '10:00 PM'
        }

        val newMinute : Int = previousMinuteSubstring.toInt() + minutes

        val remainderHours : Int
        val remainderMinutes : Int

        var finalHour : String
        var finalMinute : String

        if(newMinute >= 60){

            remainderHours = newMinute / 60
            remainderMinutes = newMinute % 60

            finalHour = (remainderHours + previousHourSubstring.toInt()).toString()
            finalMinute = remainderMinutes.toString()

            if(finalHour.toInt() >= 12){
                previousAbbreviationSubstring = "PM"
            }

            if(finalHour.toInt() > 12){
                finalHour = (finalHour.toInt() - 12).toString()
            }

            if(finalMinute.toInt() <= 9){
                finalMinute = "0" + finalMinute
            }

            return finalHour + ":" + finalMinute + " " + previousAbbreviationSubstring
        }
        else{

            finalHour = previousHourSubstring
            finalMinute = newMinute.toString()

            if(finalMinute.toInt() <= 9){
                finalMinute = "0" + finalMinute
            }

            return finalHour + ":" + finalMinute + " " + previousAbbreviationSubstring
        }
    }

    //This function is used when we are dealing with only assignments.
    //This returns how many total minutes there are in the schedule.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun assignmentsOnlyTotalMinutesDeterminer(
        startTimeValue: String?,
        endTimeValue: String?
    ): Int {

        val scheduleStartTime = LocalTime.parse(startTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val scheduleEndTime = LocalTime.parse(endTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        if(scheduleStartTime.minute.toString() == "30" && scheduleEndTime.minute.toString() != "30"){
            val difference = scheduleEndTime.hour - scheduleStartTime.hour + 1
            val finalDifference = difference - 1
            return finalDifference * 60 - 30
        }
        else if(scheduleStartTime.minute.toString() != "30" && scheduleEndTime.minute.toString() == "30"){
            val difference = scheduleEndTime.hour - scheduleStartTime.hour
            return difference * 60 + 30
        }
        else{
            val difference = scheduleEndTime.hour - scheduleStartTime.hour
            return difference * 60
        }
    }

    //This function is used when we are dealing with only assignments. We check to see if
    //all assignments share the same difficulty. If so return true otherwise return false.
    private fun sameIntAcrossAllAssignmentsDeterminer(
        sameIntAcrossAllAssignments: Int,
        listOfAssignments: MutableList<AssignmentClass>
    ): Boolean {

        for(currentIndex in listOfAssignments){

            if(currentIndex.difficulty == sameIntAcrossAllAssignments){
                continue
            }
            else{
                return false
            }
        }

        return true
    }

    //This function is used when we are dealing with only scheduled events.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun eventsOnlyString(
        listOfAssignments: MutableList<AssignmentClass>,
        startTimeValue: String?,
        endTimeValue: String?
    ): String {

        var loopBoolean = 1

        //In this while loop we first sort the scheduled events.
        while (loopBoolean <= 5) {

            for (i in listOfAssignments.indices) {

                val outerStringTime = LocalTime.parse(listOfAssignments[i].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                for (j in i until listOfAssignments.size) {

                    val innerStringTime = LocalTime.parse(listOfAssignments[j].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                    if (outerStringTime.isAfter(innerStringTime)) {
                        val temp = listOfAssignments[i]
                        listOfAssignments[i] = listOfAssignments[j]
                        listOfAssignments[j] = temp
                    }
                }
            }

            loopBoolean++
        }

        //In this for loop we check to see if there are any unscheduled times between the scheduled events.
        for (i in listOfAssignments.indices) {

            val outerStringTime = LocalTime.parse(listOfAssignments[i].endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val innerStringTime = LocalTime.parse(listOfAssignments[i+1].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

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

        val passedStartTimeValue = LocalTime.parse(startTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val passedEndTimeValue = LocalTime.parse(endTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val sortedStartTimeValue = LocalTime.parse(listOfAssignments[0].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val sortedEndTimeValue = LocalTime.parse(listOfAssignments[listOfAssignments.size - 1].endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

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

        //We create a String of the now sorted schedule to
        //be returned and displayed on the android screen.
        return recommendedScheduleString(listOfAssignments)

    }

    //Checks to see if there are only Assignments in the schedule.
    private fun onlyAssignmentsDeterminer(
        listOfAssignments: MutableList<AssignmentClass>
    ): Boolean{

        var indicator = 0

        for (currentIndex in listOfAssignments) {

            if(!currentIndex.booleanClass){
                indicator = 1
            }
        }

        return indicator == 0
    }

    //Checks to see if there are only scheduled events in the schedule.
    private fun onlyEventsDeterminer(
        listOfAssignments: MutableList<AssignmentClass>
    ): Boolean{

        var indicator = 0

        for (currentIndex in listOfAssignments) {

            if(currentIndex.booleanClass){
                indicator = 1
            }
        }

        return indicator == 0
    }

    //This function is used to print out and display the recommended schedule.
    private fun recommendedScheduleString(
        listOfAssignments: MutableList<AssignmentClass>
    ): String {

        var varStr = ""

        for(index in listOfAssignments){
            varStr = varStr + index.startTime + " - " + index.endTime + ": " + index.name + "\n"
        }

        return varStr
    }

    //Checks to see if any events begin at the beginning of the schedule.
    private fun checkMatchingStartTimes(
        listOfAssignments: MutableList<AssignmentClass>,
        startTimeValue: String?
    ): Boolean{

        for (currentIndex in listOfAssignments){

            if(currentIndex.startTime == startTimeValue){
                return true
            }
        }

        return false
    }

    //Check to see if any events end at the end of the schedule.
    private fun checkMatchingEndTimes(
        listOfAssignments: MutableList<AssignmentClass>,
        endTimeValue: String?
    ): Boolean{

        for (currentIndex in listOfAssignments){

            if(currentIndex.endTime == endTimeValue){
                return true
            }
        }

        return false
    }

    //Counts the number of scheduled events in the schedule.
    private fun countNumberOfInputtedEvents(
        listOfAssignments: MutableList<AssignmentClass>
    ): Int{

        var count = 0

        for (currentIndex in listOfAssignments){

            if(!currentIndex.booleanClass){

                count++
            }
        }

        return count
    }

    //Counts the number of assignments in the schedule.
    private fun countNumberOfAssignments(
        listOfAssignments: MutableList<AssignmentClass>
    ): Int{

        var count = 0

        for (currentIndex in listOfAssignments){

            if(currentIndex.booleanClass){

                count++
            }
        }

        return count
    }
}
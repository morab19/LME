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
    private lateinit var scheduleTextView: TextView

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
        scheduleTextView = findViewById(R.id.time_schedule)

        //The information being passed around throughout the app.
        val listOfAssignments: MutableList<AssignmentClass> = intent.getSerializableExtra("key") as MutableList<AssignmentClass>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")

        scheduleTextView.text = "Schedule: " + startTimeValue + " - " + endTimeValue

        //The start time and end time of the overall schedule is turned into time variables.
        val scheduleStartTime = LocalTime.parse(startTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val scheduleEndTime = LocalTime.parse(endTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        //If statement executes if the user only inputs 1 assignment.
        if ((listOfAssignments.size == 1) && (listOfAssignments[0].isAssignmentClass)) {
            recommendedSchedule.text = startTimeValue + " - " + endTimeValue + ": " + listOfAssignments[0].name
        }
        //Else if statement executes if the user only inputs 1 scheduled event.
        else if ((listOfAssignments.size == 1) && (!listOfAssignments[0].isAssignmentClass)) {

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

    //Checks to see if there are only scheduled events in the schedule.
    private fun onlyEventsDeterminer(
        listOfAssignments: MutableList<AssignmentClass>
    ): Boolean{

        var indicator = 0

        for (currentIndex in listOfAssignments) {

            if(currentIndex.isAssignmentClass){
                indicator = 1
            }
        }

        return indicator == 0
    }

    //Checks to see if there are only Assignments in the schedule.
    private fun onlyAssignmentsDeterminer(
        listOfAssignments: MutableList<AssignmentClass>
    ): Boolean{

        var indicator = 0

        for (currentIndex in listOfAssignments) {

            if(!currentIndex.isAssignmentClass){
                indicator = 1
            }
        }

        return indicator == 0
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
                    isAssignmentClass = true,
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
                isAssignmentClass = true,
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
                isAssignmentClass = true,
                startTime = listOfAssignments[listOfAssignments.size - 1].endTime,
                endTime = endTimeValue.toString()
            )

            listOfAssignments.add(listOfAssignments.size, c)
        }

        //We create a String of the now sorted schedule to
        //be returned and displayed on the android screen.
        return recommendedScheduleString(listOfAssignments)
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

            //In this while loop we sort the assignments based on ascending difficulty.
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

            //In this for loop we are grabbing the difficulty of each assignment.
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

    //This function is used when we are dealing with
    //a mix of assignments and scheduled events.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun assignmentsAndEventsString(
        listOfAssignments: MutableList<AssignmentClass>,
        startTimeValue: String?,
        endTimeValue: String?
    ): String {

        val scheduleStartTime = LocalTime.parse(startTimeValue.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val scheduleEndTime = LocalTime.parse(endTimeValue.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        var listOfOnlyEvents : ArrayList<AssignmentClass> = grabEvents(listOfAssignments)
        var listOfOnlyAssignments : ArrayList<AssignmentClass> = grabAssignments(listOfAssignments)

        //This if statement executes if we are dealing with
        //only one scheduled event and only one assignment.
        if(listOfOnlyEvents.size == 1 && listOfOnlyAssignments.size == 1){

            val eventStartTime = LocalTime.parse(listOfOnlyEvents[0].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val eventEndTime = LocalTime.parse(listOfOnlyEvents[0].endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            //This if statement executes if the scheduled event takes up the entire schedule.
            if((eventStartTime == scheduleStartTime) && (eventEndTime == scheduleEndTime)){
                loopTest.text = "There is no extra blank space to assign the assignments."
                return startTimeValue + " - " + endTimeValue + ": " + listOfAssignments[0].name
            }
            //This else if statement executes if the schedule event is in the middle of the schedule.
            else if(scheduleStartTime.isBefore(eventStartTime) && scheduleEndTime.isAfter(eventEndTime)){

                val newList : ArrayList<AssignmentClass> = ArrayList()

                val a = AssignmentClass(
                    difficulty = 0,
                    name = listOfOnlyAssignments[0].name,
                    isAssignmentClass = true,
                    startTime = startTimeValue.toString(),
                    endTime = listOfOnlyEvents[0].startTime
                )

                val b = AssignmentClass(
                    difficulty = 0,
                    name = listOfOnlyEvents[0].name,
                    isAssignmentClass = true,
                    startTime = listOfOnlyEvents[0].startTime,
                    endTime = listOfOnlyEvents[0].endTime
                )

                val c = AssignmentClass(
                    difficulty = 0,
                    name = listOfOnlyAssignments[0].name,
                    isAssignmentClass = true,
                    startTime =  listOfOnlyEvents[0].endTime,
                    endTime = endTimeValue.toString()
                )

                newList.add(a)
                newList.add(b)
                newList.add(c)

                return recommendedScheduleString(newList)
            }
            //This else if statement executes if the scheduled event
            //begins at the beginning of the schedule but ends in the middle.
            else if(eventStartTime == scheduleStartTime){

                val newList : ArrayList<AssignmentClass> = ArrayList()

                val a = AssignmentClass(
                    difficulty = 0,
                    name = listOfOnlyEvents[0].name,
                    isAssignmentClass = true,
                    startTime = listOfOnlyEvents[0].startTime,
                    endTime = listOfOnlyEvents[0].endTime
                )

                val b = AssignmentClass(
                    difficulty = 0,
                    name = listOfOnlyAssignments[0].name,
                    isAssignmentClass = true,
                    startTime = listOfOnlyEvents[0].endTime,
                    endTime = endTimeValue.toString()
                )

                newList.add(a)
                newList.add(b)

                return recommendedScheduleString(newList)
            }
            //This else statement executes if the scheduled
            //event begins in the middle of the schedule.
            else {

                val newList : ArrayList<AssignmentClass> = ArrayList()

                val a = AssignmentClass(
                    difficulty = 0,
                    name = listOfOnlyAssignments[0].name,
                    isAssignmentClass = true,
                    startTime = startTimeValue.toString(),
                    endTime = listOfOnlyEvents[0].startTime
                )

                val b = AssignmentClass(
                    difficulty = 0,
                    name = listOfOnlyEvents[0].name,
                    isAssignmentClass = true,
                    startTime = listOfOnlyEvents[0].startTime,
                    endTime =  listOfOnlyEvents[0].endTime
                )

                newList.add(a)
                newList.add(b)

                return recommendedScheduleString(newList)
            }
        }

        //This if statement executes if we are dealing with
        //only scheduled event and more than one assignment.
        if(listOfOnlyEvents.size == 1 && listOfOnlyAssignments.size > 1){

            val eventStartTime = LocalTime.parse(listOfOnlyEvents[0].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val eventEndTime = LocalTime.parse(listOfOnlyEvents[0].endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            //This if statement executes if the scheduled event takes up the entire schedule.
            if((eventStartTime == scheduleStartTime) && (eventEndTime == scheduleEndTime)) {
                loopTest.text = "There is no extra blank space to assign the assignments."
                return startTimeValue + " - " + endTimeValue + ": " + listOfAssignments[0].name
            }
            //This else if statement executes if the schedule event is in the middle of the schedule.
            else if (scheduleStartTime.isBefore(eventStartTime) && scheduleEndTime.isAfter(eventEndTime)) {

                val newList : ArrayList<AssignmentClass> = ArrayList()

                val a = AssignmentClass(
                    difficulty = 0,
                    name = "Blank",
                    isAssignmentClass = true,
                    startTime = startTimeValue.toString(),
                    endTime = listOfOnlyEvents[0].startTime
                )

                val b = AssignmentClass(
                    difficulty = 0,
                    name = listOfOnlyEvents[0].name,
                    isAssignmentClass = false,
                    startTime = listOfOnlyEvents[0].startTime,
                    endTime = listOfOnlyEvents[0].endTime
                )

                val c = AssignmentClass(
                    difficulty = 0,
                    name = "Blank",
                    isAssignmentClass = true,
                    startTime =  listOfOnlyEvents[0].endTime,
                    endTime = endTimeValue.toString()
                )

                newList.add(a)
                newList.add(b)
                newList.add(c)

                var sameIntAcrossAllAssignments : Int = listOfOnlyAssignments[0].difficulty
                var sameIntOperationBoolean : Boolean = sameIntAcrossAllAssignmentsDeterminer(sameIntAcrossAllAssignments, listOfOnlyAssignments )
                var totalMinutes : Int = mixedOnlyTotalMinutesDeterminer(newList)

                //If statement executes if all assignments have matching difficulties
                if(sameIntOperationBoolean){

                    newList[0].name = listOfOnlyAssignments[0].name
                    newList[2].name = listOfOnlyAssignments[1].name

                    //If statement executes if the user only put 2 assignments
                    if(listOfOnlyAssignments.size == 2){

                        val minutesPerAssignment : Int = totalMinutes / 2

                        for(i in newList.indices){

                            if(i == newList.size - 1){
                                break
                            }

                            if(newList[i].isAssignmentClass) {

                                var newTime = addTime(newList[i].startTime, minutesPerAssignment)
                                val newCompareTime = LocalTime.parse(newTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                                val timeInNextIndex = LocalTime.parse(newList[i + 1].startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                                if (newCompareTime.isAfter(timeInNextIndex)) {
                                    continue
                                }
                                else {
                                    return "FALSEHELLO" + newCompareTime.toString() + timeInNextIndex.toString()
                                }
                            }
                        }

                        newList[newList.size - 1].endTime = endTimeValue.toString()

                        return recommendedScheduleString(newList)
                    }
                    //Else statement executes if the user put more than 2 assignments.
                    else{

                    }
                }
                //Else statement executes if we're dealing with only more than 1 unique difficulty.
                else{

                }

                return recommendedScheduleString(newList)
            }
            //This else if statement executes if the scheduled event
            //begins at the beginning of the schedule but ends in the middle.
            else if (eventStartTime == scheduleStartTime) {

                val newList : ArrayList<AssignmentClass> = ArrayList()

                val a = AssignmentClass(
                    difficulty = 0,
                    name = listOfOnlyEvents[0].name,
                    isAssignmentClass = false,
                    startTime = listOfOnlyEvents[0].startTime,
                    endTime = listOfOnlyEvents[0].endTime
                )
                val b = AssignmentClass(
                    difficulty = 0,
                    name = "Blank",
                    isAssignmentClass = true,
                    startTime = listOfOnlyEvents[0].endTime,
                    endTime = endTimeValue.toString()
                )

                newList.add(a)
                newList.add(b)

                var sameIntAcrossAllAssignments : Int = listOfOnlyAssignments[0].difficulty
                var sameIntOperationBoolean : Boolean = sameIntAcrossAllAssignmentsDeterminer(sameIntAcrossAllAssignments, listOfOnlyAssignments )
                var totalMinutes : Int = mixedOnlyTotalMinutesDeterminer(newList)

                if(sameIntOperationBoolean){

                    if(listOfOnlyAssignments.size == 2){

                        val minutesPerAssignment : Int = totalMinutes / 2

                        newList.remove(b)

                        val c = AssignmentClass(
                            difficulty = 0,
                            name = listOfOnlyAssignments[0].name,
                            isAssignmentClass = true,
                            startTime = listOfOnlyEvents[0].endTime,
                            endTime = addTime(listOfOnlyEvents[0].endTime, minutesPerAssignment)
                        )
                        val d = AssignmentClass(
                            difficulty = 0,
                            name = listOfOnlyAssignments[1].name,
                            isAssignmentClass = true,
                            startTime = c.endTime,
                            endTime = endTimeValue.toString()
                        )

                        newList.add(c)
                        newList.add(d)

                        return recommendedScheduleString(newList)
                    }
                    else{

                        val minutesPerAssignment : Int = totalMinutes / listOfOnlyAssignments.size

                        newList.remove(b)

                        for(i in listOfOnlyAssignments.indices){

                            val newObject = AssignmentClass(
                                difficulty = 0,
                                name = listOfOnlyAssignments[i].name,
                                isAssignmentClass = true,
                                startTime = newList[i].endTime,
                                endTime = addTime(newList[i].endTime, minutesPerAssignment)
                            )

                            newList.add(newObject)
                        }

                        return recommendedScheduleString(newList)
                    }
                }
                else{

                    if(listOfOnlyAssignments.size == 2){

                    }
                    else{

                    }
                }

                return totalMinutes.toString() + "\n" + sameIntAcrossAllAssignments.toString() + "\n" + sameIntOperationBoolean.toString()
            }
            //This else statement executes if the scheduled
            //event begins in the middle of the schedule.
            else {

                val newList : ArrayList<AssignmentClass> = ArrayList()

                val a = AssignmentClass(
                    difficulty = 0,
                    name = "Blank",
                    isAssignmentClass = true,
                    startTime = startTimeValue.toString(),
                    endTime = listOfOnlyEvents[0].startTime
                )
                val b = AssignmentClass(
                    difficulty = 0,
                    name = listOfOnlyEvents[0].name,
                    isAssignmentClass = false,
                    startTime = listOfOnlyEvents[0].startTime,
                    endTime = listOfOnlyEvents[0].endTime
                )

                newList.add(a)
                newList.add(b)

                var sameIntAcrossAllAssignments : Int = listOfOnlyAssignments[0].difficulty
                var sameIntOperationBoolean : Boolean = sameIntAcrossAllAssignmentsDeterminer(sameIntAcrossAllAssignments, listOfOnlyAssignments )
                var totalMinutes : Int = mixedOnlyTotalMinutesDeterminer(newList)

                if(sameIntOperationBoolean){

                }
                else{

                }
            }
        }

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
                    isAssignmentClass = true,
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
                isAssignmentClass = true,
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
                isAssignmentClass = true,
                startTime = listOfOnlyEvents[listOfOnlyEvents.size - 1].endTime,
                endTime = endTimeValue.toString()
            )

            listOfOnlyEvents.add(listOfOnlyEvents.size, c)
        }

        val sameIntAcrossAllAssignments : Int = listOfAssignments[0].difficulty
        val sameIntOperationBoolean : Boolean = sameIntAcrossAllAssignmentsDeterminer(sameIntAcrossAllAssignments, listOfOnlyAssignments)
        val totalMinutes : Int = mixedOnlyTotalMinutesDeterminer(listOfOnlyEvents)

        return sameIntAcrossAllAssignments.toString() + sameIntOperationBoolean.toString() + totalMinutes.toString()

        //This else if statement executes if we are dealing with
        //only 1 assignment and more than 1 scheduled event.
        if(listOfOnlyEvents.size > 1 && listOfOnlyAssignments.size == 1){

        }
        //This else if statement executes if we are dealing with
        //more than 1 assignment and only 1 scheduled event.
        //(listOfOnlyEvents.size > 1 && listOfOnlyAssignments.size > 1)
        else{

        }

        return "Blank"
    }

    private fun checkForNoLeftoverTime(
        listOfOnlyEvents: ArrayList<AssignmentClass>
    ): Boolean {

        for(currentIndex in listOfOnlyEvents){

            if(currentIndex.name == "Blank"){
                return false
            }
        }

        return true
    }

    //This function is used when we are dealing with a mix of assignments and
    //scheduled events. We return back an ArrayList of scheduled Events.
    private fun grabEvents(
        listOfAssignments: MutableList<AssignmentClass>
    ): ArrayList<AssignmentClass> {

        val listOfOnlyEvents : ArrayList<AssignmentClass> = ArrayList()

        for(currentIndex in listOfAssignments){

            if(!currentIndex.isAssignmentClass){
                listOfOnlyEvents.add(currentIndex)
            }
        }

        return listOfOnlyEvents
    }

    //This function is used when we are dealing with a mix of assignments and
    //scheduled events. We return back an ArrayList of assignments.
    private fun grabAssignments(
        listOfAssignments: MutableList<AssignmentClass>
    ): ArrayList<AssignmentClass> {

        val listOfOnlyAssignments : ArrayList<AssignmentClass> = ArrayList()

        for(currentIndex in listOfAssignments){

            if(currentIndex.isAssignmentClass){
                listOfOnlyAssignments.add(currentIndex)
            }
        }

        return listOfOnlyAssignments
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

    //This function is used when we are dealing with a mix of assignments and scheduled events. This is used
    //to determine how many total minutes there are for the assignments outside of the scheduled events.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun mixedOnlyTotalMinutesDeterminer(
        listOfEvents: MutableList<AssignmentClass>
    ): Int {

        var totalMinutes = 0

        for(currentIndex in listOfEvents){

            if(currentIndex.name == "Blank"){
                val currentIndexStartTime = LocalTime.parse(currentIndex.startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                val currentIndexEndTime = LocalTime.parse(currentIndex.endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

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

    //This function is used when we are dealing with assignments. We're
    //assigning total minutes to each assignment based on difficulty.
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

            previousHourSubstring = currentTime.substring(0,1)			//Ex. Grabs the '3' part in '3:00 PM'
            previousMinuteSubstring = currentTime.substring(2,4)		//Ex. Grabs the '00' part in '3:00 PM'
            previousAbbreviationSubstring = currentTime.substring(5,7)	//Ex. Grabs the 'PM' part in '3:00 PM'
        }
        else{
            previousHourSubstring = currentTime.substring(0,2)          //Ex. Grabs the '10' part in '10:00 PM'
            previousMinuteSubstring = currentTime.substring(3,5)        //Ex. Grabs the '00' part in '10:00 PM'
            previousAbbreviationSubstring = currentTime.substring(6,8)  //Ex. Grabs the 'PM' part in '10:00 PM'
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

            return "$finalHour:$finalMinute $previousAbbreviationSubstring"
        }
        else{

            finalHour = previousHourSubstring
            finalMinute = newMinute.toString()

            if(finalMinute.toInt() <= 9){
                finalMinute = "0" + finalMinute
            }

            return "$finalHour:$finalMinute $previousAbbreviationSubstring"
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

            if(!currentIndex.isAssignmentClass){

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

            if(currentIndex.isAssignmentClass){

                count++
            }
        }

        return count
    }
}
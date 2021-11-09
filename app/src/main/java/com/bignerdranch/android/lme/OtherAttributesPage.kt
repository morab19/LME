package com.bignerdranch.android.lme

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class OtherAttributesPage : AppCompatActivity() {

    private lateinit var nextButton: Button
    private lateinit var nameOfEvent: EditText
    private lateinit var startHourTimeSpinner: Spinner
    private lateinit var endHourTimeSpinner: Spinner
    private lateinit var startHalfHourTimeSpinner: Spinner
    private lateinit var endHalfHourTimeSpinner: Spinner
    private lateinit var startTimeOfDaySpinner: Spinner
    private lateinit var endTimeOfDaySpinner: Spinner

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_attributes_page)

        //Information being passed around throughout the app.
        val listOfAssignments:MutableList<AssignmentClass> = intent.getSerializableExtra("key") as MutableList<AssignmentClass>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")
        val startTimeAbbreviation = intent.getStringExtra("startTimeZone")
        val endTimeAbbreviation = intent.getStringExtra("endTimeZone")

        nextButton = findViewById(R.id.next_button)
        nameOfEvent = findViewById(R.id.name_of_event_edit_text)
        startHourTimeSpinner = findViewById(R.id.starting_hour_spinner)
        startHalfHourTimeSpinner = findViewById(R.id.starting_half_hour_spinner)
        startTimeOfDaySpinner = findViewById(R.id.starting_time_of_day_spinner)
        endHourTimeSpinner = findViewById(R.id.ending_hour_spinner)
        endHalfHourTimeSpinner = findViewById(R.id.ending_half_hour_spinner)
        endTimeOfDaySpinner = findViewById(R.id.ending_time_of_day_spinner)

        val hoursArray = resources.getStringArray(R.array.hour_array)
        val hoursAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, hoursArray)
        startHourTimeSpinner.adapter = hoursAdapter
        endHourTimeSpinner.adapter = hoursAdapter

        val halfHourArray = resources.getStringArray(R.array.half_hour_array)
        val halfHourAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, halfHourArray)
        startHalfHourTimeSpinner.adapter = halfHourAdapter
        endHalfHourTimeSpinner.adapter = halfHourAdapter

        val timeOfDayArray = resources.getStringArray(R.array.am_pm_array)
        val timeOfDayAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, timeOfDayArray)
        startTimeOfDaySpinner.adapter = timeOfDayAdapter
        endTimeOfDaySpinner.adapter = timeOfDayAdapter

        if(startTimeAbbreviation != endTimeAbbreviation){

            val timeOfDayArray = resources.getStringArray(R.array.am_pm_array)
            val timeOfDayAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, timeOfDayArray)
            startTimeOfDaySpinner.adapter = timeOfDayAdapter
            endTimeOfDaySpinner.adapter = timeOfDayAdapter
        }
        else if( ((startTimeAbbreviation == "PM") && (endTimeAbbreviation == "PM")) ){

            val pmTimeOfDay = resources.getStringArray(R.array.pm_array)
            val pmTimeOfDayAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, pmTimeOfDay)
            startTimeOfDaySpinner.adapter = pmTimeOfDayAdapter
            endTimeOfDaySpinner.adapter = pmTimeOfDayAdapter
        }
        else{

            val amTimeOfDay = resources.getStringArray(R.array.am_array)
            val amTimeOfDayAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, amTimeOfDay)
            startTimeOfDaySpinner.adapter = amTimeOfDayAdapter
            endTimeOfDaySpinner.adapter = amTimeOfDayAdapter
        }

        nameOfEvent.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if( nameOfEvent.text.isNotEmpty()) {

                        nextButton.isEnabled = true
                }
                if( nameOfEvent.text.isEmpty()) {

                        nextButton.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        nextButton.setOnClickListener {

            val userStartTime : String = startHourTimeSpinner.selectedItem.toString() + ":" + startHalfHourTimeSpinner.selectedItem.toString() + " " + startTimeOfDaySpinner.selectedItem.toString()
            val convertedUserStartTime = LocalTime.parse(userStartTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            val originalStartTimeValue = LocalTime.parse(startTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val originalEndTimeValue = LocalTime.parse(endTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            val userEndTime : String = endHourTimeSpinner.selectedItem.toString() + ":" + endHalfHourTimeSpinner.selectedItem.toString() + " " + endTimeOfDaySpinner.selectedItem.toString()
            val convertedUserEndTime = LocalTime.parse(userEndTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if( checkNames(listOfAssignments)
                && checkStartTime(convertedUserStartTime, originalStartTimeValue, originalEndTimeValue)
                && checkEndTime(convertedUserEndTime, originalStartTimeValue, originalEndTimeValue)) {

                nameOfEvent.setText("")
                val messageRedId = R.string.name_already_exists_and_both_times_invalid
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkNames(listOfAssignments)
                     && checkStartTime(convertedUserStartTime, originalStartTimeValue, originalEndTimeValue) ){

                nameOfEvent.setText("")
                val messageRedId = R.string.name_already_exists_and_start_time_invalid
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkNames(listOfAssignments)
                     && checkEndTime(convertedUserEndTime, originalStartTimeValue, originalEndTimeValue) ){

                nameOfEvent.setText("")
                val messageRedId = R.string.name_already_exists_and_end_time_invalid
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkStartTime(convertedUserStartTime, originalStartTimeValue, originalEndTimeValue)
                     && checkEndTime(convertedUserEndTime, originalStartTimeValue, originalEndTimeValue) ){

                val messageRedId = R.string.both_times_invalid
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkStartTime(convertedUserStartTime, originalStartTimeValue, originalEndTimeValue) ){

                val messageRedId = R.string.start_time_invalid
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkEndTime(convertedUserEndTime, originalStartTimeValue, originalEndTimeValue) ){

                val messageRedId2 = R.string.end_time_invalid
                Toast.makeText(baseContext, messageRedId2, Toast.LENGTH_SHORT).show()
            }
            else if( checkNames(listOfAssignments)
                     && checkForOverlap(listOfAssignments, convertedUserStartTime, convertedUserEndTime) ){

                nameOfEvent.setText("")
                val messageRedId2 = R.string.name_already_exists_and_overlap_detected
                Toast.makeText(baseContext, messageRedId2, Toast.LENGTH_LONG).show()
            }
            else if( checkNames(listOfAssignments) ){

                nameOfEvent.setText("")
                val messageRedId = R.string.name_already_exists
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkForOverlap(listOfAssignments, convertedUserStartTime, convertedUserEndTime) ){

                val messageRedId = R.string.overlap_detected
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else{

                val intent = Intent(this, AddMorePage::class.java)

                val c = AssignmentClass(
                    difficulty = 0,
                    name = nameOfEvent.text.toString(),
                    booleanClass = false,
                    startTime = userStartTime,
                    endTime = userEndTime
                )

                listOfAssignments.add(c)
                intent.putExtra("key", listOfAssignments as Serializable)
                intent.putExtra("startTimeValue", startTimeValue)
                intent.putExtra("endTimeValue", endTimeValue)
                intent.putExtra("startTimeZone", startTimeAbbreviation)
                intent.putExtra("endTimeZone", endTimeAbbreviation)
                startActivity(intent)
            }
        }
    }

    private fun checkNames(listOfAssignments:MutableList<AssignmentClass>): Boolean {

        if(listOfAssignments.isNotEmpty()){

            for(currentIndex in listOfAssignments){

                if (currentIndex.name == nameOfEvent.text.toString()){
                    return true
                }
            }
        }

        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkStartTime(
        convertedUserStartTime: LocalTime,
        originalStartTimeValue: LocalTime,
        originalEndTimeValue: LocalTime
    ): Boolean{

        return (convertedUserStartTime.isBefore(originalStartTimeValue)
                || convertedUserStartTime.isAfter(originalEndTimeValue)
                || convertedUserStartTime == originalEndTimeValue)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkEndTime(
        convertedUserEndTime: LocalTime,
        originalStartTimeValue: LocalTime,
        originalEndTimeValue: LocalTime
    ): Boolean{

        return (convertedUserEndTime.isAfter(originalEndTimeValue)
                || convertedUserEndTime.isBefore(originalStartTimeValue)
                || convertedUserEndTime == originalStartTimeValue)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkForOverlap(
        listOfAssignments: MutableList<AssignmentClass>,
        convertedUserStartTime: LocalTime,
        convertedUserEndTime: LocalTime
    ): Boolean{

        if(listOfAssignments.isNotEmpty()){

            for(currentIndex in listOfAssignments){

                if(!currentIndex.booleanClass) {

                    val currentIndexStartTime = LocalTime.parse(currentIndex.startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                    val currentIndexEndTime = LocalTime.parse(currentIndex.endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                    if(convertedUserStartTime == currentIndexStartTime
                        || convertedUserEndTime == currentIndexEndTime
                        || ( (convertedUserStartTime == currentIndexStartTime) && (convertedUserEndTime == currentIndexEndTime) )
                        || ( (convertedUserStartTime < currentIndexStartTime) && (convertedUserEndTime > currentIndexEndTime) ) ){

                        return true
                    }
                }
            }

            return false
        }

        return false
    }
}
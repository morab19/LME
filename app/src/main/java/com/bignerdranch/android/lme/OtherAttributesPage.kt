package com.bignerdranch.android.lme

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class OtherAttributesPage : AppCompatActivity() {

    private lateinit var nextButton: Button
    private lateinit var nameOfEvent: EditText
    private lateinit var startOfEventEditText: EditText
    private lateinit var endOfEventEditText: EditText
    private lateinit var startTimeToggleButton: Button
    private lateinit var endTimeToggleButton: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_attributes_page)

        nextButton = findViewById(R.id.next_button)
        nameOfEvent = findViewById(R.id.name_of_event_edit_text)
        startOfEventEditText = findViewById(R.id.start_time_edit_text)
        endOfEventEditText = findViewById(R.id.end_time_edit_text)
        startTimeToggleButton = findViewById(R.id.start_toggle_button)
        endTimeToggleButton = findViewById(R.id.end_toggle_button)

        val listOfAssignments:MutableList<AssignmentClass> = intent.getSerializableExtra("key") as MutableList<AssignmentClass>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")
        val startTimeAbbreviation = intent.getStringExtra("startTimeZone")
        val endTimeAbbreviation = intent.getStringExtra("endTimeZone")

        startTimeToggleButton.text = startTimeAbbreviation
        endTimeToggleButton.text = endTimeAbbreviation

        startTimeToggleButton.setOnClickListener{

            if(startTimeAbbreviation != endTimeAbbreviation){

                if(startTimeToggleButton.text.toString() == "am"){
                    startTimeToggleButton.text = "pm"
                }
                else{
                    startTimeToggleButton.text = "am"
                }
            }
        }

        endTimeToggleButton.setOnClickListener{

            if(startTimeAbbreviation != endTimeAbbreviation) {

                if (endTimeToggleButton.text.toString() == "am") {
                    endTimeToggleButton.text = "pm"
                } else {
                    endTimeToggleButton.text = "am"
                }
            }
        }

        nameOfEvent.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if( nameOfEvent.text.isNotEmpty()
                    && startOfEventEditText.text.isNotEmpty()
                    && endOfEventEditText.text.isNotEmpty()) {

                        nextButton.isEnabled = true
                }
                if( nameOfEvent.text.isEmpty()
                    || startOfEventEditText.text.isEmpty()
                    || endOfEventEditText.text.isEmpty()) {

                        nextButton.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        startOfEventEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if( nameOfEvent.text.isNotEmpty()
                    && startOfEventEditText.text.isNotEmpty()
                    && endOfEventEditText.text.isNotEmpty()) {

                        nextButton.isEnabled = true
                }
                if( nameOfEvent.text.isEmpty()
                    || startOfEventEditText.text.isEmpty()
                    || endOfEventEditText.text.isEmpty()) {

                        nextButton.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })

        endOfEventEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if( nameOfEvent.text.isNotEmpty()
                    && startOfEventEditText.text.isNotEmpty()
                    && endOfEventEditText.text.isNotEmpty()) {

                        nextButton.isEnabled = true
                }
                if( nameOfEvent.text.isEmpty()
                    || startOfEventEditText.text.isEmpty()
                    || endOfEventEditText.text.isEmpty()) {

                        nextButton.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        nextButton.setOnClickListener {

            var userStartTime : String = startOfEventEditText.text.toString() + ":00 " + startTimeToggleButton.text.toString().uppercase()
            var convertedUserStartTime = LocalTime.parse(userStartTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            var originalStartTimeValue = LocalTime.parse(startTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            var originalEndTimeValue = LocalTime.parse(endTimeValue, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            var userEndTime : String = endOfEventEditText.text.toString() + ":00 " + endTimeToggleButton.text.toString().uppercase()
            var convertedUserEndTime = LocalTime.parse(userEndTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if( checkNames(listOfAssignments)
                && checkStartTime(convertedUserStartTime, originalStartTimeValue, originalEndTimeValue)
                && checkEndTime(convertedUserEndTime, originalStartTimeValue, originalEndTimeValue)) {

                nameOfEvent.setText("")
                startOfEventEditText.setText("")
                endOfEventEditText.setText("")
                val messageRedId = R.string.name_already_exists_and_both_times_invalid
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkNames(listOfAssignments)
                     && checkStartTime(convertedUserStartTime, originalStartTimeValue, originalEndTimeValue) ){

                nameOfEvent.setText("")
                startOfEventEditText.setText("")
                val messageRedId = R.string.name_already_exists_and_start_time_invalid
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkNames(listOfAssignments)
                     && checkEndTime(convertedUserEndTime, originalStartTimeValue, originalEndTimeValue) ){

                nameOfEvent.setText("")
                endOfEventEditText.setText("")
                val messageRedId = R.string.name_already_exists_and_end_time_invalid
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkStartTime(convertedUserStartTime, originalStartTimeValue, originalEndTimeValue)
                     && checkEndTime(convertedUserEndTime, originalStartTimeValue, originalEndTimeValue) ){

                startOfEventEditText.setText("")
                endOfEventEditText.setText("")
                val messageRedId = R.string.both_times_invalid
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkStartTime(convertedUserStartTime, originalStartTimeValue, originalEndTimeValue) ){

                startOfEventEditText.setText("")
                val messageRedId = R.string.start_time_invalid
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkEndTime(convertedUserEndTime, originalStartTimeValue, originalEndTimeValue) ){

                endOfEventEditText.setText("")
                val messageRedId2 = R.string.end_time_invalid
                Toast.makeText(baseContext, messageRedId2, Toast.LENGTH_SHORT).show()
            }
            else if( checkNames(listOfAssignments)
                     && checkForOverlap(listOfAssignments, convertedUserStartTime, convertedUserEndTime) ){

                nameOfEvent.setText("")
                startOfEventEditText.setText("")
                endOfEventEditText.setText("")
                val messageRedId2 = R.string.name_already_exists_and_overlap_detected
                Toast.makeText(baseContext, messageRedId2, Toast.LENGTH_LONG).show()
            }
            else if( checkNames(listOfAssignments) ){

                nameOfEvent.setText("")
                val messageRedId = R.string.name_already_exists
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( checkForOverlap(listOfAssignments, convertedUserStartTime, convertedUserEndTime) ){
                startOfEventEditText.setText("")
                endOfEventEditText.setText("")
                val messageRedId = R.string.overlap_detected
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else{

                val intent = Intent(this, AddMorePage::class.java)

                var c = AssignmentClass(
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

                    var currentIndexStartTime = LocalTime.parse(currentIndex.startTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                    var currentIndexEndTime = LocalTime.parse(currentIndex.endTime, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

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
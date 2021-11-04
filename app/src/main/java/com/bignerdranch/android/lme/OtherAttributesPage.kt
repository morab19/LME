package com.bignerdranch.android.lme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.Serializable

class OtherAttributesPage : AppCompatActivity() {

    private lateinit var nextButton: Button
    private lateinit var nameOfEvent: EditText
    private lateinit var startOfEventEditText: EditText
    private lateinit var endOfEventEditText: EditText
    private lateinit var startTimeToggleButton: Button
    private lateinit var endTimeToggleButton: Button

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
        val startTimeZone = intent.getStringExtra("startTimeZone")
        val endTimeZone = intent.getStringExtra("endTimeZone")

        startTimeToggleButton.text = startTimeZone
        endTimeToggleButton.text = endTimeZone

        startTimeToggleButton.setOnClickListener{

            if(startTimeZone != endTimeZone){

                if(startTimeToggleButton.text.toString() == "am"){
                    startTimeToggleButton.setText("pm")
                }
                else{
                    startTimeToggleButton.setText("am")
                }
            }
        }

        endTimeToggleButton.setOnClickListener{

            if(startTimeZone != endTimeZone) {

                if (endTimeToggleButton.text.toString() == "am") {
                    endTimeToggleButton.setText("pm")
                } else {
                    endTimeToggleButton.setText("am")
                }
            }
        }

        nameOfEvent.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if( nameOfEvent.text.isNotEmpty() && startOfEventEditText.text.isNotEmpty() && endOfEventEditText.text.isNotEmpty()) {
                    nextButton.isEnabled = true
                }
                if( nameOfEvent.text.isEmpty() || startOfEventEditText.text.isEmpty() || endOfEventEditText.text.isEmpty()) {
                    nextButton.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

                if(listOfAssignments.isNotEmpty()){

                    for (currentIndex in listOfAssignments){

                        if (currentIndex.name == nameOfEvent.text.toString()){
                            nameOfEvent.setText("")
                            val messageRedId = R.string.name_already_exists
                            Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })

        startOfEventEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if( nameOfEvent.text.isNotEmpty() && startOfEventEditText.text.isNotEmpty() && endOfEventEditText.text.isNotEmpty()) {
                    nextButton.isEnabled = true
                }
                if( nameOfEvent.text.isEmpty() || startOfEventEditText.text.isEmpty() || endOfEventEditText.text.isEmpty()) {
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

                if( nameOfEvent.text.isNotEmpty() && startOfEventEditText.text.isNotEmpty() && endOfEventEditText.text.isNotEmpty()) {
                    nextButton.isEnabled = true
                }
                if( nameOfEvent.text.isEmpty() || startOfEventEditText.text.isEmpty() || endOfEventEditText.text.isEmpty()) {
                    nextButton.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })

        nextButton.setOnClickListener {
            val intent = Intent(this, AddMorePage::class.java)

            var c = AssignmentClass(
                difficulty = 0,
                name = nameOfEvent.text.toString(),
                booleanClass = false
            )

            listOfAssignments.add(c)
            intent.putExtra("key", listOfAssignments as Serializable)
            intent.putExtra("startTimeValue", startTimeValue)
            intent.putExtra("endTimeValue", endTimeValue)
            intent.putExtra("startTimeZone", startTimeZone)
            intent.putExtra("endTimeZone", endTimeZone)
            startActivity(intent)
        }
    }
}
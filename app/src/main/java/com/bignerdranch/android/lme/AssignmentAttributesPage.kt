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

class AssignmentAttributesPage : AppCompatActivity() {

    //Initialize all objects on the android screen we will be interacting with.
    private lateinit var nextButton: Button
    private lateinit var difficultyValue: EditText
    private lateinit var nameOfAssignment: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment_attributes_page)

        //Connecting all objects on the android screen to
        //variable names we will be interacting with.
        difficultyValue = findViewById(R.id.difficulty_of_assignment_edit_text)
        nameOfAssignment = findViewById(R.id.name_of_assignment_edit_text)
        nextButton = findViewById(R.id.next_button)

        //Information being passed around throughout the app.
        val listOfAssignments:MutableList<AssignmentClass> = intent.getSerializableExtra("key") as MutableList<AssignmentClass>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")
        val startTimeAbbreviation = intent.getStringExtra("startTimeZone")
        val endTimeAbbreviation = intent.getStringExtra("endTimeZone")

        var num: Int
        var str: String

        //This editText holds the difficulty of the assignment
        difficultyValue.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Enables the "NEXT" button when there are no empty editTexts
                if( difficultyValue.text.isNotEmpty() && nameOfAssignment.text.isNotEmpty()){
                    nextButton.isEnabled = true
                }
                if( difficultyValue.text.isEmpty() || nameOfAssignment.text.isEmpty() ){
                    nextButton.isEnabled = false
                }
                //This block of code was added to keep
                //it from crashing when backspacing.
                str = difficultyValue.text.toString()
                if(str != ""){
                    num = Integer.parseInt(str)
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })

        //This editText holds the name of the Assignment
        nameOfAssignment.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Enables the "NEXT" button when there are no empty editTexts
                if( difficultyValue.text.isNotEmpty() && nameOfAssignment.text.isNotEmpty()){
                    nextButton.isEnabled = true
                }
                if( difficultyValue.text.isEmpty() || nameOfAssignment.text.isEmpty() ){
                    nextButton.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })

        //The following code executes when the user clicks the "NEXT" Button
        nextButton.setOnClickListener {

            //If statement executes if the set difficulty is not a
            //value ranging from 1-7 preventing the user from continuing.
            if( (difficultyValue.text.toString().toInt() < 1) || (difficultyValue.text.toString().toInt() > 7) ){
                val messageRedId = R.string.invalid_difficulty_value
                Toast.makeText(this, messageRedId, Toast.LENGTH_SHORT).show()
            }
            //Else if statement executes if there's another assignment or scheduled event with the
            //same name as the name just inputted by the user preventing them from continuing.
            else if( checkNames(listOfAssignments) ){
                nameOfAssignment.setText("")
                val messageRedId = R.string.name_already_exists
                Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else{

                //The assignment is then added to an ArrayList.
                val intent = Intent(this, AddMorePage::class.java)

                val c = AssignmentClass(
                    difficulty = difficultyValue.text.toString().toInt(),
                    name = nameOfAssignment.text.toString(),
                    booleanClass = true,
                    startTime = "Blank",
                    endTime = "Blank"
                )

                listOfAssignments.add(c)
                //Moving on to the next screen.
                intent.putExtra("key", listOfAssignments as Serializable)
                intent.putExtra("startTimeValue", startTimeValue)
                intent.putExtra("endTimeValue", endTimeValue)
                intent.putExtra("startTimeZone", startTimeAbbreviation)
                intent.putExtra("endTimeZone", endTimeAbbreviation)
                startActivity(intent)
            }
        }
    }

    //This function takes an ArrayList of events and checks to see if the user inputs a
    //name that already exists in the list. If it does return true otherwise return false.
    private fun checkNames(listOfAssignments:MutableList<AssignmentClass>): Boolean {

        if(listOfAssignments.isNotEmpty()){

            for (currentIndex in listOfAssignments){

                if (currentIndex.name == nameOfAssignment.text.toString()){
                    return true
                }
            }
        }

        return false
    }
}
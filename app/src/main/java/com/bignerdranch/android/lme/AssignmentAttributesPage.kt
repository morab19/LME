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

    private lateinit var nextButton: Button
    private lateinit var difficultyValue: EditText
    private lateinit var nameOfAssignment: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment_attributes_page)

        difficultyValue = findViewById(R.id.difficulty_of_assignment_edit_text)
        nameOfAssignment = findViewById(R.id.name_of_assignment_edit_text)
        nextButton = findViewById(R.id.next_button)

        val listOfAssignments:MutableList<AssignmentClass> = intent.getSerializableExtra("key") as MutableList<AssignmentClass>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")
        val startTimeZone = intent.getStringExtra("startTimeZone")
        val endTimeZone = intent.getStringExtra("endTimeZone")

        var num: Int
        var str: String

        difficultyValue.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if( difficultyValue.text.isNotEmpty() && nameOfAssignment.text.isNotEmpty()){
                    nextButton.isEnabled = true
                }
                if( difficultyValue.text.isEmpty() || nameOfAssignment.text.isEmpty() ){
                    nextButton.isEnabled = false
                }

                //This block of code was added to keep
                //it from crashing when backspacing.

                str = difficultyValue.text.toString()
                if(!str.equals("")){
                    num = Integer.parseInt(str)
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })

        nameOfAssignment.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if( difficultyValue.text.isNotEmpty() && nameOfAssignment.text.isNotEmpty()){
                    nextButton.isEnabled = true
                }
                if( difficultyValue.text.isEmpty() || nameOfAssignment.text.isEmpty() ){
                    nextButton.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

                if(listOfAssignments.isNotEmpty()){

                    for (currentIndex in listOfAssignments){

                        if (currentIndex.name == nameOfAssignment.text.toString()){
                            nameOfAssignment.setText("")
                            val messageRedId = R.string.name_already_exists
                            Toast.makeText(baseContext, messageRedId, Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        })
        nextButton.setOnClickListener {

            if ((difficultyValue.text.toString().toInt() < 1) || (difficultyValue.text.toString().toInt() > 7)) {
                val messageRedId = R.string.invalid_difficulty_value
                Toast.makeText(this, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this, AddMorePage::class.java)

                var c = AssignmentClass(
                    difficulty = difficultyValue.text.toString().toInt(),
                    name = nameOfAssignment.text.toString(),
                    booleanClass = true
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
}
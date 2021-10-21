package com.bignerdranch.android.lme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText

class OtherAttributesPage : AppCompatActivity() {

    private lateinit var nextButton: Button
    private lateinit var nameOfEvent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_attributes_page)

        nextButton = findViewById(R.id.next_button)
        nameOfEvent = findViewById(R.id.name_of_event_edit_text)

        nameOfEvent.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if( nameOfEvent.text.isNotEmpty() ){
                    nextButton.isEnabled = true
                }
                if( nameOfEvent.text.isEmpty() ){
                    nextButton.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        nextButton.setOnClickListener {
            val intent = Intent(this, AddMorePage::class.java)
            startActivity(intent)
        }
    }
}
package com.example.tenantmanagement

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tenantNameEditText =
            findViewById<EditText>(R.id.tenantNameEditText)

        val phoneEditText =
            findViewById<EditText>(R.id.phoneEditText)

        val rentEditText =
            findViewById<EditText>(R.id.rentEditText)

        val saveButton =
            findViewById<Button>(R.id.saveButton)

        val resultTextView =
            findViewById<TextView>(R.id.resultTextView)

        saveButton.setOnClickListener {

            val name = tenantNameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val rent = rentEditText.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty() || rent.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                val tenant = Tenant(
                    name = name,
                    phone = phone,
                    rent = rent
                )

                resultTextView.text = tenant.summary()

                Toast.makeText(
                    this,
                    "Tenant saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
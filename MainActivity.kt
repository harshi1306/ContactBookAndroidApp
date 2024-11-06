package com.example.ca2

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contactNameEditText: EditText = findViewById(R.id.ContactName)
        val phoneNumberEditText: EditText = findViewById(R.id.PhoneNumber)
        val deleteContactEditText: EditText = findViewById(R.id.DeleteContact)
        val addContactButton: Button = findViewById(R.id.addContactButton)
        val deleteContactButton: Button = findViewById(R.id.deleteContactButton)
        val contactListView: ListView = findViewById(R.id.contactListView)

        val db = SQLLiteDBHelper(this, null)

        // Load and display contacts
        fun loadContacts() {
            val cursor = db.getAllContacts()
            val contacts = mutableListOf<Contact>()

            cursor?.let {
                while (cursor.moveToNext()) {
                    val contactId = cursor.getInt(cursor.getColumnIndexOrThrow(SQLLiteDBHelper.ID_COL))
                    val contactName = cursor.getString(cursor.getColumnIndexOrThrow(SQLLiteDBHelper.NAME_COL))
                    val contactPhone = cursor.getString(cursor.getColumnIndexOrThrow(SQLLiteDBHelper.PHONE_COL))
                    contacts.add(Contact(contactId, contactName, contactPhone))
                }
            }
            cursor?.close()

            val adapter = ContactAdapter(this, contacts)
            contactListView.adapter = adapter
        }

        addContactButton.setOnClickListener {
            val contactName = contactNameEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()

            if (contactName.isNotEmpty() && phoneNumber.isNotEmpty()) {
                db.addContact(contactName, phoneNumber)
                Toast.makeText(this, "$contactName added to database", Toast.LENGTH_LONG).show()
                contactNameEditText.text.clear()
                phoneNumberEditText.text.clear()
                loadContacts()
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }

        deleteContactButton.setOnClickListener {
            val contactId = deleteContactEditText.text.toString().toIntOrNull()
            if (contactId != null) {
                db.deleteContact(contactId)
                Toast.makeText(this, "Contact deleted", Toast.LENGTH_LONG).show()
                deleteContactEditText.text.clear()
                loadContacts()
            } else {
                Toast.makeText(this, "Enter a valid Contact ID", Toast.LENGTH_SHORT).show()
            }
        }

        // Load contacts when the app starts
        loadContacts()
    }
}


package com.example.tenantmanagement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class TenantFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_tenant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = view.findViewById<EditText>(R.id.tenantNameEditText)
        val phone = view.findViewById<EditText>(R.id.tenantPhoneEditText)
        val email = view.findViewById<EditText>(R.id.tenantEmailEditText)
        val house = view.findViewById<EditText>(R.id.tenantHouseEditText)
        val save = view.findViewById<Button>(R.id.saveTenantButton)

        save.setOnClickListener {
            val tenantName = name.text.toString().trim()
            val tenantPhone = phone.text.toString().trim()
            val tenantEmail = email.text.toString().trim()
            val tenantHouse = house.text.toString().trim()

            if (tenantName.isEmpty() || tenantPhone.isEmpty() ||
                tenantEmail.isEmpty() || tenantHouse.isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val prefs = requireContext().getSharedPreferences(
                    "tenant_records",
                    Context.MODE_PRIVATE
                )

                val id = System.currentTimeMillis().toString()

                prefs.edit()
                    .putString("${id}_name", tenantName)
                    .putString("${id}_phone", tenantPhone)
                    .putString("${id}_email", tenantEmail)
                    .putString("${id}_house", tenantHouse)
                    .apply()

                Toast.makeText(
                    requireContext(),
                    "Tenant saved successfully!",
                    Toast.LENGTH_LONG
                ).show()

                name.text.clear()
                phone.text.clear()
                email.text.clear()
                house.text.clear()
            }
        }
    }
}

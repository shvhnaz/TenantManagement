
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
import androidx.navigation.fragment.findNavController
import org.json.JSONArray
import org.json.JSONObject

class AddTenantFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_add_tenant,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameInput =
            view.findViewById<EditText>(R.id.tenantNameEditText)
        val phoneInput =
            view.findViewById<EditText>(R.id.tenantPhoneEditText)
        val rentInput =
            view.findViewById<EditText>(R.id.tenantRentEditText)
        val saveButton =
            view.findViewById<Button>(R.id.saveTenantButton)

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val rentText = rentInput.text.toString().trim()
            val rentAmount = rentText.toDoubleOrNull()

            if (
                name.isBlank() ||
                phone.isBlank() ||
                rentAmount == null ||
                rentAmount < 0
            ) {
                Toast.makeText(
                    requireContext(),
                    "Enter a name, phone number and valid rent",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Create the Tenant object that Safe Args will pass.
            val tenant = Tenant(
                name = name,
                phone = phone,
                rent = rentAmount.toString()
            )

            // Save the tenant to SharedPreferences.
            val preferences = requireContext().getSharedPreferences(
                "tenant_data",
                Context.MODE_PRIVATE
            )

            val tenants = try {
                JSONArray(preferences.getString("tenants", "[]"))
            } catch (e: Exception) {
                JSONArray()
            }

            val tenantJson = JSONObject().apply {
                put("name", tenant.name)
                put("phone", tenant.phone)
                put("rent", rentAmount)
            }

            tenants.put(tenantJson)

            preferences.edit()
                .putString("tenants", tenants.toString())
                .apply()

            Toast.makeText(
                requireContext(),
                "Tenant saved successfully!",
                Toast.LENGTH_SHORT
            ).show()

            // Navigate using the Safe Args generated direction.
            val action =
                AddTenantFragmentDirections
                    .actionAddTenantFragmentToViewTenantsFragment(tenant)

            findNavController().navigate(action)
        }
    }
}

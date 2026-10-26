
package com.example.tenantmanagement

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.json.JSONArray

class ViewTenantsFragment : Fragment() {

    private val args: ViewTenantsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_view_tenants,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tenantListText =
            view.findViewById<TextView>(R.id.tenantListText)
        val callButton =
            view.findViewById<Button>(R.id.callTenantButton)

        // Receive the Tenant object passed through Safe Args.
        val receivedTenant: Tenant? = args.tenant

        val preferences = requireContext().getSharedPreferences(
            "tenant_data",
            Context.MODE_PRIVATE
        )

        val tenants = try {
            JSONArray(preferences.getString("tenants", "[]"))
        } catch (e: Exception) {
            JSONArray()
        }

        val displayText = StringBuilder()
        var firstPhoneNumber: String? = null

        // Show all saved tenants.
        for (i in 0 until tenants.length()) {
            val tenant = tenants.getJSONObject(i)
            val name = tenant.optString("name")
            val phone = tenant.optString("phone")
            val rent = tenant.optDouble("rent", 0.0)

            if (firstPhoneNumber == null && phone.isNotBlank()) {
                firstPhoneNumber = phone
            }

            displayText.append(
                "${i + 1}. $name\n" +
                        "Phone: $phone\n" +
                        "Rent: KSh ${"%.2f".format(rent)}\n\n"
            )
        }

        // Identify the tenant received through Safe Args.
        if (receivedTenant != null) {
            displayText.insert(
                0,
                "Recently added tenant: ${receivedTenant.name}\n\n"
            )
        }

        if (tenants.length() == 0) {
            tenantListText.text = if (receivedTenant != null) {
                "Recently added tenant:\n${receivedTenant.summary()}"
            } else {
                "No tenants added yet."
            }

            callButton.isEnabled = receivedTenant != null &&
                    receivedTenant.phone.isNotBlank()

            if (receivedTenant != null) {
                firstPhoneNumber = receivedTenant.phone
            }
        } else {
            tenantListText.text = displayText.toString().trim()
            callButton.isEnabled = true
        }

        callButton.setOnClickListener {
            val phoneNumber = firstPhoneNumber

            if (phoneNumber.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    "No phone number available",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val dialIntent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:$phoneNumber")
                )
                startActivity(dialIntent)
            }
        }
    }
}

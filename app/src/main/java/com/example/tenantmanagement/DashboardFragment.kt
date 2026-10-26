
package com.example.tenantmanagement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.json.JSONArray
import java.util.Locale

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_dashboard,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.addTenantButton)
            .setOnClickListener {
                findNavController().navigate(
                    R.id.action_dashboardFragment_to_addTenantFragment
                )
            }

        view.findViewById<Button>(R.id.viewTenantsButton)
            .setOnClickListener {
                findNavController().navigate(
                    R.id.action_dashboardFragment_to_viewTenantsFragment
                )
            }
    }

    override fun onResume() {
        super.onResume()
        updateDashboard()
    }

    private fun updateDashboard() {
        val preferences = requireContext().getSharedPreferences(
            "tenant_data",
            Context.MODE_PRIVATE
        )

        val tenants = try {
            JSONArray(preferences.getString("tenants", "[]"))
        } catch (e: Exception) {
            JSONArray()
        }

        var totalRent = 0.0

        for (i in 0 until tenants.length()) {
            totalRent += tenants.getJSONObject(i).optDouble("rent", 0.0)
        }

        view?.findViewById<TextView>(R.id.tenantCountText)?.text =
            "Total Tenants: ${tenants.length()}"

        view?.findViewById<TextView>(R.id.totalRentText)?.text =
            "Total Rent: KSh ${String.format(Locale.getDefault(), "%.2f", totalRent)}"
    }
}

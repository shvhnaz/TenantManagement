
package com.example.tenantmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.security.MessageDigest

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameInput = view.findViewById<EditText>(R.id.fullNameEditText)
        val emailInput = view.findViewById<EditText>(R.id.registerEmailEditText)
        val passwordInput = view.findViewById<EditText>(R.id.registerPasswordEditText)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val backButton = view.findViewById<Button>(R.id.backToLoginButton)

        val prefs = requireContext().getSharedPreferences(
            "tenant_auth",
            android.content.Context.MODE_PRIVATE
        )

        registerButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim().lowercase()
            val password = passwordInput.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hash = MessageDigest.getInstance("SHA-256")
                .digest(password.toByteArray())
                .joinToString("") { "%02x".format(it) }

            prefs.edit()
                .putString("name", name)
                .putString("email", email)
                .putString("password_hash", hash)
                .apply()

            Toast.makeText(requireContext(), "Account created! Please log in.", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}

package com.example.firebase_angelma67

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Vincular vistas con sus IDs del XML
        val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)
        val signButton = findViewById<Button>(R.id.signbutton)
        val loginButton = findViewById<Button>(R.id.loginbutton)

        // 1. Botón de Registro ("registrar")
        signButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Registro exitoso -> Ir a la pantalla principal
                            irAMainActivity()
                        } else {
                            // Error en el registro
                            Toast.makeText(
                                baseContext,
                                "Error al registrar: ${task.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, completa los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Botón de Iniciar Sesión ("Acceder")
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Login exitoso -> Ir a la pantalla principal
                            irAMainActivity()
                        } else {
                            // Error al acceder (credenciales incorrectas)
                            Toast.makeText(
                                baseContext,
                                "Error al acceder: ${task.exception?.message}",
                                Toast.LENGTH_LONG,
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, completa los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun irAMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Cierra el Login para que al pulsar "Atrás" no vuelva a aparecer
    }
}
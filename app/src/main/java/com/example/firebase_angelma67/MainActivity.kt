package com.example.firebase_angelma67

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvNombreUsuario = findViewById<TextView>(R.id.tvNombreUsuario)
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            val nombreRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(uid)
                .child("nombre")

            nombreRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombre = snapshot.getValue(String::class.java)
                    if (!nombre.isNullOrEmpty()) {
                        tvNombreUsuario.text = nombre
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    tvNombreUsuario.text = "Error"
                }
            })
        }
    }
}
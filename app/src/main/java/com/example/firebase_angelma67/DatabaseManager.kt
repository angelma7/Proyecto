package com.example.firebase_angelma67

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DatabaseManager(uid: String) {

    private val userRef: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("usuarios")
        .child(uid)

    fun escucharNombreUsuario(onResultado: (String) -> Unit) {
        userRef.child("nombre").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nombre = snapshot.getValue(String::class.java)
                if (!nombre.isNullOrEmpty()) {
                    onResultado(nombre)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onResultado("Error")
            }
        })
    }

    fun escucharIngredientes(onResultado: (List<String>, List<String>) -> Unit) {
        userRef.child("ingredientes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ingredientes = ArrayList<String>()
                val claves = ArrayList<String>()

                for (hijo in snapshot.children) {
                    val alimento = hijo.getValue(String::class.java)
                    val clave = hijo.key
                    if (!alimento.isNullOrEmpty() && clave != null) {
                        ingredientes.add(alimento)
                        claves.add(clave)
                    }
                }
                onResultado(ingredientes, claves)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun anadirIngrediente(
        alimento: String,
        onExito: () -> Unit,
        onError: () -> Unit
    ) {
        userRef.child("ingredientes").push().setValue(alimento)
            .addOnSuccessListener { onExito() }
            .addOnFailureListener { onError() }
    }

    fun eliminarIngrediente(clave: String) {
        userRef.child("ingredientes").child(clave).removeValue()
    }
}
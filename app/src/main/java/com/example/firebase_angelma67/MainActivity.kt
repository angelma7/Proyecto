package com.example.firebase_angelma67

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var listaIngredientes: ArrayList<String>
    private lateinit var listaClaves: ArrayList<String>
    private lateinit var adaptador: ArrayAdapter<String>
    private lateinit var dbManager: DatabaseManager
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvNombreUsuario = findViewById<TextView>(R.id.tvNombreUsuario)
        val etNuevoIngrediente = findViewById<EditText>(R.id.etNuevoIngrediente)
        val btnAnadirIngrediente = findViewById<Button>(R.id.btnAnadirIngrediente)
        val lvIngredientes = findViewById<ListView>(R.id.lvIngredientes)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        listaIngredientes = ArrayList()
        listaClaves = ArrayList()
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaIngredientes)
        lvIngredientes.adapter = adaptador

        dbHelper = DbHelper(this)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Toast.makeText(this, "No hay sesión iniciada", Toast.LENGTH_SHORT).show()
            return
        }

        dbManager = DatabaseManager(uid)

        dbManager.escucharNombreUsuario { nombre ->
            tvNombreUsuario.text = nombre
        }

        dbManager.escucharIngredientes { ingredientes, claves ->
            listaIngredientes.clear()
            listaIngredientes.addAll(ingredientes)
            listaClaves.clear()
            listaClaves.addAll(claves)
            adaptador.notifyDataSetChanged()
        }

        btnAnadirIngrediente.setOnClickListener {
            val alimento = etNuevoIngrediente.text.toString().trim()
            if (alimento.isEmpty()) {
                etNuevoIngrediente.error = "Escribe un alimento"
                return@setOnClickListener
            }

            dbHelper.insertarIngrediente(alimento)

            dbManager.anadirIngrediente(
                alimento = alimento,
                onExito = {
                    etNuevoIngrediente.text.clear()
                    Toast.makeText(this, "Añadido: $alimento", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(this, "No se pudo guardar", Toast.LENGTH_SHORT).show()
                }
            )
        }

        lvIngredientes.setOnItemLongClickListener { _, _, position, _ ->
            val clave = listaClaves[position]
            val alimento = listaIngredientes[position]
            dbManager.eliminarIngrediente(clave)
            dbHelper.eliminarIngredientePorNombre(alimento)
            Toast.makeText(this, "Eliminado: $alimento", Toast.LENGTH_SHORT).show()
            true
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_recetas -> {
                    Toast.makeText(this, "Recetas", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_favoritos -> {
                    Toast.makeText(this, "Favoritos", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_ajustes -> {
                    Toast.makeText(this, "Ajustes", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}
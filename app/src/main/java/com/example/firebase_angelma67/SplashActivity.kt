package com.example.firebase_angelma67

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Ocultar la barra superior si aparece
        supportActionBar?.hide()

        // Temporizador de 2 segundos (2000 milisegundos) para pasar al Login
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Cierra el Splash para que al pulsar atrás no vuelva a él
        }, 3000)
    }
}
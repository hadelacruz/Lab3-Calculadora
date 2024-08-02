package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    var txtResult:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtResult = findViewById(R.id.txtResult)

    }

    fun calcular(view: View){
        var boton = view as Button
        var textoBoton = boton.text.toString()
        var concatenar = txtResult?.text.toString()+textoBoton
        var concatenarSinCeros = quitarCerosIzquierda(concatenar)
        //var mostrar = quitarCerosIzquierda(concatenar)
        txtResult?.text = concatenarSinCeros  // Actualiza el texto de txtResult
    }

    fun quitarCerosIzquierda(str: String): String{
        var i =0
        while(i<str.length && str[i]=='0') i++
        val sb = StringBuffer(str)
        sb.replace(0, i,"")
        return sb.toString()
    }
}
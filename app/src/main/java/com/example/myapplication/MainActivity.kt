package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Stack
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    var txtResult: TextView? = null

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
        findViewById<Button>(R.id.btnIgual).setOnClickListener {
            evaluarExpresion(it)
        }
        findViewById<Button>(R.id.btnC).setOnClickListener {
            limpiarTexto(it)
        }
    }

    // Método para limpiar el texto
    fun limpiarTexto(view: View) {
        txtResult?.text = "0"
    }

    fun calcular(view: View) {
        val boton = view as Button
        val textoBoton = boton.text.toString()
        val concatenar = txtResult?.text.toString() + textoBoton
        val concatenarSinCeros = quitarCerosIzquierda(concatenar)
        txtResult?.text = concatenarSinCeros
    }

    fun quitarCerosIzquierda(str: String): String {
        var i = 0
        while (i < str.length && str[i] == '0') i++
        val sb = StringBuffer(str)
        sb.replace(0, i, "")
        return sb.toString()
    }

    // Método para evaluar la expresión
    fun evaluarExpresion(view: View) {
        val expresion = txtResult?.text.toString()
        try {
            val result = evaluate(expresion)
            txtResult?.text = result.toString()
        } catch (e: IllegalArgumentException) {
            txtResult?.text = "Error: ${e.message}"
        }
    }

    // Métodos de la calculadora
    private fun precedence(c: Char): Int {
        return when (c) {
            '+', '-' -> 1
            '*', '/' -> 2
            '^' -> 3
            else -> -1
        }
    }

    private fun infixToPostfix(expresion: String): String {
        val result = StringBuilder()
        val stack = Stack<Char>()

        val cleanedExpression = expresion.replace("\\s+".toRegex(), "")

        var i = 0
        while (i < cleanedExpression.length) {
            val c = cleanedExpression[i]

            if (c.isLetterOrDigit()) {
                while (i < cleanedExpression.length && cleanedExpression[i].isLetterOrDigit()) {
                    result.append(cleanedExpression[i])
                    i++
                }
                result.append(' ')
                i--
            } else if (c == '(') {
                stack.push(c)
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(')
                    result.append(stack.pop()).append(' ')
                if (!stack.isEmpty() && stack.peek() != '(') {
                    throw IllegalArgumentException("Invalid Expression") // expresión inválida
                } else {
                    stack.pop()
                }
            } else {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek()))
                    result.append(stack.pop()).append(' ')
                stack.push(c)
            }
            i++
        }

        while (!stack.isEmpty()) {
            if (stack.peek() == '(')
                throw IllegalArgumentException("Invalid Expression") // expresión inválida
            result.append(stack.pop()).append(' ')
        }

        return result.toString().trim()
    }

    private fun stringToList(expression: String): List<String> {
        return expression.split(" ").filter { it.isNotEmpty() }
    }

    private fun evaluatePostfix(postfix: List<String>): Double {
        val stack = Stack<Double>()

        for (token in postfix) {
            when {
                token.toDoubleOrNull() != null -> stack.push(token.toDouble())
                else -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> a / b
                        "^" -> a.pow(b)
                        else -> throw IllegalArgumentException("Unknown operator: $token")
                    })
                }
            }
        }
        return stack.pop()
    }

    private fun evaluate(expresion: String): Double {
        val postfixExpression = infixToPostfix(expresion)
        return evaluatePostfix(stringToList(postfixExpression))
    }


}

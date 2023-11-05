package com.rohan.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private var tvResult: TextView? = null
    private var lastNumeric: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
    }

    fun onDigit(view: View) {
        tvResult?.append((view as Button).text)
        lastNumeric = true
        lastDot = false
    }

    fun onClear(view: View) {
        tvResult?.text = ""
        lastNumeric = false
        lastDot = false
    }

    fun onCE(view: View) {
        val currentText = tvResult?.text.toString()
        if (currentText.isNotEmpty()) {
            // Remove the last character from the result text
            val newText = currentText.substring(0, currentText.length - 1)
            tvResult?.text = newText
        }
    }

    fun onPercent(view: View) {
        val currentText = tvResult?.text.toString()
        if (currentText.isNotEmpty()) {
            try {
                // Parse the current text as a double and divide by 100 to calculate the percentage
                val currentValue = currentText.toDouble()
                val result = currentValue / 100
                tvResult?.text = result.toString()
            } catch (e: Exception) {
                // Handle any parsing or calculation errors here
                tvResult?.text = "Error"
            }
        }
    }



    fun onDecimalPoint(view: View) {
        if (lastNumeric && !lastDot) {
            tvResult?.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View) {
        if (lastNumeric && !isOperatorAdded(tvResult?.text.toString())) {
            tvResult?.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    fun onEqual(view: View) {
        if (lastNumeric) {
            val expression = tvResult?.text.toString()
            try {
                val result = evaluateExpression(expression)
                tvResult?.text = formatResult(result)
            } catch (e: Exception) {
                tvResult?.text = "Error"
            }
        }
    }

    private fun formatResult(result: Double): String {
        // Check if the result is an integer (no decimal part)
        return if (result == result.toInt().toDouble()) {
            result.toInt().toString()
        } else {
            String.format("%.2f", result) // Format with 2 decimal places
        }
    }



    private fun isOperatorAdded(value: String): Boolean {
        return if (value.isNotEmpty()) {
            val lastChar = value[value.length - 1]
            lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/'
        } else {
            false
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val exp = ExpressionBuilder(expression).build()
        return exp.evaluate()
    }
}

package com.example.calculatorapp.model

import java.util.Stack
import kotlin.math.*

object ExpressionEvaluator {

    fun evaluate(expression: String): Double {
        val tokens = tokenize(expression)
        val rpn = shuntingYard(tokens)
        return evaluateRPN(rpn)
    }

    private fun tokenize(expression: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        while (i < expression.length) {
            val c = expression[i]
            if (c.isWhitespace()) {
                i++
                continue
            }
            if (c.isDigit() || c == '.') {
                val sb = StringBuilder()
                while (i < expression.length && (expression[i].isDigit() || expression[i] == '.')) {
                    sb.append(expression[i])
                    i++
                }
                tokens.add(sb.toString())
            } else if (c.isLetter()) {
                val sb = StringBuilder()
                while (i < expression.length && expression[i].isLetter()) {
                    sb.append(expression[i])
                    i++
                }
                tokens.add(sb.toString())
            } else {
                tokens.add(c.toString())
                i++
            }
        }
        return tokens
    }

    private fun shuntingYard(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val operators = Stack<String>()
        val precedence = mapOf(
            "+" to 1, "-" to 1,
            "×" to 2, "÷" to 2, "%" to 2,
            "^" to 3, "√" to 3,
            "sin" to 4, "cos" to 4, "tan" to 4,
            "log" to 4, "ln" to 4, "!" to 4
        )

        for (token in tokens) {
            if (token.first().isDigit()) {
                output.add(token)
            } else if (token == "π") {
                 output.add(Math.PI.toString())
            } else if (token == "e") {
                 output.add(Math.E.toString())
            } else if (token == "(") {
                operators.push(token)
            } else if (token == ")") {
                while (operators.isNotEmpty() && operators.peek() != "(") {
                    output.add(operators.pop())
                }
                operators.pop()
            } else {
                while (operators.isNotEmpty() && operators.peek() != "(" &&
                    (precedence[operators.peek()] ?: 0) >= (precedence[token] ?: 0)) {
                    output.add(operators.pop())
                }
                operators.push(token)
            }
        }
        while (operators.isNotEmpty()) {
            output.add(operators.pop())
        }
        return output
    }

    private fun evaluateRPN(tokens: List<String>): Double {
        val stack = Stack<Double>()
        for (token in tokens) {
            if (token.first().isDigit() || (token.startsWith("-") && token.length > 1)) {
                stack.push(token.toDouble())
            } else {
                val b = if (stack.isNotEmpty()) stack.pop() else 0.0
                // Unary operators
                if (token in listOf("sin", "cos", "tan", "log", "ln", "√", "!")) {
                     when (token) {
                        "sin" -> stack.push(sin(Math.toRadians(b)))
                        "cos" -> stack.push(cos(Math.toRadians(b)))
                        "tan" -> stack.push(tan(Math.toRadians(b)))
                        "log" -> stack.push(log10(b))
                        "ln" -> stack.push(ln(b))
                        "√" -> stack.push(sqrt(b))
                        "!" -> stack.push(factorial(b))
                     }
                } else {
                    // Binary operators
                    val a = if (stack.isNotEmpty()) stack.pop() else 0.0
                    when (token) {
                        "+" -> stack.push(a + b)
                        "-" -> stack.push(a - b)
                        "×" -> stack.push(a * b)
                        "÷" -> stack.push(a / b)
                        "%" -> stack.push(a % b)
                        "^" -> stack.push(a.pow(b))
                    }
                }
            }
        }
        return if (stack.isNotEmpty()) stack.pop() else 0.0
    }

    private fun factorial(x: Double): Double {
        if (x < 0) return Double.NaN
        var res = 1.0
        for (i in 1..x.toInt()) {
            res *= i
        }
        return res
    }
}

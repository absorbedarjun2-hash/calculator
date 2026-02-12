package com.example.calculatorapp.model

data class CalculatorState(
    val expression: String = "",
    val result: String = "",
    val isScientificMode: Boolean = false,
    val history: List<String> = emptyList(),
    val error: String? = null
)

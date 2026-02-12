package com.example.calculatorapp.model

sealed class CalculatorAction {
    data class Input(val value: String) : CalculatorAction() // Digits, Operators, Functions
    object Clear : CalculatorAction()
    object Delete : CalculatorAction()
    object Calculate : CalculatorAction()
    object ToggleScientificMode : CalculatorAction()
    object ShowHistory : CalculatorAction()
    object HideHistory : CalculatorAction()
    object ClearHistory : CalculatorAction()
}

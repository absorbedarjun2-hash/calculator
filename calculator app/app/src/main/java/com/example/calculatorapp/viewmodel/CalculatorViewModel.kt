package com.example.calculatorapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.calculatorapp.model.CalculatorAction
import com.example.calculatorapp.model.CalculatorState
import com.example.calculatorapp.model.ExpressionEvaluator

class CalculatorViewModel : ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Input -> {
                state = state.copy(
                    expression = state.expression + action.value
                )
                calculateResult()
            }
            is CalculatorAction.Clear -> {
                state = state.copy(expression = "", result = "", error = null)
            }
            is CalculatorAction.Delete -> {
                if (state.expression.isNotEmpty()) {
                    state = state.copy(expression = state.expression.dropLast(1))
                    calculateResult()
                }
            }
            is CalculatorAction.Calculate -> {
                calculateResult(commit = true)
            }
            is CalculatorAction.ToggleScientificMode -> {
                state = state.copy(isScientificMode = !state.isScientificMode)
            }
            is CalculatorAction.ShowHistory -> {
                // Toggle history view - implementation dependent on UI
            }
            is CalculatorAction.HideHistory -> {
                // UI dependent
            }
            is CalculatorAction.ClearHistory -> {
                 state = state.copy(history = emptyList())
            }
        }
    }

    private fun calculateResult(commit: Boolean = false) {
        if (state.expression.isEmpty()) {
            state = state.copy(result = "")
            return
        }
        try {
            // Simple replacement for UI symbols to Math symbols
            val evalExpression = state.expression
                .replace("×", "×") // Already using custom symbol in evaluator but just in case
                .replace("÷", "÷")

            val resultDouble = ExpressionEvaluator.evaluate(evalExpression)
            
            // Format result (remove trailing .0)
            val resultString = if (resultDouble % 1.0 == 0.0) {
                resultDouble.toLong().toString()
            } else {
                 String.format("%.8f", resultDouble).trimEnd('0').trimEnd('.')
            }

            if (commit) {
                state = state.copy(
                    result = resultString,
                    expression = resultString,
                    history = state.history + "${state.expression} = $resultString"
                )
            } else {
                state = state.copy(result = resultString, error = null)
            }
        } catch (e: Exception) {
            // Don't show error while typing, only if explicit calculate or severe
            if (commit) {
                state = state.copy(error = "Error")
            }
        }
    }
}

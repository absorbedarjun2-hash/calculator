package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.calculatorapp.ui.components.CalculatorScreen
import com.example.calculatorapp.ui.theme.CalculatorAppTheme
import com.example.calculatorapp.viewmodel.CalculatorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: CalculatorViewModel by viewModels()
        setContent {
            CalculatorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    CalculatorScreen(
                        state = viewModel.state,
                        onAction = viewModel::onAction
                    )
                }
            }
        }
    }
}

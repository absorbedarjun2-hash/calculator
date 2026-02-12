package com.example.calculatorapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatorapp.model.CalculatorAction
import com.example.calculatorapp.model.CalculatorState
import com.example.calculatorapp.ui.theme.AccentColor
import com.example.calculatorapp.ui.theme.ErrorColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var showHistory by remember { mutableStateOf(false) }

    if (showHistory) {
        ModalBottomSheet(onDismissRequest = { showHistory = false }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("History", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    reverseLayout = true, // Show newest at bottom? standard log usually newest top. Let's keep normal.
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.history.reversed()) { item ->
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Divider(color = Color.Gray.copy(alpha = 0.2f))
                    }
                }
                if (state.history.isEmpty()) {
                    Text("No history yet.")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onAction(CalculatorAction.ClearHistory) },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
                ) {
                    Text("Clear History")
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Toggle Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { showHistory = true }) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { onAction(CalculatorAction.ToggleScientificMode) }) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = "Scientific Mode",
                    tint = if (state.isScientificMode) AccentColor else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Display Section
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = state.expression,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3 // Allow more lines for long expressions
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.result.ifEmpty { "0" },
                style = MaterialTheme.typography.displayLarge,
                color = if (state.error != null) ErrorColor else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1
            )
            
            // Actions Row
             Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                 val context = LocalContext.current
                 IconButton(onClick = {
                     val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                     val clip = android.content.ClipData.newPlainText("Calculator Result", state.result)
                     clipboard.setPrimaryClip(clip)
                     // Toast or snackbar could be added here
                 }) {
                     Icon(
                         imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                         contentDescription = "Copy",
                         tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                     )
                 }
                 IconButton(onClick = {
                     val sendIntent: android.content.Intent = android.content.Intent().apply {
                         action = android.content.Intent.ACTION_SEND
                         putExtra(android.content.Intent.EXTRA_TEXT, state.result)
                         type = "text/plain"
                     }
                     val shareIntent = android.content.Intent.createChooser(sendIntent, null)
                     context.startActivity(shareIntent)
                 }) {
                     Icon(
                         imageVector = androidx.compose.material.icons.Icons.Default.Share,
                         contentDescription = "Share",
                         tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                     )
                 }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Scientific Keys
        AnimatedVisibility(
            visible = state.isScientificMode,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            val sciButtons = listOf(
                "sin", "cos", "tan", "log", "ln",
                "√", "^", "!", "π", "e"
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            ) {
                items(sciButtons) { symbol ->
                    CalculatorButton(
                        symbol = symbol,
                        modifier = Modifier
                            .aspectRatio(1.5f) // Flatter buttons for sci
                            .height(50.dp),
                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha=0.5f),
                        textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onAction(CalculatorAction.Input(if (symbol == "e") "e" else if (symbol == "π") "π" else symbol))
                        }
                    )
                }
            }
        }

        // Basic Keypad Section
        val buttons = listOf(
            "C", "(", ")", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "⌫", "="
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(buttons) { symbol ->
                val (bg, text) = when (symbol) {
                    "C" -> ErrorColor.copy(alpha = 0.1f) to ErrorColor
                    "=" -> AccentColor to Color.White
                    "÷", "×", "-", "+", "(", ")" -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
                    else -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface
                }

                CalculatorButton(
                    symbol = symbol,
                    modifier = Modifier.aspectRatio(1f),
                    backgroundColor = bg,
                    textColor = text,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        val action = when (symbol) {
                            "C" -> CalculatorAction.Clear
                            "⌫" -> CalculatorAction.Delete
                            "=" -> CalculatorAction.Calculate
                            else -> CalculatorAction.Input(symbol)
                        }
                        onAction(action)
                    }
                )
            }
        }
    }
}

package com.example.calculatorapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.math.BigDecimal


class BigDecimalViewModel : ViewModel() {
private val activityTag = "TAg"
private var operand1: BigDecimal? = null

//private var operand2: Double = 0.0
private var pendingOperation = "="
private var result = MutableLiveData<BigDecimal>()
val stringResult: LiveData<String>
    get() = Transformations.map(result) {it.toString()}
private var newNumber = MutableLiveData<String>()
val stringNewNumber: LiveData<String> get() = newNumber

private var operation = MutableLiveData<String>()
val stringOperation: LiveData<String> get() = operation

fun digitPressed(caption: String) {
    if (newNumber.value != null) {
        newNumber.value = newNumber.value + caption
    } else {
        newNumber.value = caption
    }
}

fun operandPressed(op: String) {
    try {
        val value = newNumber.value?.toBigDecimal()
        performOperation(value ?: BigDecimal.ZERO, op)
    } catch (e: NumberFormatException) {
        newNumber.value = ""
    }
    pendingOperation = op

    operation.value = pendingOperation
}

fun negPressed() {
    val value = newNumber.value
    if (value == null || value.isEmpty()) {
        newNumber.value = "-"
    } else {
        try {
            var doubleVal = value.toBigDecimal()
            doubleVal *= BigDecimal.valueOf(-1)
            newNumber.value = doubleVal.toString()
        } catch (e: NumberFormatException) {
            newNumber.value = ""
        }
    }
}

private fun performOperation(value: BigDecimal, op: String) {
    //Toast.makeText(this,"Value is: ${value}, Operation is ${op}",Toast.LENGTH_SHORT).show()
    if (operand1 == null) {
        operand1 = value
    } else {
        if (pendingOperation == "=" || pendingOperation == "Neg") {
            pendingOperation = op
        }
        when (pendingOperation) {
            "+" -> {
//                    Log.e(activityTag, "PLUS: Value is: ${value}, Operation is ${op}")
                operand1 = operand1!! + value
            }
            "-" -> {
//                    Log.e(activityTag, "MINUS: Value is: ${value}, Operation is ${op}")
                operand1 = operand1!! - value
            }
            "*" -> {
//                    Log.e(activityTag, "MULTY: Value is: ${value}, Operation is ${op}")
                operand1 = operand1!! * value
            }
            "/" -> {
//                    Log.e(activityTag, "DIVIDE: Value is: ${value}, Operation is ${op}")
                operand1 = if (value == BigDecimal.ZERO) {
                    BigDecimal.valueOf(Double.NaN)
                } else {
                    operand1!! / value
                }
            }
            "=" -> {
                //Log.e(activityTag, "RESULT: Value is: ${value}, Operation is ${op}")
                operand1 = value
            }/*"Neg" -> {
                    Log.e(activityTag, "RESULT: Value is: ${value}, Operation is ${op}")
                    var newValue = value * (-1.0)
                    operand1 = newValue
                }*/
            else -> {
                //Log.e(activityTag, "ERROR: Value is: ${value}, Operation is ${op}")

            }
        }
    }
    result.value = operand1
    newNumber.value = ""
    operation.value = op//?
}
}
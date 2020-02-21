package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NumberFormatException
import kotlin.math.absoluteValue


private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1Stored"

class MainActivity : AppCompatActivity() {
    private val activityTag = "MainActivity"

    //private lateinit var result: EditText
    //private lateinit var newNumber: EditText
    //private val displayedOperator by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }
    private var operand1: Double? = null
    private var operand2: Double = 0.0
    private var pendingOperation = "="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        result.editableText.clear()
        operation.text = pendingOperation

        val buttonList: ArrayList<Button> = ArrayList()
        buttonList.add(button_0)
        buttonList.add(button_1)
        buttonList.add(button_2)
        buttonList.add(button_3)
        buttonList.add(button_4)
        buttonList.add(button_5)
        buttonList.add(button_6)
        buttonList.add(button_7)
        buttonList.add(button_8)
        buttonList.add(button_9)
        buttonList.add(button_point)

        val listener =
            View.OnClickListener { v -> val button = v as Button; newNumber.append(button.text) }

        buttonList.forEach { b -> b.setOnClickListener(listener) }
        // Operation buttons

        val operationList: ArrayList<Button> = ArrayList()
        operationList.add(button_equal)
        operationList.add(button_divide)
        operationList.add(button_multiply)
        operationList.add(button_minus)
        operationList.add(button_plus)
        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            val value = newNumber.text.toString()
            if (value.isNotEmpty()) {
                performOperation(value.toDouble(), op)
            }
            pendingOperation = op

            operation.text = pendingOperation
        }

        operationList.forEach { b -> b.setOnClickListener(opListener) }

        button_neg.setOnClickListener { v ->
            val value = newNumber.text.toString()
            if(value.isEmpty()){
                newNumber.setText("-")
            }else{
                try {
                    var doubleVal = value.toDouble()
                    doubleVal *= -1
                    newNumber.setText(doubleVal.toString())
                }catch (e: NumberFormatException){
                    newNumber.setText("")
                }
            }
        }
    }

    private fun performOperation(value: Double, op: String) {
        //Toast.makeText(this,"Value is: ${value}, Operation is ${op}",Toast.LENGTH_SHORT).show()
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=" || pendingOperation == "Neg") {
                pendingOperation = op
            }
            when (pendingOperation) {
                "+" -> {
                    Log.e(activityTag, "PLUS: Value is: ${value}, Operation is ${op}")
                    operand1 = operand1!! + value
                }
                "-" -> {
                    Log.e(activityTag, "MINUS: Value is: ${value}, Operation is ${op}")
                    operand1 = operand1!! - value
                }
                "*" -> {
                    Log.e(activityTag, "MULTY: Value is: ${value}, Operation is ${op}")
                    operand1 = operand1!! * value
                }
                "/" -> {
                    Log.e(activityTag, "DIVIDE: Value is: ${value}, Operation is ${op}")
                    operand1 = if (value == 0.0) {
                        Double.NaN
                    } else {
                        operand1!! / value
                    }
                }
                "=" -> {
                    Log.e(activityTag, "RESULT: Value is: ${value}, Operation is ${op}")
                    operand1 = value
                }/*"Neg" -> {
                    Log.e(activityTag, "RESULT: Value is: ${value}, Operation is ${op}")
                    var newValue = value * (-1.0)
                    operand1 = newValue
                }*/
                else -> {
                    Log.e(activityTag, "ERROR: Value is: ${value}, Operation is ${op}")

                }
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
        operation.text = op
    }

    fun numberClicked(v: View) {
        val button = v as Button
        newNumber.append(button.text)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)) {
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else null
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        operation.text = pendingOperation

    }
}



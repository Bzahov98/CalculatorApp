package com.example.calculatorapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val activityTag = "MainActivity"
    //private lateinit var result: EditText
    //private lateinit var newNumber: EditText
    //private val displayedOperator by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val viewModel: CalculatorViewModel by viewModels()
        val viewModel: BigDecimalViewModel by viewModels()
        setContentView(R.layout.activity_main)
        initViews(viewModel)
    }

    private fun initViews(viewModel: BigDecimalViewModel) {
        result.editableText.clear()
        viewModel.stringResult   .observe(this, Observer { str-> result.setText(str)    })
        //        viewModel.stringResult   .observe(this, Observer { str-> result.setText(str)    })
        viewModel.stringNewNumber.observe(this, Observer { str-> newNumber.setText(str) })
        viewModel.stringOperation.observe(this, Observer { str-> operation.setText(str) })
        //operation.text = pendingOperation
        //viewModel.operation.value = ""
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

        val listener = View.OnClickListener {
            viewModel.digitPressed((it as Button).text.toString())
        }
        //View.OnClickListener { v -> val button = v as Button; newNumber.append(button.text) }
        buttonList.forEach { b -> b.setOnClickListener(listener) }
        // Operation buttons

        val operationList: ArrayList<Button> = ArrayList()
        operationList.add(button_equal)
        operationList.add(button_divide)
        operationList.add(button_multiply)
        operationList.add(button_minus)
        operationList.add(button_plus)
        val opListener = View.OnClickListener {
            viewModel.operandPressed((it as Button).text.toString())
        }

        operationList.forEach { it.setOnClickListener(opListener) }

        button_neg.setOnClickListener {
            viewModel.negPressed()
        }
    }


    fun numberClicked(v: View) {
        val button = v as Button
        newNumber.append(button.text)
    }


}



package com.example.gb_android_calc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    TextView resultField;
    EditText numberField;
    TextView operationField;
    Double operand = null;
    String lastOperation = "=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultField = findViewById(R.id.result);
        numberField = findViewById(R.id.numberField);
        operationField = findViewById(R.id.operation);

        ImageButton backspace = findViewById(R.id.backspace);
        Button btnClear = findViewById(R.id.button_cleaning);

        btnClear.setOnClickListener(v -> {
            resultField.setText("");
            numberField.setText("");
            operationField.setText("");
        });

        backspace.setOnClickListener(v -> {

            int cursorPosEnd = numberField.getSelectionEnd();
            int textLength = numberField.getText().length();

            if (cursorPosEnd != 0 && textLength != 0) {
                SpannableStringBuilder selection = (SpannableStringBuilder) numberField.getText();
                selection.replace(cursorPosEnd - 1, cursorPosEnd, "");

                numberField.setText(selection);

                numberField.setSelection(cursorPosEnd - 1);
            }
        });
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle instanceState) {
        instanceState.putString("OPERATION", lastOperation);
        if (operand != null)
            instanceState.putDouble("OPERAND", operand);
        super.onSaveInstanceState(instanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand = savedInstanceState.getDouble("OPERAND");
        resultField.setText(operand.toString());
        operationField.setText(lastOperation);
    }


    public void onNumberClick(View view) {

        Button button = (Button) view;
        numberField.append(button.getText());

        if (lastOperation.equals("=") && operand != null) {
            operand = null;
        }
    }

    public void onOperationClick(View view) {

        Button button = (Button) view;
        String op = button.getText().toString();
        String number = numberField.getText().toString();

        if (number.length() > 0) {
            number = number.replace(',', '.');
            try {
                performOperation(Double.valueOf(number), op);
            } catch (NumberFormatException ex) {
                numberField.setText("");
            }
        }
        lastOperation = op;
        operationField.setText(lastOperation);
    }

    private void performOperation(Double number, String operation) {

        if (operand == null) {
            operand = number;
        } else {
            if (lastOperation.equals("=")) {
                lastOperation = operation;
            }
            switch (lastOperation) {
                case "=":
                    operand = number;
                    break;
                case "÷":
                    if (number == 0) {
                        operand = 0.0;
                    } else {
                        operand /= number;
                    }
                    break;
                case "x":
                    operand *= number;
                    break;
                case "+":
                    operand += number;
                    break;
                case "-":
                    operand -= number;
                    break;
            }
        }
        resultField.setText(operand.toString().replace('.', ','));
        numberField.setText("");
    }
}
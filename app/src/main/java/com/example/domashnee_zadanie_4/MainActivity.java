package com.example.domashnee_zadanie_4;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView resultTextView, solutionTextView;
    private static final int MAX_SOLUTION_TEXTVIEW_LENGTH = 15;
    private static final int RESULT_TEXTVIEW_LENGTH_BEFORE_POINT = 7;
    private static final int RESULT_TEXTVIEW_LENGTH_AFTER_POINT = 3;

    private static final int MAX_RESULT_TEXTVIEW =
            RESULT_TEXTVIEW_LENGTH_BEFORE_POINT + RESULT_TEXTVIEW_LENGTH_AFTER_POINT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        resultTextView = findViewById(R.id.textView_result);
        solutionTextView = findViewById(R.id.textView_solution);

        assignId(R.id.button_c);
        assignId(R.id.button_1);
        assignId(R.id.button_2);
        assignId(R.id.button_3);
        assignId(R.id.button_4);
        assignId(R.id.button_5);
        assignId(R.id.button_6);
        assignId(R.id.button_7);
        assignId(R.id.button_8);
        assignId(R.id.button_9);
        assignId(R.id.button_0);
        assignId(R.id.button_div);
        assignId(R.id.button_mul);
        assignId(R.id.button_add);
        assignId(R.id.button_sub);
        assignId(R.id.button_res);
        assignId(R.id.button_open_bracket);
        assignId(R.id.button_close_bracket);
        assignId(R.id.button_AC);
        assignId(R.id.button_dot);



    }


    void assignId(int id) {
        MaterialButton button = findViewById(id);
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTextView.getText().toString();

        switch (buttonText) {
            case "AC":
                solutionTextView.setText("");
                resultTextView.setText("0");
                return;
            case "=":
                String finalResult = getResult(dataToCalculate);

                if (!finalResult.equals("Error")) {
                    resultTextView.setText(finalResult);
                }
                solutionTextView.setText(resultTextView.getText());
                return;
            case "C":
                dataToCalculate = dataToCalculate.substring(0,dataToCalculate.length()-1);
                break;
            default:
                if (dataToCalculate.length() < MAX_SOLUTION_TEXTVIEW_LENGTH) {
                    dataToCalculate = dataToCalculate + buttonText;
                }
        }

        solutionTextView.setText(dataToCalculate);

    }

    String getResult(String data) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable,data,"Javascript",1,null).toString();

            finalResult = limitToMaxDigits(finalResult, MAX_RESULT_TEXTVIEW);

            finalResult = removeTrailingZeros(finalResult);


            return finalResult;
        } catch (Exception e) {
            return "Error";
        }
    }

    public static String removeTrailingZeros(String value) {
        // Find the index of the decimal point
        int decimalIndex = value.indexOf(".");
        if (decimalIndex == -1) {
            // No decimal point, return the original string
            return value;
        }

        // Iterate from the end of the string and find the last non-zero character
        int endIndex = value.length() - 1;
        while (endIndex > decimalIndex && value.charAt(endIndex) == '0') {
            endIndex--;
        }

        // If the last character after trimming is the decimal point, remove it
        if (endIndex == decimalIndex) {
            return value.substring(0, decimalIndex); // return the integer part only
        }

        // Return the trimmed string up to the last non-zero character
        return value.substring(0, endIndex + 1);
    }

    public static String limitToMaxDigits(String value, int maxDigits) {

        BigDecimal bigDecimal = new BigDecimal(value);

        // Set precision to 10 significant digits
        bigDecimal = bigDecimal.round(new MathContext(3));

        bigDecimal = bigDecimal.stripTrailingZeros();

        return bigDecimal.toPlainString();

    }

    
}
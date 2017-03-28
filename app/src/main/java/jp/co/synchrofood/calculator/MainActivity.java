package jp.co.synchrofood.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static jp.co.synchrofood.calculator.MainActivity.RequestCode.REQUEST_CODE_ANOTHER_CALC1;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private EditText numberInput1;
    private EditText numberInput2;
    private Spinner operatorSelector;
    private TextView calcResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.calcButton1).setOnClickListener(this);
        findViewById(R.id.calcButton2).setOnClickListener(this);
        findViewById(R.id.nextButton).setOnClickListener(this);

        numberInput1 = EditText.class.cast(findViewById(R.id.numberInput1));
        numberInput1.addTextChangedListener(this);

        numberInput2 = EditText.class.cast(findViewById(R.id.numberInput2));
        numberInput2.addTextChangedListener(this);

        operatorSelector = (Spinner) findViewById(R.id.operatorSelector);
        calcResult = (TextView) findViewById(R.id.calcResult);
    }

    private boolean checkEditTextInput() {
        String input1 = numberInput1.getText().toString();
        String input2 = numberInput2.getText().toString();
        return !TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        refreshResult();
    }

    private void refreshResult() {
        if (checkEditTextInput()) {
            int result = calc();
            String resultText = getString(R.string.calc_result_text, result);
            calcResult.setText(resultText);
        } else {
            calcResult.setText(R.string.calc_result_default);
        }
    }

    private int calc() {
        String input1 = numberInput1.getText().toString();
        String input2 = numberInput2.getText().toString();

        int number1 = Integer.parseInt(input1);
        int number2 = Integer.parseInt(input2);

        int operator = operatorSelector.getSelectedItemPosition();
        switch (operator) {
            case 0:
                return number1 + number2;
            case 1:
                return number1 - number2;
            case 2:
                return number1 * number2;
            case 3:
                return number1 / number2;
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calcButton1:
                Intent intent1 = new Intent(this, AnotherCalcActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_ANOTHER_CALC1.getCode());
                break;
            case R.id.calcButton2:
                Intent intent2 = new Intent(this, AnotherCalcActivity.class);
                startActivityForResult(intent2, RequestCode.REQUEST_CODE_ANOTHER_CALC2.getCode());
                break;
            case R.id.nextButton:
                if (checkEditTextInput()) {
                    int result = calc();
                    numberInput1.setText(String.valueOf(result));
                    refreshResult();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        Bundle resultBundle = data.getExtras();
        if (!resultBundle.containsKey("result")) {
            return;
        }
        int result = resultBundle.getInt("result");

        switch (requestCode) {
            case 1:
                numberInput1.setText(String.valueOf(result));
                break;
            case 2:
                numberInput2.setText(String.valueOf(result));
                break;
        }
        refreshResult();
    }

    public enum RequestCode {
        REQUEST_CODE_ANOTHER_CALC1(1),
        REQUEST_CODE_ANOTHER_CALC2(2),;
        private int code;

        RequestCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}

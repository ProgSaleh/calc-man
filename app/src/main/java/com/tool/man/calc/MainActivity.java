package com.tool.man.calc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mAdditionMain;
    private TextView mSubtractionMain;
    private TextView mMultiplicationMain;
    private TextView mDivisionMain;
    private TextView mRemainderMain;

    public static final String USER_DESIRED_OPERATION = "com.tool.man.calc.USER_DESIRED_OPERATION";
    private char mDesiredOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inflates main page's views.
        inflateViews();
    }

    /**
     * Inflates control views to the layout.
     */
    private void inflateViews() {
        mAdditionMain = findViewById(R.id.label_addition_main);
        mSubtractionMain = findViewById(R.id.label_subtraction_main);
        mMultiplicationMain = findViewById(R.id.label_multiplication_main);
        mDivisionMain = findViewById(R.id.label_division_main);
        mRemainderMain = findViewById(R.id.label_division_reminder_main);
    }

    public void openCalc(@NonNull View view) {
        // Switch statement would have been a better here,
        // but, as recommended, R.IDs will not be final in recent coming updates of Gradle
        // which will break all switch statements!!!
        Intent intent = new Intent(this, CalcActivity.class);

        if (view.getId() == R.id.label_addition_main) {
            mDesiredOperation = '+';
        } else if (view.getId() == R.id.label_subtraction_main) {
            mDesiredOperation = '-';
        } else if (view.getId() == R.id.label_multiplication_main) {
            mDesiredOperation = '×';
        } else if (view.getId() == R.id.label_division_main) {
            mDesiredOperation = '/';
        } else if (view.getId() == R.id.label_division_reminder_main) {
            // it's (else if) and not (else)
            // because of the up coming updates of the main layout...
            mDesiredOperation = '%';
        }
        intent.putExtra(USER_DESIRED_OPERATION, mDesiredOperation);
        startActivity(intent);
    }
}
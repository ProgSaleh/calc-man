package com.tool.man.calc;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalcActivity extends AppCompatActivity {

    // Controls of the layout.
    private TextView mFirstOperatorTextView;
    private TextView mSecondOperatorTextView;
    private TextView mOperationTextView;
    private Button mGuessButtonOne;
    private Button mGuessButtonTwo;
    private Button mGuessButtonThree;
    private Button mGuessButtonFour;

    // Put guess buttons in a data structure so they can be treated more easley.
    private List<Button> mGuessButtons = new ArrayList<>();
    // Put guess numbers in a data structure so they can be shuffled.
    private List<Integer> mGuessNumbers = new ArrayList<>();

    private char mReceivedOperation; // user desired operation(clicked operation from the main layout.)

    private int mFirstRandomNumber;
    private int mSecondRandomNumber;

    // Three randomly generated guess numbers.
    private int mGuessOne;
    private int mGuessTwo;
    private int mGuessThree;

    int userGuessedAnswer; // User guess for current expression.

    // Range of generated numbers.
    // Note that as the number increases,
    // generating takes LONG time due to enhancements in enhanceExpressions()
    private final int RANDOMIZATION_RANGE = 35;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        // Inflate layout views.
        inflateViews();

        // add buttons to the list AFTER they've been inflated(a.k.a. not null!).
        mGuessButtons.add(mGuessButtonOne);
        mGuessButtons.add(mGuessButtonTwo);
        mGuessButtons.add(mGuessButtonThree);
        mGuessButtons.add(mGuessButtonFour);

        // user clicked(desired) operation.
        mReceivedOperation = getIntent().
                getCharExtra(MainActivity.USER_DESIRED_OPERATION, '+');

        // Set up and display an expression
        generateCompleteExpression();
        Log.d("Saleh", mFirstRandomNumber + " : " + mSecondRandomNumber);

        // Start a timer for the current operation.
        //handleTimeLimit();

        // Sets listeners for guess buttons.
        mGuessButtonOne.setOnClickListener(guessListener);
        mGuessButtonTwo.setOnClickListener(guessListener);
        mGuessButtonThree.setOnClickListener(guessListener);
        mGuessButtonFour.setOnClickListener(guessListener);
    } // onCreate()

    private void inflateViews() {
        //mRemainingTimeValueTextView = findViewById(R.id.remaining_time_value_text_view);
        mFirstOperatorTextView = findViewById(R.id.first_operator_text_view);
        mOperationTextView = findViewById(R.id.operation_text_view);
        mSecondOperatorTextView = findViewById(R.id.second_operator_text_view);
        mGuessButtonOne = findViewById(R.id.choice_1_button);
        mGuessButtonTwo = findViewById(R.id.choice_2_button);
        mGuessButtonThree = findViewById(R.id.choice_3_button);
        mGuessButtonFour = findViewById(R.id.choice_4_button);
        //mCorrectAnswerLabelTextView = findViewById(R.id.correct_answer_label_text_view);
    }

    /**
     * Generates a full expression.
     */
    private void generateCompleteExpression() {

        // Generate completely, freely, random two integers(within the given range)
        mFirstRandomNumber = (int) (Math.random() * RANDOMIZATION_RANGE) + 1;
        mSecondRandomNumber = (int) (Math.random() * RANDOMIZATION_RANGE) + 1;

        // Generate guesses within the same range.
        mGuessOne = (int) (Math.random() * RANDOMIZATION_RANGE) + 1;
        mGuessTwo = (int) (Math.random() * RANDOMIZATION_RANGE) + 1;
        mGuessThree = (int) (Math.random() * RANDOMIZATION_RANGE) + 1;

        // Check to see if generated integers need to be enhanced(that's regenerated)...
        enhanceExpressions();

        // After generation COMPLETELY finishes, get the right answer.
        getCorrectAnswer();

        configureGuesses();

        mFirstOperatorTextView.setText(String.valueOf(mFirstRandomNumber));
        mOperationTextView.setText(String.valueOf(mReceivedOperation));
        mSecondOperatorTextView.setText(String.valueOf(mSecondRandomNumber));

    } // generateCompleteExpression()

    /**
     * Sets correct and generated numbers and then
     * shuffle the correct answer's position button.
     */
    private void configureGuesses() {

        mGuessNumbers.add(getCorrectAnswer());
        mGuessNumbers.add(mGuessOne);
        mGuessNumbers.add(mGuessTwo);
        mGuessNumbers.add(mGuessThree);
        Collections.shuffle(mGuessNumbers);

        mGuessButtonOne.setText(String.valueOf(mGuessNumbers.remove(0)));
        mGuessButtonTwo.setText(String.valueOf(mGuessNumbers.remove(0)));
        mGuessButtonThree.setText(String.valueOf(mGuessNumbers.remove(0)));
        mGuessButtonFour.setText(String.valueOf(mGuessNumbers.remove(0)));
    } // configureGuesses()

    /**
     * Gets the correct answer for current operation.
     */
    private int getCorrectAnswer() {
        int mCorrectAnswer = 0;
        switch (mReceivedOperation) {
            case '+':
                mCorrectAnswer = mFirstRandomNumber + mSecondRandomNumber;
                break;
            case '-':
                mCorrectAnswer = mFirstRandomNumber - mSecondRandomNumber;
                break;
            case '×':
                mCorrectAnswer = mFirstRandomNumber * mSecondRandomNumber;
                break;
            case '/':
                mCorrectAnswer = mFirstRandomNumber / mSecondRandomNumber;
                break;
            case '%':
                mCorrectAnswer = mFirstRandomNumber % mSecondRandomNumber;
                break;
        }
        //Log.d("Salehh", String.valueOf(mCorrectAnswer));
        return mCorrectAnswer;
    }


    /**
     * Gets user choice for the current expression.
     */
    public void guessedAnswer(View view) {
        Button userGuessedButton = (Button) view;
        userGuessedAnswer = Integer.parseInt(userGuessedButton.getText().toString());
        //Log.d("SS", userGuessedAnswer + "");
    }

    /**
     * enhance numerics.. AVOID "/ by zero" for example.
     * <p>
     * TODO enhance algorithms to get better results...
     */
    private void enhanceExpressions() {
        // if a generated guess matches the correct answer, regenerate.
        if (mGuessOne == getCorrectAnswer() ||
                mGuessTwo == getCorrectAnswer() ||
                mGuessThree == getCorrectAnswer()
        ) {
            generateCompleteExpression();
        }

        switch (mReceivedOperation) {
            case '/':
                // If second number is 0, regenerate!
                if (mSecondRandomNumber == 0) {
                    generateCompleteExpression();
                }
                // If first number is less than the second, regenerate!
                if (mFirstRandomNumber < mSecondRandomNumber) {
                    generateCompleteExpression();
                }
                // If division result isn't an integer(+Z, a.k.a. 0 or >), regenerate!
                if (mFirstRandomNumber % mSecondRandomNumber != 0) {
                    generateCompleteExpression();
                }
                break;

            case '-':
                // If first number is less than the second, regenerate!
                if (mFirstRandomNumber < mSecondRandomNumber) {
                    generateCompleteExpression();
                }
                // No break, fall out to include % operation.
            case '%':
                // If first number is less than the second, regenerate!
                if (mFirstRandomNumber < mSecondRandomNumber) {
                    generateCompleteExpression();
                }
//                if (mFirstRandomNumber % mSecondRandomNumber != 0) {
//                    generateCompleteExpression();
//                }
                break;

            // Avoid > 12 numbers in multiplication(for simplicity...).
            case '×':
                if (mFirstRandomNumber > 12 || mSecondRandomNumber > 12) {
                    generateCompleteExpression();
                }
                break;
        }

        // Range of guesses should be consistent with the correct answer.
        // For instance, when correctAnswer is >= 100, guesses should be within the same range.
        // NOTE! this algorithm DOES make generating new numbers
        // slower and slower as the range of guesses increases.
        // Current range is 35. Tested on range 165 and the delay was greatly noticed!
        if (getCorrectAnswer() >= 100) {
            if (mGuessOne < 100 || mGuessTwo < 100 || mGuessThree < 100) {
                generateCompleteExpression();
            }
        } else {
            if (mGuessOne >= 100 || mGuessTwo >= 100 || mGuessThree >= 100) {
                generateCompleteExpression();
            }
        }

        if (getCorrectAnswer() >= 10) {
            if (mGuessOne < 10 || mGuessTwo < 10 || mGuessThree < 10) {
                generateCompleteExpression();
            }
        } else {
            if (mGuessOne >= 10 || mGuessTwo >= 10 || mGuessThree >= 10) {
                generateCompleteExpression();
            }
        }

        if (getCorrectAnswer() < 10) {
            if (mGuessOne >= 10 || mGuessTwo >= 10 || mGuessThree >= 10) {
                generateCompleteExpression();
            }
        } else {
            if (mGuessOne <= 9 || mGuessTwo <= 9 || mGuessThree <= 9) {
                generateCompleteExpression();
            }
        }
    }

    /**
     * Enable guess buttons.
     */
    private void enableButtons() {
        mGuessButtonOne.setEnabled(true);
        mGuessButtonTwo.setEnabled(true);
        mGuessButtonThree.setEnabled(true);
        mGuessButtonFour.setEnabled(true);
    }

    /**
     * Disable guess buttons and gray their color to look more of a disabled ones.
     * <p>
     * NOTE that disabling them gives a be-default gray color.
     * However, because of changing their background color
     * to the default one when generating next expression,
     * they don't get the default gray color!!! So I had to color them myself.</p>
     */
    private void disableButtons() {
        mGuessButtonOne.setEnabled(false);
        mGuessButtonTwo.setEnabled(false);
        mGuessButtonThree.setEnabled(false);
        mGuessButtonFour.setEnabled(false);

        for (Button button : mGuessButtons) {
            button.setBackgroundColor(Color.LTGRAY);
        }
    }

    private void generateNextExpression() {
        new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long l) {
                // No need
            }

            @Override
            public void onFinish() {
                for (Button button : mGuessButtons) {
                    button.setBackgroundColor(Color.argb(255, 62, 0, 238));
                }
                enableButtons();
                generateCompleteExpression();
            }

        }.start();
    }

    private final View.OnClickListener guessListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Immediately disable all buttons.
            disableButtons();

            Button clickedButton = (Button) view;
            int userAnswerForCurrentExpression =
                    Integer.parseInt(clickedButton.getText().toString());

            if (userAnswerForCurrentExpression == getCorrectAnswer()) {
                clickedButton.setBackgroundColor(Color.GREEN);
            } else {
                clickedButton.setBackgroundColor(Color.RED);

                for (Button button : mGuessButtons) {
                    // Find correct answer and set its background color to green.
                    if (Integer.parseInt(button.getText().toString()) == getCorrectAnswer()) {
                        button.setBackgroundColor(Color.GREEN);
                    }
                }
            }

            // enable the buttons and generate next expression.
            generateNextExpression();

        }
    };

} // CalcActivity {}
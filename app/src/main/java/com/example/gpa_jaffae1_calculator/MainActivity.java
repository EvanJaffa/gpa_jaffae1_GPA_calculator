/* ************************************************************************ */
/* Filename: MainActivity.java  Author: Evan Jaffa                          */
/* Course: CSIT 451-03, Prof. Zhou, Spring 2021                             */
/* Main activity code for GPA Calculator Assignment                         */
/* ************************************************************************ */

package com.example.gpa_jaffae1_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.Arrays;

import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity {

    //Establish objects to serve as references the views in this activity
    private Button calcButton;
    private EditText grade1, grade2, grade3, grade4, grade5, credit1, credit2, credit3, credit4, credit5;
    private TextView gradeDisplay;

    // Establish a boolean switch variable for the Calculate button, to control its mode as Calculate or Clear
    boolean clearOrCalc = false;  // False = Button says Calc, True = Button now says Clear

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use R.id matching to assign each object to its respective view in the activity
        grade1 = (EditText)findViewById(R.id.gradeText1);
        grade2 = (EditText)findViewById(R.id.gradeText2);
        grade3 = (EditText)findViewById(R.id.gradeText3);
        grade4 = (EditText)findViewById(R.id.gradeText4);
        grade5 = (EditText)findViewById(R.id.gradeText5);
        credit1 = (EditText)findViewById(R.id.creditText1);
        credit2 = (EditText)findViewById(R.id.creditText2);
        credit3 = (EditText)findViewById(R.id.creditText3);
        credit4 = (EditText)findViewById(R.id.creditText4);
        credit5 = (EditText)findViewById(R.id.creditText5);
        gradeDisplay = (TextView)findViewById(R.id.resultText);
        calcButton = (Button)findViewById(R.id.calculateButton);

        // Set gradeCalcInitializer() as the onClick method for calcButton
        calcButton.setOnClickListener(this::gradeCalcInitializer);
    }

    // This method checks to make sure that, for every Course in the UI, that its 'Grade' field and 'Course' field are
    // wither both filled in, or both empty
    public boolean checkEmptyEvenness(){
        Log.i("running", "checkEmptyEvenness() is running");

        // If a 'gradeX' and 'creditX' pair of TextViews are not both filled XOR both unfilled, operation stops and a Toast is generated that asks the user to fill empty fields.
        if( isEmpty(grade1) ^ isEmpty(credit1) ){
            Toast.makeText(MainActivity.this, "Please fill both fields for Course 1", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( isEmpty(grade2) ^ isEmpty(credit2) ){
            Toast.makeText(MainActivity.this, "Please fill both fields for Course 2", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( isEmpty(grade3) ^ isEmpty(credit3) ){
            Toast.makeText(MainActivity.this, "Please fill both fields for Course 3", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( isEmpty(grade4) ^ isEmpty(credit4) ){
            Toast.makeText(MainActivity.this, "Please fill both fields for Course 4", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( isEmpty(grade5) ^ isEmpty(credit5) ){
            Toast.makeText(MainActivity.this, "Please fill both fields for Course 5", Toast.LENGTH_LONG).show();
            return false;
        }

        // If each Course line passes the check, a Toast indicating so is made, and operation continues
        Toast.makeText(MainActivity.this, "The fields are all evenly filled", Toast.LENGTH_SHORT).show();
        Log.i("running", "checkEmptyEvenness() has reached the return-true statement");
        return true;

    }

    // emptyRowsReturner() produces a key for which 'rows' or 'Courses' have been left blank
    public boolean[] emptyRowsReturner(){
        Log.i("running", "emptyRowsReturner() is running");

        // create a new 5-element array to represent which lines have been left empty
        boolean[] emptyRowsKey = new boolean[5];

        // Set the array elements as 'true' to start
        for( int i = 0; i < emptyRowsKey.length; i++){
            emptyRowsKey[i] = true;
        }

        // In normal program operation, this method is always called after checkEmptyEvenness() returns a success.
        // Thus, we can assume that every Course line either has both fields filled XOR both fields empty
        // Thus, we can just check if the 'gradeX' view is empty and assume the same is true for the 'creditX' view on a given line
        for( int i = 0; i < emptyRowsKey.length; i++){
            switch(i){
                // For each line, if the 'gradeX' view is empty, then we set a corresponding element of the emptyRowsKey to 'false'
                case 0:
                    if(isEmpty(grade1)){
                        emptyRowsKey[i] = false;
                    }
                    break;
                case 1:
                    if(isEmpty(grade2)){
                        emptyRowsKey[i] = false;
                    }
                    break;
                case 2:
                    if(isEmpty(grade3)){
                        emptyRowsKey[i] = false;
                    }
                    break;
                case 3:
                    if(isEmpty(grade4)){
                        emptyRowsKey[i] = false;
                    }
                    break;
                case 4:
                    if(isEmpty(grade5)){
                        emptyRowsKey[i] = false;
                    }
                    break;
            }
        }

        // Report the emptyRowsKey state into the log for debugging
        String keyArrayAsString = Arrays.toString(emptyRowsKey);
        Log.i("running", "emptyRowsReturner() has reached its return stmt");
        Log.i("running", "emptyRowsKey[] = "+keyArrayAsString);
        return emptyRowsKey;
    }

    // This method checks if a EditText view is empty. This process is needed many times, so I elected to compartmentalize it
    private boolean isEmpty(EditText myEditText) {
        return myEditText.getText().toString().trim().length() == 0;
    }

    // This is the main controller method, called when the Calculate button is clicked
    public void gradeCalcInitializer(View view) {
        Log.i("running", "gradeCalcInitializer() is running");

        // If the big button is set to 'Calculate' mode
        if (!clearOrCalc) {

            // First run checkEmptyEvenness() to see if the calculation should start. If not, the operation is cancelled.
            if (checkEmptyEvenness()) {

                // Call emptyRowsReturner() to produce a key for which 'rows' or 'Courses' have been left blank
                boolean emptyRowsKey[] = emptyRowsReturner();

                // Hand this emptyRowsKey to averageGrades(), which performs the weighted average calculations
                // averageGrades() produces a float, which is assigned to weightedAverage
                float weightedAverage = averageGrades(emptyRowsKey);

                // gradeDisplay (an EditText object) has its text set to the amount of weightedAverage
                gradeDisplay.setText(Float.toString(weightedAverage));
                // and changeGradeDisplayColor() changes its color as appropriate, as stated in the assignment specifications
                changeGradeDisplayColor(weightedAverage);
                // updateCalcButton() updates the Calculate button to say 'Clear', and sets an internal flag to indicate this.
                updateCalcButton(0);
                return;

            }

            // If the big button is set to 'Clear' mode
        } else if (clearOrCalc) {
            Log.i("running", "Detected that the button was pressed while clearOrCalc is true");
            // clearFields() resets all ten EditText views to empty
            clearFields();
            // updateCalcButton() updates the Calculate button to say 'Calculate', and sets an internal flag to indicate this.
            updateCalcButton(1);

        } else{     //debug
            Log.i("running", "Error: else{} block inside gradeCalcInitializer() has been reached.");
        }
    }

    // clearFields() resets all ten EditText views to empty
    public void clearFields(){
        grade1.setText("");
        credit1.setText("");
        grade2.setText("");
        credit2.setText("");
        grade3.setText("");
        credit3.setText("");
        grade4.setText("");
        credit4.setText("");
        grade5.setText("");
        credit5.setText("");
        gradeDisplay.setText("");
    }

    // averageGrades() performs the weighted average calculations, and skips over empty rows to avoid mathematical runtime errors
    public float averageGrades(boolean emptyRowsKey[]){
        Log.i("running", "averageGrades() is running");

        // Create an array for holding sums and products as they are calculated
        float[] valuesArray = new float[10];

        // Declare holder variables and accumulator variables
        String gradeString, creditString;   // On each iteration of the below loop, gradeString and creditString will hold the values pulled from
                                            // EditText objects (as Strings) before they are converted to floats.

        float product = 0, gradeSum = 0, creditSum = 0; // At the end of each iteration of the below loop, the given course's Quality Points
                                                        // will be calculated and stored in 'product', and then gradeSum and creditSum will accumulate
                                                        // the total values of Quality Points and Credits

        for( int i = 0; i <= 4; i++ ){
            Log.i("running", "inside averageGrades(), the for-loop has begun with i = "+i);

            /* Here we use the emptyRowsKey. As we iterate through this for-loop, if a given emptyRowsKey element 0-4 is 'false', then we know that
               that corresponding row of the UI input grid was left blank. The loop then skips to the next iteration.
               We do not want to attempt to pull a numerical value from an empty EditText view, nor do we want to accidentally include a value of '0'
               into our grade averaging algorithm */
            if(emptyRowsKey[i] == false){
                Log.i("running", "row "+(i+1)+" has been detected as empty and skipped");
                continue;
            }

            // If operation reaches this switch statement, we know that the 'gradeX' and 'creditX' views for the given row have some content in them
            switch (i){
                // In each case, we pull the text from a view, and assign it to a string holder variable (gradeString or creditString)
                // Then, we transform that number into a float and place it into the appropriate spot in the valuesArray[] array
                case 0:
                    gradeString = grade1.getText().toString();
                    valuesArray[i] = parseFloat(gradeString);
                    creditString = credit1.getText().toString();
                    valuesArray[i+5] = parseFloat(creditString);
                    break;
                case 1:
                    gradeString = grade2.getText().toString();
                    valuesArray[i] = parseFloat(gradeString);
                    creditString = credit2.getText().toString();
                    valuesArray[i+5] = parseFloat(creditString);
                    break;
                case 2:
                    gradeString = grade3.getText().toString();
                    valuesArray[i] = parseFloat(gradeString);
                    creditString = credit3.getText().toString();
                    valuesArray[i+5] = parseFloat(creditString);
                    break;
                case 3:
                    gradeString = grade4.getText().toString();
                    valuesArray[i] = parseFloat(gradeString);
                    creditString = credit4.getText().toString();
                    valuesArray[i+5] = parseFloat(creditString);
                    break;
                case 4:
                    gradeString = grade5.getText().toString();
                    valuesArray[i] = parseFloat(gradeString);
                    creditString = credit5.getText().toString();
                    valuesArray[i+5] = parseFloat(creditString);
                    break;

            }

            // After a pair of values are written into valuesArray[], we store their product (the course's Quality Points) in the holder variable 'product'
            product = valuesArray[i]*valuesArray[i+5];
            // and then add that product to the accumulator variable 'gradeSum'
            gradeSum = gradeSum + product;
            // We then add the course's credit value to the accumulator variable 'creditSum'
            creditSum = creditSum + valuesArray[i+5];
        }
        Log.i("running", "inside averageGrades(), the computation loop has finished");


        // The final result is simply the total of all quality points (accumulated into 'gradeSum') divided by the total credits taken (accumulated into 'creditSum')
        float finito = gradeSum/creditSum;

        Log.i("running", "averageGrades() has reached its return stmt");
        Log.i("running", "result of computation = "+finito);
        return finito;
    }

    // changeGradeDisplayColor() changes the color of gradeDisplay to green/yellow/red as appropriate
    public void changeGradeDisplayColor(float gradeInput){
        if(gradeInput < 60){
            gradeDisplay.setTextColor(getResources().getColor(R.color.redBkgd));
        }
        else if(gradeInput >= 61 && gradeInput < 80){
            gradeDisplay.setTextColor(getResources().getColor(R.color.yellowBkgd));
        }
        else if(gradeInput >= 80){
            gradeDisplay.setTextColor(getResources().getColor(R.color.greenBkgd));
        }
        else{
            Log.i("running", "Error: changeGradeDisplay() has received value of "+gradeInput);
        }
    }

    // updateCalcButton() is a method that compartmentalizes the process of switching the main button from 'Calculate' to 'Clear'
    // it takes a simple '0' or '1' as input
    public void updateCalcButton(int input){
        Log.i("running", "updateCalcButton() has launched");
        switch(input){
            // In each case, the value of clearOrCalc is flipped, and the text of calcButton is changed
            case 0:
                calcButton.setText("Clear");
                clearOrCalc = true;
                Log.i("running", "Reached return stmt of updateCalcButton() case 0");
                return;
            case 1:
                calcButton.setText("Calculate");
                clearOrCalc = false;
                Log.i("running", "Reached return stmt of updateCalcButton() case 1");
                return;
            default:
                Log.i("running", "Error: updateCalcButton() has received value of "+input);
                return;
        }
    }
}
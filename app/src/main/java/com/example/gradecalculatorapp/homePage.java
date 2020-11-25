package com.example.gradecalculatorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class homePage extends AppCompatActivity {

    CalculatorDatabaseHelper calculatorDatabaseHelper;

    TextView textViewProgramName,textViewStudentID,textViewProgramDetails;
    Button AddSemesterButton,btnLogout;
    String SessionStudentID;
    String[] studentData = new String[7];
    String[] programData = new String[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(homePage.this);
        final SharedPreferences.Editor editor= sharedPreferences.edit();

        calculatorDatabaseHelper = new CalculatorDatabaseHelper(this);

        textViewProgramName = (TextView)findViewById(R.id.textViewProgramName);
        textViewStudentID = (TextView)findViewById(R.id.textViewStudentID);
        textViewProgramDetails = (TextView)findViewById(R.id.textViewProgramDetails);

        AddSemesterButton = (Button) findViewById(R.id.AddSemesterButton);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        SessionStudentID = sharedPreferences.getString("SessionStudentID","0");
        textViewStudentID.setText(SessionStudentID);
        studentData = calculatorDatabaseHelper.getStudentDataForHomePage(SessionStudentID);
        programData = calculatorDatabaseHelper.getProgramName(studentData[4]);
        textViewProgramName.setText(programData[0]);

        Double percentage = calculatorDatabaseHelper.getPercentageAll(SessionStudentID);
        String[] gradeGPA = calculatorDatabaseHelper.getGradeGPA(percentage);

        textViewProgramDetails.setText("Your GPA is "+gradeGPA[0] + "\nYour Grade is " + gradeGPA[1]);
//        textViewProgramDetails.setText("The program is of " + programData[1] + " Credits , Stream is " + studentData[5] + " and MiniStream is " + studentData[6]);

        List<String> studentSemesterDetails = calculatorDatabaseHelper.getstudentSemesterDetails(SessionStudentID);
        Log.d("The Semester details ",studentSemesterDetails.toString());
        ListView listViewDisplaySemester = findViewById(R.id.listViewDisplaySemester);
        final homePageAdapter homePageAdapter = new homePageAdapter(studentSemesterDetails);
        listViewDisplaySemester.setAdapter(homePageAdapter);
        listViewDisplaySemester.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textViewInformation = (TextView) view.findViewById(R.id.textViewCourseID);
                String semesterInformation = textViewInformation.getText().toString().trim();
                Toast.makeText(homePage.this, semesterInformation, Toast.LENGTH_SHORT).show();
                Intent semsterCourseIntent = new Intent(homePage.this, semesterCourses.class);
                Bundle myBundle = new Bundle();
                myBundle.putString("semesterInformation",semesterInformation);
                semsterCourseIntent.putExtras(myBundle);
                startActivity(semsterCourseIntent);
            }
        });
        AddSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addSemesterIntent = new Intent(homePage.this, addSemester.class);
                startActivity(addSemesterIntent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("SessionStudentID", "0");
                editor.commit();
                Intent loginIntent = new Intent(homePage.this, loginActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
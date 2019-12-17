package com.sop.firebasech2;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sop.firebasech2.Logica.Manejador;
import com.sop.firebasech2.objetos.Occurence;
import com.sop.firebasech2.objetos.Utils;

import java.util.Calendar;

public class GenerateReportsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnInitialDate;
    private Button mBtnFinalDate;
    private EditText mEtInitialDate;
    private EditText mEtFinalDate;
    private EditText mEtReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_reports);

        mEtInitialDate = findViewById(R.id.etInitialDate);
        mEtFinalDate = findViewById(R.id.etFinalDate);
        mEtReport = findViewById(R.id.etReport);

        mBtnInitialDate = findViewById(R.id.btnInitialDate);
        mBtnInitialDate.setOnClickListener(this);

        mBtnFinalDate = findViewById(R.id.btnFinalDate);
        mBtnFinalDate.setOnClickListener(this);

        Button mBtnGenerateReport = findViewById(R.id.btnGenerateReport);
        mBtnGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iDate = mEtInitialDate.getText().toString();
                String fDate = mEtFinalDate.getText().toString();

                if (!iDate.isEmpty() && !fDate.isEmpty()) {
                    // Dates are selected, they are not null or empty
                    if (Utils.validateDateInterval(iDate, fDate)){
                        // Dates are in correct form, correct interval of time
                        //TODO Generate report. Make query to db
                        generateReport(iDate, fDate);

                    } else {
                        // Incorrect selection of interval. Initial is after Final date
                        Toast.makeText(GenerateReportsActivity.this,
                                "Incorrect selection of interval. Final date must be after initial one!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    // At least one date was not selected
                    Toast.makeText(GenerateReportsActivity.this,
                            "At least one date was not selected. Initial and Final date must be entered!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        final Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int id = v.getId();
        switch (id) {
            case R.id.btnInitialDate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mEtInitialDate.setText(year+"-"+Utils.dateNumberDecorator(month+1)+"-"+Utils.dateNumberDecorator(dayOfMonth));
                    }
                }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
                break;
            case R.id.btnFinalDate:
                DatePickerDialog datePDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mEtFinalDate.setText(year+"-"+Utils.dateNumberDecorator(month+1)+"-"+Utils.dateNumberDecorator(dayOfMonth));
                    }
                }, currentYear, currentMonth, currentDay);
                datePDialog.show();
                break;
        }
    }

    public void generateReport(String initialDate, String finalDate){
        Manejador manejador = new Manejador();
        Query query = manejador.getSymptomReportByDates(initialDate, finalDate);
        Log.d("GenerateReport", "Se ejecuto");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO Add the printing of reports
                Occurence symptom = new Occurence();
                String dataString = "";
                int i = 1;
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Log.d("GenerateReport", "index: "+i);
                    symptom = ds.getValue(Occurence.class);
                    dataString += templateReport(symptom, i);
                    i++;
                }

                if (dataString.isEmpty()){
                    //TODO Print message about there were not results from that query specificatio, (for share func disable the future btn)
                } else {
                    printReport(dataString);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Add message of error
                //TODO Add message of error when Database Error
                String error = "<b>Error:"+ databaseError.getMessage() +"</b>";
                printReport(error);
            }
        });
    }

    public String templateReport(Occurence symptom, int index) {
        String msg = "<h3>Symptom "+ index +":</h3> " + symptom.getTitle();
        msg += "<p><b>Date:</b> "+ symptom.getTimeOfOccurence() +"    <b>Intensity:</b> "+ symptom.getIntensity() +"</p>";
        msg += "<p>"+ symptom.getDescription() +"</p><hr>";
        return msg;
        //mEtReport.setText(Html.fromHtml(msg));
    }

    public void printReport(String data){
        mEtReport.setText(Html.fromHtml(data));
    }
}

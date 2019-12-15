package com.sop.firebasech2;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.sop.firebasech2.objetos.Utils;

import java.time.LocalDateTime;
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
                        // Generate report. Make query to db
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
                        mEtInitialDate.setText(Utils.dateNumberDecorator(dayOfMonth)+"-"+Utils.dateNumberDecorator(month+1)+"-"+year);
                    }
                }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
                break;
            case R.id.btnFinalDate:
                DatePickerDialog datePDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mEtFinalDate.setText(Utils.dateNumberDecorator(dayOfMonth)+"-"+Utils.dateNumberDecorator(month+1)+"-"+year);
                    }
                }, currentYear, currentMonth, currentDay);
                datePDialog.show();
                break;
        }
    }
}

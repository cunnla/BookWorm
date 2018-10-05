package cunnla.cunnla.bookworm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBook extends AppCompatActivity implements View.OnClickListener {

    Button btnOK, btnCancel;
    EditText etName, etAuthor, etGenre, etNotes;

    TextView tvDate;
    String dateString = null;
    Date theDate;

    Book selectedBook;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        btnOK = (Button) findViewById(R.id.btnOk);
        btnOK.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setText("Touch here to set up date");
        tvDate.setOnClickListener(this);

        etName = (EditText)findViewById(R.id.etName);
        etAuthor = (EditText)findViewById(R.id.etAuthor);
        etGenre = (EditText)findViewById(R.id.etGenre);
        etNotes = (EditText)findViewById(R.id.etNotes);

        selectedBook = new Book();

    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.btnOk:
                if (!etName.getText().toString().equalsIgnoreCase("") &&
                        dateString!=null    ) {              // if a name and a date is entered
                    selectedBook.bookDate = dateString;
                    selectedBook.bookName = etName.getText().toString();
                    selectedBook.bookAuthor = etAuthor.getText().toString();
                    selectedBook.bookGenre = etGenre.getText().toString();
                    selectedBook.bookNotes = etNotes.getText().toString();
                    intent = new Intent();
                    selectedBook.putDetailsToIntent(intent);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, "Please enter a valid book date and name", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnCancel:
                intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.tvDate:
                dateString = setDateString();
                break;
        }


    }

    public String setDateString(){

        Calendar myCalendar= Calendar.getInstance();

        //////////////////
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        theDate = new Date();

        try {
            if (dateString!=null) {
                theDate = df.parse(dateString);
            }
        } catch (ParseException e) {
            //Handle exception here, most of the time you will just log it.
            e.printStackTrace();
        }

        myCalendar.setTime(theDate);
        /////////////////

        int year=myCalendar.get(Calendar.YEAR);
        int month=myCalendar.get(Calendar.MONTH);
        int day=myCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener datePickerDialogListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day){
                month++;   //because in Android month starts from 0
                String sMonth = Integer.toString(month); // this is to make the string look like YYYY-MM-DD HH:mm:SS
                String sDay = Integer.toString(day);     // which is the mysql date format
                if (month<10) {sMonth = "0"+month;}
                if (day<10) {sDay = "0"+day;}
                dateString = (year+"-"+sMonth+"-"+sDay+" 00:00:00");
                Log.d("myLogs", "dateString: "+ dateString);
                tvDate.setText(sDay+"."+sMonth+"."+year);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerDialogListener, year, month, day);
        datePickerDialog.show();

        return dateString;

    }

}

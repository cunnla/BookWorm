package cunnla.cunnla.bookworm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AddEditBook extends AppCompatActivity implements View.OnClickListener,  AdapterView.OnItemSelectedListener {

    Button btnOK, btnCancel;
    EditText etName, etAuthor, etNotes;
    TextView tvDate;

    Spinner spGenre;
    String strGenre;
    ArrayList<String> genreList;

    Date theDate;
    String dateString;

    Intent intent;

    public Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);


        btnOK = (Button) findViewById(R.id.btnOk);
        btnOK.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setOnClickListener(this);


        etName = (EditText)findViewById(R.id.etName);
        etAuthor = (EditText)findViewById(R.id.etAuthor);
        etNotes = (EditText)findViewById(R.id.etNotes);

        spGenre = (Spinner) findViewById(R.id.spGenre);
        spGenre.setOnItemSelectedListener(this);
        showGenreSpinner();


        intent = getIntent();
        selectedBook = new Book();
        selectedBook.getDetailsFromIntent(intent);


        tvDate.setText(selectedBook.bookDateNice());
        etName.setText(selectedBook.bookName);
        etAuthor.setText(selectedBook.bookAuthor);
        spGenre.setSelection(genreList.indexOf(selectedBook.bookGenre));
        etNotes.setText(selectedBook.bookNotes);

        if (selectedBook.bookDate!=null) {
            dateString = selectedBook.bookDate;
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnOk:
                if (!etName.getText().toString().equalsIgnoreCase("") ) {              // if a name is entered
                    selectedBook.bookDate = dateString;
                    selectedBook.bookName = etName.getText().toString();
                    selectedBook.bookAuthor = etAuthor.getText().toString();
                    selectedBook.bookGenre = strGenre;
                    selectedBook.bookNotes = etNotes.getText().toString();

                    intent = new Intent();
                    selectedBook.putDetailsToIntent(intent);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, "Please enter a valid book name", Toast.LENGTH_LONG).show();
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


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(pos > 0){
            strGenre = parent.getItemAtPosition(pos).toString();
        } else {
            strGenre = selectedBook.bookGenre;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void showGenreSpinner(){

        genreList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.genre_array)));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this ,R.layout.list_spinner,genreList){

            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };
        spinnerArrayAdapter.remove(spinnerArrayAdapter.getItem(1));
        spGenre.setAdapter(spinnerArrayAdapter);

    }

    public String setDateString(){

        Calendar myCalendar= Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        theDate = new Date();

        try {
            if (dateString!=null) {
                theDate = df.parse(dateString);
            } else {
                Log.d ("myLogs", "date is empty");
                theDate=Calendar.getInstance().getTime();
            }
        } catch (ParseException e) {
            //Handle exception here, most of the time you will just log it.
            e.printStackTrace();
        }

        myCalendar.setTime(theDate);

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

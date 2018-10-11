package cunnla.cunnla.bookworm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Utils {



    //final static int SORT_ARRAY = R.array.;  //does not exist so far, I may need to create it later
    final static int GENRE_ARRAY = R.array.genre_array;
    final static int SPINNER_LAYOUT = R.layout.list_spinner;

    ArrayList<String> genreList;

    Date theDate;
    String dateString;

    utilsCallBack utilsCallBack = null;

    public Utils(){}   // empty constructor

    public Utils (utilsCallBack callBack){    // callback constructor
        this.utilsCallBack=callBack;          // I will use the callback to update the text view in AddBook and EditBook
    }                                         // when setting up the time



    public void updateTextView(String str) {  // method used in callback
        this.utilsCallBack.updateTextView(str);
    }


    public void showGenreSpinner(Context context, Spinner spinner){

        genreList = new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(GENRE_ARRAY)));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context ,SPINNER_LAYOUT,genreList){

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
        spinner.setAdapter(spinnerArrayAdapter);

    }


    public String setDateString(Context context){

        Calendar myCalendar= Calendar.getInstance();

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
                updateTextView(sDay+"."+sMonth+"."+year);  // using callback here
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, datePickerDialogListener, year, month, day);
        datePickerDialog.show();

        return dateString;

    }






}

interface utilsCallBack
{
    // Declaration of the template function for the interface
    public void updateTextView(String mystr);
}

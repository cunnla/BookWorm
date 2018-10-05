package cunnla.cunnla.bookworm;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Book {

    String bookDate, bookName, bookAuthor, bookGenre, bookNotes, id;
    String dateString = "";


     public Book(){
       this.bookDate = "";
       this.bookName = "";
       this.bookAuthor = "";
       this.bookGenre = "";
       this.bookNotes = "";
       this.id = "";
     }

    public Book(String bookDate, String bookName, String bookAuthor, String bookGenre, String bookNotes, String id){
        this.bookDate = bookDate;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookGenre = bookGenre;
        this.bookNotes = bookNotes;
        this.id = id;
    }


     public ContentValues addBookToCV(){
         ContentValues cv = new ContentValues();
         cv.clear();
         cv.put("bookDate", this.bookDate);
         cv.put("bookName", this.bookName);
         cv.put("bookAuthor", this.bookAuthor);
         cv.put("bookGenre", this.bookGenre);
         cv.put("bookNotes", this.bookNotes);
         return cv;
     }

     public Intent putDetailsToIntent(Intent intent){
         intent.putExtra("bookDate", this.bookDate);
         intent.putExtra("bookName", this.bookName);
         intent.putExtra("bookAuthor", this.bookAuthor);
         intent.putExtra("bookGenre", this.bookGenre);
         intent.putExtra("bookNotes", this.bookNotes);
         intent.putExtra ("id", this.id);
         return intent;
     }

     public void getDetailsFromIntent(Intent intent){
         this.bookDate = intent.getStringExtra("bookDate");
         this.bookName = intent.getStringExtra("bookName");
         this.bookAuthor = intent.getStringExtra("bookAuthor");
         this.bookGenre = intent.getStringExtra("bookGenre");
         this.bookNotes = intent.getStringExtra("bookNotes");
         this.id = intent.getStringExtra("id");
     }

     public String bookDateNice() {
         // here we take the date in the mysql format  YYYY-MM-DD HH:MM:SS to 0d-0m-yyyy

         String result = "";

         Calendar myCal = Calendar.getInstance();
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Date theDate = new Date();

         Log.d("myLogs", "bookDateNice data: "+this.toString());

         try {
               if (this.bookDate!=null) {
               theDate = df.parse(this.bookDate);
              // String strDate = df.format(myCal.getTime());   // checking if the date is correct
              // Log.d("myLogs", "strDate: "+ strDate);
               }

               myCal.setTime(theDate);

               int intDay = myCal.get(Calendar.DAY_OF_MONTH);
               int intMonth = myCal.get(Calendar.MONTH);
               intMonth++;
               Log.d("myLogs", "DAY and MONTH: "+ intDay + " "+intMonth);

               String sDay = Integer.toString(intDay);
               String sMonth = Integer.toString(intMonth);

               if (intDay<10) {
                   sDay = "0"+sDay;
               }

               if (intMonth<10) {
                   sMonth = "0"+sMonth;
               }

               result = sDay + "." + sMonth + "." + myCal.get(Calendar.YEAR);
               Log.d("myLogs", "result: "+ result);
         }
               catch (ParseException e){
                      e.printStackTrace();
               }


         return result;

     }



    @Override
    public String toString() {
        return this.bookDate + " : " + this.bookName + " by " + this.bookAuthor + ". "+ this.bookGenre + ". id: " + this.id;
    }
}

package cunnla.cunnla.bookworm;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.DatePicker;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Book implements Serializable{     //implements Serializable in order to pass the data from intent to intent

    String bookDate, bookName, bookAuthor, bookGenre, bookNotes, id;
    //String dateString = "";


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

         if (intent.getStringExtra("bookDate")!=null) {
             this.bookDate = intent.getStringExtra("bookDate");
         } else {
             Calendar myCalendar= Calendar.getInstance();
             SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             this.bookDate = (df.format(myCalendar.getTime()));
         }

         if (intent.getStringExtra("bookName")!=null) {
             this.bookName = intent.getStringExtra("bookName");
         } else {
             this.bookName = "";
         }

         if (intent.getStringExtra("bookAuthor")!=null) {
             this.bookAuthor = intent.getStringExtra("bookAuthor");
         } else {
             this.bookAuthor = "";
         }

         if (intent.getStringExtra("bookGenre")!=null) {
             this.bookGenre = intent.getStringExtra("bookGenre");
         } else {
             this.bookGenre = "";
         }

         if (intent.getStringExtra("bookNotes")!=null) {
             this.bookNotes = intent.getStringExtra("bookNotes");
         } else {
             this.bookNotes = "";
         }

         if (intent.getStringExtra("id")!=null) {
             this.id = intent.getStringExtra("id");
         }

         Log.d("myLogs", "Intent: bookdata: "+this.toString());
     }

    public Bundle putDetailsToBundle(){   //we need the bundle for the IntentService
        Bundle bookExtras = new Bundle();
        bookExtras.putString("bookDate", this.bookDate);
        bookExtras.putString("bookName", this.bookName);
        bookExtras.putString("bookAuthor", this.bookAuthor);
        bookExtras.putString("bookGenre", this.bookGenre);
        bookExtras.putString("bookNotes", this.bookNotes);
        bookExtras.putString("id", this.id);
        return bookExtras;
    }

    public void getDetailsFromBundle(Bundle bundle){     //we need the bundle for the IntentService

        if (bundle.getString("bookDate")!=null) {
            this.bookDate = bundle.getString("bookDate");
        }

        if (bundle.getString("bookName")!=null) {
            this.bookName = bundle.getString("bookName");
        } else {
            this.bookName = "";
        }

        if (bundle.getString("bookAuthor")!=null) {
            this.bookAuthor = bundle.getString("bookAuthor");
        } else {
            this.bookAuthor = "";
        }

        if (bundle.getString("bookGenre")!=null) {
            this.bookGenre = bundle.getString("bookGenre");
        } else {
            this.bookGenre = "";
        }

        if (bundle.getString("bookNotes")!=null) {
            this.bookNotes = bundle.getString("bookNotes");
        } else {
            this.bookNotes = "";
        }

        if (bundle.getString("id")!=null) {
            this.id = bundle.getString("id");
        }

        Log.d("myLogs", "Bundle: bookdata: "+this.toString());
    }



     public String bookDateNice() {
         // here we take the date in the mysql format  YYYY-MM-DD HH:MM:SS to 0d-0m-yyyy

         String result = "";

         Calendar myCal = Calendar.getInstance();
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Date theDate = new Date();

         Log.d("myLogs", "bookDateNice data: "+this.toString());

         try {
               if ((this.bookDate!=null)) {
                   theDate = df.parse(this.bookDate);
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

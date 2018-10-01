package cunnla.cunnla.bookworm;

import android.content.ContentValues;
import android.content.Intent;

public class Book {

    String bookDate, bookName, bookAuthor, bookGenre, bookNotes, id;


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


    @Override
    public String toString() {
        return this.bookDate + ": " + this.bookName + " by " + this.bookAuthor + ". "+ this.bookGenre + ". id: " + this.id;
    }
}

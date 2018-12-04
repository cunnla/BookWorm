package cunnla.cunnla.bookworm;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;

public class DBIntentService extends IntentService {

    DBHelper dbHelper;
    SQLiteDatabase db;

    Book selectedBook;

    Bundle bookExtras;


    public DBIntentService() {
        super("dbThread");
    }

    public void onCreate() {
        super.onCreate();
        Log.d("intentServiceLogs", "onCreate");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        selectedBook = new Book();

        String task = intent.getStringExtra("task");



       // Log.d("intentServiceLogs", "strSelection: " + strSelection +", strShowGenre: " + strArrayShowGenre[0] +", orderBy: "+orderBy);

        Log.d("intentServiceLogs", "onHandleIntent start: " + task+" book id: "+selectedBook.id);


        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        switch (task){

            case "deleteBook":
                bookExtras = intent.getParcelableExtra("selectedBook");
                selectedBook.getDetailsFromBundle(bookExtras);
                db.delete("bookTable", "id = " + selectedBook.id, null);
                break;
            case "updateBook":
                bookExtras = intent.getParcelableExtra("selectedBook");
                selectedBook.getDetailsFromBundle(bookExtras);
                db.update("bookTable", selectedBook.addBookToCV(),"id = " + selectedBook.id, null);
                break;
            case "addBook":
                bookExtras = intent.getParcelableExtra("selectedBook");
                selectedBook.getDetailsFromBundle(bookExtras);
                db.insert("bookTable", null, selectedBook.addBookToCV());
                break;
            case "showAllBooks":

                ArrayList<Book> booksList = new ArrayList<>();

                String orderBy = "bookDate DESC";
                String[]strArrayShowGenre = null;
                String strSelection = null;


                if (intent.getStringExtra("strSelection")!=null){
                    strSelection = intent.getStringExtra("strSelection");
                }
                strArrayShowGenre = intent.getExtras().getStringArray("strArrayShowGenre");
                orderBy = intent.getStringExtra("orderBy");


                Log.d("intentServiceLogs", "strSelection: "+strSelection);
                Log.d("intentServiceLogs", "strArrayShowGenre: "+strArrayShowGenre);



                Cursor cursor = db.query("bookTable", null, strSelection, strArrayShowGenre, null, null, orderBy);
                if (cursor.moveToFirst()) {
                    do{
                        booksList.add( new Book(cursor.getString(cursor.getColumnIndex("bookDate")),
                                cursor.getString(cursor.getColumnIndex("bookName")),
                                cursor.getString(cursor.getColumnIndex("bookAuthor")),
                                cursor.getString(cursor.getColumnIndex("bookGenre")),
                                cursor.getString(cursor.getColumnIndex("bookNotes")),
                                cursor.getString(cursor.getColumnIndex("id"))
                        ));
                        Log.d("intentServiceLogs", "Adding to books list:"+ cursor.getString(cursor.getColumnIndex("bookName")));
                    }while (cursor.moveToNext());
                }else
                    Log.d("intentServiceLogs", "0 rows");
                cursor.close();

                // sending the info back to the activity. The info we send is the ArrayList<Book> booksList
                Intent responseIntent = new Intent();

                responseIntent.setAction("MY_RESPONSE");
                responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
                responseIntent.putExtra("booksList", booksList);
                sendBroadcast(responseIntent);

                break;

        }


    }

}

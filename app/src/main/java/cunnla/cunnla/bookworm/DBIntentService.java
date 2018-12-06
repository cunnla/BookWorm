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
                String strGenreSelection = null;
                String strSearchSelection = null;
                String strGenreArgs = null;
                String strSearchArgs = null;
                String strSelection = null;
                String[]strArrayArgs = null;


              // getting the genre and search strings from the intent extras
                if (intent.getStringExtra("strGenreSelection")!=null){
                    strGenreSelection = intent.getStringExtra("strGenreSelection");
                    strGenreArgs = intent.getStringExtra("strGenreArgs");
                    Log.d("intentServiceLogs", "strGenreSelection:  "+strGenreSelection);
                    Log.d("intentServiceLogs", "strGenreArgs:  "+strGenreArgs);
                }


                if (intent.getStringExtra("strSearchSelection")!=null){
                    strSearchSelection = intent.getStringExtra("strSearchSelection");
                    strSearchArgs = intent.getStringExtra("strSearchArgs");
                    Log.d("intentServiceLogs", "strSearchSelection:  "+strSearchSelection);
                    Log.d("intentServiceLogs", "strSearchArgs:  "+strSearchArgs);
                }

                // forming the Selection and Arguments strings
                if ((strGenreSelection!=null) && (strSearchSelection==null)){   //if only genre is selected
                    Log.d("intentServiceLogs", "Only genre is selected");
                    strSelection = strGenreSelection;
                    strArrayArgs = new String[]{strGenreArgs};
                    Log.d("intentServiceLogs", "strSelection: "+strSelection);
                    Log.d("intentServiceLogs", "strArrayArgs: "+strArrayArgs[0]);
                }
                else if ((strGenreSelection==null) && (strSearchSelection!=null)){   //if only search is selected
                    Log.d("intentServiceLogs", "Only search is selected");
                    strSelection = strSearchSelection;
                    strArrayArgs = new String[]{strSearchArgs,strSearchArgs};
                    Log.d("intentServiceLogs", "strSelection: "+strSelection);
                }
                else if ((strGenreSelection!=null) && (strSearchSelection!=null)){   //if both search and genre are selected
                    Log.d("intentServiceLogs", "Both search and genre are selected");
                    strSelection = "("+strSearchSelection+") AND "+strGenreSelection;
                    strArrayArgs = new String[]{strSearchArgs,strSearchArgs,strGenreArgs};
                    Log.d("intentServiceLogs", "strSelection: "+strSelection);
                    Log.d("intentServiceLogs", "strArrayArgs: "+strArrayArgs[0]+" "+strArrayArgs[1]+" "+strArrayArgs[2]);
                }

                orderBy = intent.getStringExtra("orderBy");
                Log.d("intentServiceLogs", "orderBy: "+orderBy);



                Cursor cursor = db.query("bookTable", null, strSelection, strArrayArgs, null, null, orderBy);
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
        db.close();


    }

}

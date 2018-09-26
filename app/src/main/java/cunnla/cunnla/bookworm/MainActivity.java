package cunnla.cunnla.bookworm;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd;

    DBHelper dbHelper;
    SQLiteDatabase db;
    ListView listBooks;

    Book selectedBook;


    final String LOG_TAG = "myLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        listBooks = (ListView) findViewById(R.id.listBooks);
        registerForContextMenu(listBooks);

        showAllBooks();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listBooks) {

            // checking which exactly book is selected
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            selectedBook = (Book) lv.getItemAtPosition(acmi.position);

            menu.add(0, 1, 1, "View");
            menu.add(0, 2, 2, "Edit");
            menu.add(0, 3, 3, "Delete");
           // menu.add(0,4,4,selectedBook.bookName);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 1:  // view
                Toast.makeText(this, "View: "+selectedBook.bookName, Toast.LENGTH_SHORT).show();
                break;
            case 2:  // edit
                Toast.makeText(this, "Edit: "+selectedBook.bookName, Toast.LENGTH_SHORT).show();
                break;
            case 3: // delete
                Toast.makeText(this, "Delete: "+selectedBook.bookName+", id: "+selectedBook.id, Toast.LENGTH_LONG).show();
                db.delete("bookTable", "id = " + selectedBook.id, null);
                showAllBooks();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, AddBook.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}

        ContentValues cv = new ContentValues();

        // fill the table
        cv.clear();
        cv.put("bookDate", data.getStringExtra("bookDate"));
        cv.put("bookName", data.getStringExtra("bookName"));
        cv.put("bookAuthor", data.getStringExtra("bookAuthor"));
        cv.put("bookGenre", data.getStringExtra("bookGenre"));
        cv.put("bookNotes", data.getStringExtra("bookNotes"));
        db.insert("bookTable", null, cv);

        showAllBooks();

    }

    public void showAllBooks(){

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("bookTable", null, null, null, null, null, null);

        ArrayList<Book> booksList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do{
                booksList.add( new Book(cursor.getString(cursor.getColumnIndex("bookDate")),
                        cursor.getString(cursor.getColumnIndex("bookName")),
                        cursor.getString(cursor.getColumnIndex("bookAuthor")),
                        cursor.getString(cursor.getColumnIndex("bookGenre")),
                        cursor.getString(cursor.getColumnIndex("bookNotes")),
                        cursor.getString(cursor.getColumnIndex("id"))
                ));
            }while (cursor.moveToNext());
        }else
            Log.d(LOG_TAG, "0 rows");

        cursor.close();
//        dbHelper.close();

        BookAdapter bookAdapter = new BookAdapter(this, 0, booksList);
        listBooks.setAdapter(bookAdapter);
    }

    public class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context){
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");

            // создаем таблицу
            db.execSQL("create table bookTable ("
                    + "id integer primary key,"
                    + "bookDate text," + "bookName text," + "bookAuthor text," + "bookGenre text,"+ "bookNotes text"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


}

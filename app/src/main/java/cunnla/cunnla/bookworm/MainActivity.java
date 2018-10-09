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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener  {

    Button btnAdd;
    Spinner spSort, spShowGenre;
    String orderBy = "bookDate";
    String[] strShowGenre = null;
    String strSelection = null;

    DBHelper dbHelper;
    SQLiteDatabase db;
    ListView listBooks;

    Book selectedBook;

    Intent intent;
    final static int INTENT_CODE_ADD = 1;
    final static int INTENT_CODE_VIEW = 2;
    final static int INTENT_CODE_EDIT = 3;
    final static int INTENT_CODE_DELETE = 4;


    final String LOG_TAG = "myLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        spSort = (Spinner) findViewById(R.id.spSort);
        spSort.setOnItemSelectedListener(this);
        spShowGenre = (Spinner) findViewById(R.id.spShowGenre);
        spShowGenre.setOnItemSelectedListener(this);

        listBooks = (ListView) findViewById(R.id.listBooks);
        listBooks.setOnItemClickListener(this);
        registerForContextMenu(listBooks);

        dbHelper = new DBHelper(this);

        selectedBook = new Book();

        orderBy = "bookDate";
        strShowGenre = null;
        strSelection = null;
        showAllBooks();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
         Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);
         selectedBook = (Book) parent.getAdapter().getItem(position);  // getting our object Book from position in ListView
         intent = new Intent(this, ViewBook.class);
         selectedBook.putDetailsToIntent(intent);
         startActivityForResult(intent, INTENT_CODE_VIEW);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d("myLogs", "Got to onItemSelected");

        switch (parent.getId()) {
            case R.id.spSort:
                Log.d("myLogs", "Got to the sorting selection");
                         switch (pos) {
                             case 0:
                                orderBy = "bookDate";
                                 showAllBooks();
                                 break;
                             case 1:
                                 orderBy = "bookName";
                                 showAllBooks();
                                 break;
                             case 2:
                                orderBy = "bookAuthor";
                                showAllBooks();
                                break;
                             case 3:
                                orderBy = "bookGenre";
                                showAllBooks();
                                break;
                             case 4:
                                orderBy = "bookNotes";
                                showAllBooks();
                                break;
                             default:
                                orderBy = "bookDate";
                                showAllBooks();
                                break;
                         }
            break;

            case R.id.spShowGenre:
                Log.d("myLogs", "Got to the genre selection");
                switch (pos) {
                    case 0:
                        strSelection = null;
                        strShowGenre = null;
                        showAllBooks();
                        break;
                    case 1:
                        Log.d("myLogs", strSelection+" "+strShowGenre);
                        strSelection = "bookGenre = ?";
                        strShowGenre = new String[] {"Drama"};
                        showAllBooks();
                        break;
                    case 2:
                        Log.d("myLogs", strSelection+" "+strShowGenre);
                        strSelection = "bookGenre = ?";
                        strShowGenre = new String[] {"Science Fiction"};
                        showAllBooks();
                        break;
                    case 3:
                        Log.d("myLogs", strSelection+" "+strShowGenre);
                        strSelection = "bookGenre = ?";
                        strShowGenre = new String[] {"Fantasy"};
                        showAllBooks();
                        break;
                    case 4:
                        Log.d("myLogs", strSelection+" "+strShowGenre);
                        strSelection = "bookGenre = ?";
                        strShowGenre = new String[] {"Detective Fiction"};
                        showAllBooks();
                        break;
                    case 5:
                        Log.d("myLogs", strSelection+" "+strShowGenre);
                        strSelection = "bookGenre = ?";
                        strShowGenre = new String[] {"Poetry"};
                        showAllBooks();
                        break;
                    case 6:
                        Log.d("myLogs", strSelection+" "+strShowGenre);
                        strSelection = "bookGenre = ?";
                        strShowGenre = new String[] {"Folklore and Mythology"};
                        showAllBooks();
                        break;
                    case 7:
                        Log.d("myLogs", strSelection+" "+strShowGenre);
                        strSelection = "bookGenre = ?";
                        strShowGenre = new String[] {"Historical Fiction"};
                        showAllBooks();
                        break;
                    case 8:
                        Log.d("myLogs", strSelection+" "+strShowGenre);
                        strSelection = "bookGenre = ?";
                        strShowGenre = new String[] {"Horror and Mystery"};
                        showAllBooks();
                        break;
                    case 9:
                        Log.d("myLogs", strSelection+" "+strShowGenre);
                        strSelection = "bookGenre = ?";
                        strShowGenre = new String[] {"Humor"};
                        showAllBooks();
                        break;
                    case 10:
                        Log.d("myLogs", strSelection+" "+strShowGenre);
                        strSelection = "bookGenre = ?";
                        strShowGenre = new String[] {"Non-Fiction and Documentary"};
                        showAllBooks();
                        break;
                    default:
                        strSelection = null;
                        strShowGenre = null;
                        showAllBooks();
                        break;
                }
             break;


        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

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
                Log.d("myLogs","Main activity: View: " + selectedBook.toString());
                intent = new Intent(this, ViewBook.class);
                selectedBook.putDetailsToIntent(intent);
                startActivityForResult(intent, INTENT_CODE_VIEW);
                break;
            case 2:  // edit
                Log.d("myLogs","Main activity: Edit: " + selectedBook.toString());
                intent = new Intent(this, EditBook.class);
                selectedBook.putDetailsToIntent(intent);
                startActivityForResult(intent, INTENT_CODE_EDIT);
                break;
            case 3: // delete
                Log.d("myLogs","Main activity: Delete: " + selectedBook.toString());
                intent = new Intent(this, DeleteBook.class);
                startActivityForResult(intent, INTENT_CODE_DELETE);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
            intent = new Intent(this, AddBook.class);
            startActivityForResult(intent, INTENT_CODE_ADD);
            break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        if (resultCode == RESULT_OK) {
            db = dbHelper.getWritableDatabase();
            switch (requestCode) {
                case INTENT_CODE_ADD:    // add book activity
                    selectedBook = new Book();
                    selectedBook.getDetailsFromIntent(data);
                    db.insert("bookTable", null, selectedBook.addBookToCV());
                    showAllBooks();
                    break;
                case INTENT_CODE_VIEW:    // view book activity
                    showAllBooks();
                    break;
                case INTENT_CODE_EDIT:    // edit book activity
                    selectedBook.getDetailsFromIntent(data);
                    Log.d("myLogs", "Book data in main activity after edit: "+selectedBook.toString());
                    db.update("bookTable", selectedBook.addBookToCV(),"id = " + selectedBook.id, null);
                    showAllBooks();
                    break;
                case INTENT_CODE_DELETE:    // delete book activity
                    db.delete("bookTable", "id = " + selectedBook.id, null);
                    showAllBooks();
                    break;
            }
            dbHelper.close();

        } else if (resultCode == RESULT_CANCELED){
            showAllBooks();
            } else  {
                 Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show();
                 }

    }

    public void showAllBooks(){

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("bookTable", null, strSelection, strShowGenre, null, null, orderBy);
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
        dbHelper.close();

        BookAdapter bookAdapter = new BookAdapter(this, 0, booksList);
        listBooks.setAdapter(bookAdapter);
    }



}

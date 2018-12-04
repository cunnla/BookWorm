package cunnla.cunnla.bookworm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener  {

    ImageButton btnAdd, btnSearch, btnSearchOK, btnSearchCancel;
    LinearLayout llSearch, llSearchBut, llSort;
    EditText etSearch;
    boolean btnSearchPressed = false;
    Spinner spSort, spShowGenre;
    String orderBy = "bookDate DESC";
    String[] strArrayShowGenre = null;
    String strSelection = null;

    ListView booksListView;

    ArrayList<Book> booksList;
    private MyBroadcastReceiver mMyBroadcastReceiver;


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

        btnAdd = (ImageButton)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnSearch = (ImageButton)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llSearchBut = (LinearLayout) findViewById(R.id.llSearchBut);
        btnSearchOK = (ImageButton)findViewById(R.id.btnSearchOK);
        btnSearchOK.setOnClickListener(this);
        btnSearchCancel = (ImageButton)findViewById(R.id.btnSearchCancel);
        btnSearchCancel.setOnClickListener(this);
        etSearch = (EditText) findViewById(R.id.etSearch);

        llSort = (LinearLayout) findViewById(R.id.llSort);
        spSort = (Spinner) findViewById(R.id.spSort);
        spSort.setOnItemSelectedListener(this);

        spShowGenre = (Spinner) findViewById(R.id.spShowGenre);
        spShowGenre.setOnItemSelectedListener(this);
        ArrayList<String> listGenre = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.genre_array)));
        ArrayAdapter<String> listGenreAdapter = new ArrayAdapter<String>(this, R.layout.list_spinner, listGenre);
        listGenreAdapter.remove(listGenreAdapter.getItem(0));
        spShowGenre.setAdapter(listGenreAdapter);


        booksListView = (ListView) findViewById(R.id.listBooks);
        booksListView.setOnItemClickListener(this);
        registerForContextMenu(booksListView);

        selectedBook = new Book();

        orderBy = "bookDate DESC";
        strArrayShowGenre = null;
        strSelection = null;

        //removing the search views
        llSearch.removeAllViews();
        llSearchBut.removeAllViews();
        //llSearchBut.removeView(btnSearchOK);
        //llSearchBut.removeView(btnSearchCancel);


        showAllBooks();


    }



    @Override       // if we click on a book
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
         Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);
         selectedBook = (Book) parent.getAdapter().getItem(position);  // getting our object Book from position in ListView
         intent = new Intent(this, ViewBook.class);
         selectedBook.putDetailsToIntent(intent);
         startActivityForResult(intent, INTENT_CODE_VIEW);
    }


    // if we click on an option in a spinner - sorting or selecting a genre
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d("myLogs", "Got to onItemSelected");

        switch (parent.getId()) {
            case R.id.spSort:
                Log.d("myLogs", "Got to the sorting selection");
                         switch (pos) {
                             case 0:
                                orderBy = "bookDate DESC";
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
                                orderBy = "bookNotes";
                                 showAllBooks();
                                break;
                             default:
                                orderBy = "bookDate DESC";
                                 showAllBooks();
                                break;
                         }
            break;

            case R.id.spShowGenre:
                Log.d("myLogs", "Got to the genre selection");
                if (pos==0) {
                    strSelection = null;
                    strArrayShowGenre = null;
                } else {
                    strSelection = "bookGenre = ?";
                    strArrayShowGenre = new String[]{parent.getAdapter().getItem(pos).toString()};

                }

                showAllBooks();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                intent = new Intent(this, AddEditBook.class);
                startActivityForResult(intent, INTENT_CODE_ADD);
                break;
            case R.id.btnSearch:

                if (!btnSearchPressed) {

                    llSort.removeAllViews(); // removing the views from the Sort layout

                    // removing any genre selection or sorting
                    strSelection = null;
                    strArrayShowGenre = null;
                    orderBy = "bookDate DESC";
                    showAllBooks();
                    spShowGenre.setSelection(0);
                    spSort.setSelection(0);


                    // and adding views to the Search layout

                    llSearch.addView(etSearch);
                    llSearch.addView(llSearchBut);
                    llSearchBut.addView(btnSearchOK);
                    llSearchBut.addView(btnSearchCancel);


                    btnSearchPressed = true;

                } else if (btnSearchPressed){
                    llSearch.removeAllViews();
                    llSearchBut.removeAllViews();
                    btnSearchPressed = false;

                    llSort.addView(spSort);
                    llSort.addView(spShowGenre);

                    // removing any genre selection or sorting
                    strSelection = null;
                    strArrayShowGenre = null;
                    orderBy = "bookDate DESC";
                    showAllBooks();
                    spShowGenre.setSelection(0);
                    spSort.setSelection(0);

                }

                break;
            case R.id.btnSearchOK:
                strSelection = "bookName LIKE ? OR bookAuthor LIKE ?";
                strArrayShowGenre = new String[]{"%"+etSearch.getText().toString()+"%", "%"+etSearch.getText().toString()+"%"};
                showAllBooks();
                break;
            case R.id.btnSearchCancel:
                strSelection = null;
                strArrayShowGenre = null;
                showAllBooks();
                break;
        }
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
                intent = new Intent(this, AddEditBook.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case INTENT_CODE_ADD:    // add book activity
                    selectedBook = new Book();
                    selectedBook.getDetailsFromIntent(data);
                    Log.d("myLogs", "Added book:"+selectedBook.toString());
                    addBook(selectedBook);
                    showAllBooks();
                    break;
                case INTENT_CODE_VIEW:    // view book activity
                    showAllBooks();
                    break;
                case INTENT_CODE_EDIT:    // edit book activity
                    selectedBook.getDetailsFromIntent(data);
                    Log.d("myLogs", "Book data in main activity after edit: "+selectedBook.toString());
                    updateBook(selectedBook);
                    showAllBooks();
                    break;
                case INTENT_CODE_DELETE:    // delete book activity
                    deleteBook(selectedBook);
                    showAllBooks();
                    break;
            }
            //dbHelper.close();

        } else if (resultCode == RESULT_CANCELED){
            showAllBooks();
            } else  {
                 Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show();
                 }

    }

    public void addBook(final Book selectedBook){
        Intent intentDBIntentService = new Intent(this, DBIntentService.class);
        intentDBIntentService.putExtra("selectedBook", selectedBook.putDetailsToBundle()).putExtra("task",
                "addBook");
        startService(intentDBIntentService);
    }

    public void updateBook(final Book selectedBook){

        Intent intentDBIntentService = new Intent(this, DBIntentService.class);
        intentDBIntentService.putExtra("selectedBook", selectedBook.putDetailsToBundle()).putExtra("task",
                "updateBook");
        startService(intentDBIntentService);

    }

    public void deleteBook(final Book selectedBook){
        Log.d("myLogs", "thisBook.id: "+selectedBook.id);
        Intent intentDBIntentService = new Intent(this, DBIntentService.class);
        intentDBIntentService.putExtra("selectedBook", selectedBook.putDetailsToBundle()).putExtra("task",
                "deleteBook");
        startService(intentDBIntentService);
    }


    public void showAllBooks(){

        Intent intentDBIntentService = new Intent(this, DBIntentService.class);
        intentDBIntentService.putExtra("task", "showAllBooks").
                              putExtra("strSelection", strSelection).
                              putExtra("strArrayShowGenre", strArrayShowGenre).
                              putExtra("orderBy", orderBy);
        startService(intentDBIntentService);

       /// registering the receiver and getting the message
        mMyBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("MY_RESPONSE");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mMyBroadcastReceiver, intentFilter);
        Log.d("myLogs", "Register receiver");

    }

    public void onResume() {
        super.onResume();
        /// registering the receiver and getting the message
        mMyBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("MY_RESPONSE");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mMyBroadcastReceiver, intentFilter);
        Log.d("myLogs", "Register receiver");
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(mMyBroadcastReceiver);
        Log.d("myLogs", "Unregister receiver");
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            booksList = (ArrayList<Book>) intent.getSerializableExtra("booksList");

            BookAdapter bookAdapter = new BookAdapter(getApplicationContext(), 0, booksList);
            booksListView.setAdapter(bookAdapter);

        }
    }

}

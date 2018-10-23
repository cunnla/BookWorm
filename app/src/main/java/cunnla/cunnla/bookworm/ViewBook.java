package cunnla.cunnla.bookworm;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewBook extends AppCompatActivity implements View.OnClickListener {

    Button btnOK, btnEdit, btnDelete;
    TextView tvDate, tvName, tvAuthor, tvGenre, tvNotes;

    Intent intent;
    final static int INTENT_CODE_ADD = 1;
    final static int INTENT_CODE_VIEW = 2;
    final static int INTENT_CODE_EDIT = 3;
    final static int INTENT_CODE_DELETE = 4;

    final String LOG_TAG = "myLogs";

    public Book selectedBook;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        btnOK = (Button) findViewById(R.id.btnOk);
        btnOK.setOnClickListener(this);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvName = (TextView)findViewById(R.id.tvName);
        tvAuthor = (TextView)findViewById(R.id.tvAuthor);
        tvGenre = (TextView)findViewById(R.id.tvGenre);
        tvNotes = (TextView)findViewById(R.id.tvNotes);

        intent = getIntent();

        selectedBook = new Book();
        selectedBook.getDetailsFromIntent(intent);
        Log.d("myLogs","View: OnCreate: " + selectedBook.toString());

        tvDate.setText(selectedBook.bookDateNice());
        //tvDate.setText(selectedBook.bookDate);
        tvName.setText(selectedBook.bookName);
        tvAuthor.setText(selectedBook.bookAuthor);
        tvGenre.setText(selectedBook.bookGenre);
        tvNotes.setText(selectedBook.bookNotes);

        dbHelper = new DBHelper(this);

    }

    @Override
    public void onClick(View v) {

        Intent intent2;

        switch (v.getId()) {
            case R.id.btnEdit:
                intent2 = new Intent(this, AddEditBook.class);
                selectedBook.putDetailsToIntent(intent2);
                startActivityForResult(intent2, INTENT_CODE_EDIT);
                break;
            case R.id.btnDelete:
                intent2 = new Intent(this, DeleteBook.class);
                startActivityForResult(intent2, INTENT_CODE_DELETE);
                break;
            case R.id.btnOk:
                setResult(RESULT_OK, intent);
                finish();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        if (resultCode == RESULT_OK) {



            switch (requestCode) {
                case INTENT_CODE_EDIT:    // edit book activity
                    selectedBook.getDetailsFromIntent(data);

                    tvDate.setText(selectedBook.bookDateNice());
                    tvName.setText(selectedBook.bookName);
                    tvAuthor.setText(selectedBook.bookAuthor);
                    tvGenre.setText(selectedBook.bookGenre);
                    tvNotes.setText(selectedBook.bookNotes);

                    Log.d("myLogs","View: Edit: " + selectedBook.toString());

                    updateBook(selectedBook);
                    //db = dbHelper.getReadableDatabase();
                    //db.update("bookTable", selectedBook.addBookToCV(), "id = " + selectedBook.id, null);

                    break;
                case INTENT_CODE_DELETE:    // delete book activity

                    deleteBook(selectedBook);
                    //db = dbHelper.getReadableDatabase();
                    //db.delete("bookTable", "id = " + selectedBook.id, null);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
           // dbHelper.close();
        } else if (resultCode == RESULT_CANCELED){}
          else  {
            Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateBook(final Book selectedBook){
        Intent intentDBIntentService = new Intent(this, DBIntentService.class);
        intentDBIntentService.putExtra("selectedBook", selectedBook.putDetailsToBundle()).putExtra("task",
                "updateBook");
        startService(intentDBIntentService);

    }

    public void deleteBook(final Book thisBook){
        Log.d("myLogs", "thisBook.id: "+selectedBook.id);
        Intent intentDBIntentService = new Intent(this, DBIntentService.class);
        intentDBIntentService.putExtra("selectedBook", selectedBook.putDetailsToBundle()).putExtra("task",
                "deleteBook");
        startService(intentDBIntentService);
    }


}

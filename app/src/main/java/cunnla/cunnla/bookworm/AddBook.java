package cunnla.cunnla.bookworm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddBook extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, utilsCallBack {

    Button btnOK, btnCancel;
    EditText etName, etAuthor, etNotes;

    Utils utils; // my own class for my own different util methods

    Spinner spGenre;
    String strGenre;

    TextView tvDate;
    String dateString = null;

    Book selectedBook;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);

        utils = new Utils(this);

        btnOK = (Button) findViewById(R.id.btnOk);
        btnOK.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setText("Touch here to set up date");
        tvDate.setOnClickListener(this);

        etName = (EditText)findViewById(R.id.etName);
        etAuthor = (EditText)findViewById(R.id.etAuthor);
        etNotes = (EditText)findViewById(R.id.etNotes);

        spGenre = (Spinner) findViewById(R.id.spGenre);
        spGenre.setOnItemSelectedListener(this);
        utils.showGenreSpinner(this, spGenre);

        selectedBook = new Book();

    }

    public void updateTextView(String mystr){  //this is for the callback interface
        tvDate.setText(mystr);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.btnOk:
                if (!etName.getText().toString().equalsIgnoreCase("") &&
                        utils.dateString!=null    ) {              // if a name and a date is entered
                    selectedBook.bookDate = dateString;
                    selectedBook.bookName = etName.getText().toString();
                    selectedBook.bookAuthor = etAuthor.getText().toString();
                    selectedBook.bookGenre = strGenre;
                    selectedBook.bookNotes = etNotes.getText().toString();
                    intent = new Intent();
                    selectedBook.putDetailsToIntent(intent);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, "Please enter a valid book date and name", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnCancel:
                intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.tvDate:
                dateString = utils.setDateString(this);
                break;
        }


    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        strGenre = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }



}

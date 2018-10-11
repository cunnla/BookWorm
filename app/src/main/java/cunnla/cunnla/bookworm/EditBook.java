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

public class EditBook extends AppCompatActivity implements View.OnClickListener,  AdapterView.OnItemSelectedListener, utilsCallBack {

    Button btnOK, btnCancel;
    EditText etName, etAuthor, etNotes;

    Utils utils; // my own class for my own different util methods
    Spinner spGenre;
    String strGenre;

    static TextView tvDate;  //static - in order to be able to access it from Utils class

    Intent intent;

    public Book selectedBook;

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
        tvDate.setOnClickListener(this);



        etName = (EditText)findViewById(R.id.etName);
        etAuthor = (EditText)findViewById(R.id.etAuthor);
        etNotes = (EditText)findViewById(R.id.etNotes);

        spGenre = (Spinner) findViewById(R.id.spGenre);
        spGenre.setOnItemSelectedListener(this);
        utils.showGenreSpinner(this, spGenre);

        intent = getIntent();

        selectedBook = new Book();
        selectedBook.getDetailsFromIntent(intent);

        tvDate.setText(selectedBook.bookDateNice());
        etName.setText(selectedBook.bookName);
        etAuthor.setText(selectedBook.bookAuthor);
        spGenre.setSelection(utils.genreList.indexOf(selectedBook.bookGenre));
        etNotes.setText(selectedBook.bookNotes);

        utils.dateString = selectedBook.bookDate;

    }

    public void updateTextView(String mystr){  //this is for the callback interface
         tvDate.setText(mystr);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnOk:
                if (!etName.getText().toString().equalsIgnoreCase("") ) {              // if a name is entered
                    selectedBook.bookDate = utils.dateString;
                    selectedBook.bookName = etName.getText().toString();
                    selectedBook.bookAuthor = etAuthor.getText().toString();
                    selectedBook.bookGenre = strGenre;
                    selectedBook.bookNotes = etNotes.getText().toString();

                    intent = new Intent();
                    selectedBook.putDetailsToIntent(intent);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, "Please enter a valid book name", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnCancel:
                intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.tvDate:
                utils.dateString = utils.setDateString(this);
                break;
        }

    }


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(pos > 0){
            strGenre = parent.getItemAtPosition(pos).toString();
        } else {
            strGenre = selectedBook.bookGenre;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }





}

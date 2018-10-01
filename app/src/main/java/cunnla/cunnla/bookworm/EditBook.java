package cunnla.cunnla.bookworm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditBook extends AppCompatActivity implements View.OnClickListener {

    Button btnOK, btnCancel;
    EditText etDate, etName, etAuthor, etGenre, etNotes;
    Intent intent;

    public Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        btnOK = (Button) findViewById(R.id.btnOk);
        btnOK.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        etDate = (EditText) findViewById(R.id.etDate);
        etName = (EditText)findViewById(R.id.etName);
        etAuthor = (EditText)findViewById(R.id.etAuthor);
        etGenre = (EditText)findViewById(R.id.etGenre);
        etNotes = (EditText)findViewById(R.id.etNotes);

        intent = getIntent();

        selectedBook = new Book();
        selectedBook.getDetailsFromIntent(intent);

        etDate.setText(selectedBook.bookDate);
        etName.setText(selectedBook.bookName);
        etAuthor.setText(selectedBook.bookAuthor);
        etGenre.setText(selectedBook.bookGenre);
        etNotes.setText(selectedBook.bookNotes);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnOk:
                if (!etName.getText().toString().equalsIgnoreCase("")) { // if a name is entered

                    selectedBook.bookDate = etDate.getText().toString();
                    selectedBook.bookName = etName.getText().toString();
                    selectedBook.bookAuthor = etAuthor.getText().toString();
                    selectedBook.bookGenre = etGenre.getText().toString();
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
        }

    }
}

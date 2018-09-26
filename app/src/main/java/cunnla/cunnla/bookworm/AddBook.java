package cunnla.cunnla.bookworm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddBook extends AppCompatActivity implements View.OnClickListener {

    Button btnOK;
    EditText etDate, etName, etAuthor, etGenre, etNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        btnOK = (Button) findViewById(R.id.btnOk);
        btnOK.setOnClickListener(this);

        etDate = (EditText) findViewById(R.id.etDate);
        etName = (EditText)findViewById(R.id.etName);
        etAuthor = (EditText)findViewById(R.id.etAuthor);
        etGenre = (EditText)findViewById(R.id.etGenre);
        etNotes = (EditText)findViewById(R.id.etNotes);

    }

    @Override
    public void onClick(View v) {

        if (!etName.getText().toString().equalsIgnoreCase("")) {  // if a name is entered
            Intent intent = new Intent();
            intent.putExtra("bookDate", etDate.getText().toString());
            intent.putExtra("bookName", etName.getText().toString());
            intent.putExtra("bookAuthor", etAuthor.getText().toString());
            intent.putExtra("bookGenre", etGenre.getText().toString());
            intent.putExtra("bookNotes", etNotes.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Please enter details", Toast.LENGTH_LONG).show();
        }


    }
}

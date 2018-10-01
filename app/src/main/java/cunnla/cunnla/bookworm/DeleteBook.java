package cunnla.cunnla.bookworm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DeleteBook extends AppCompatActivity implements View.OnClickListener {

    Button btnOK, btnCancel;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_book);
        this.setFinishOnTouchOutside(false);

        btnOK = (Button) findViewById(R.id.btnOk);
        btnOK.setOnClickListener(this);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        intent = getIntent();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnOk:
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btnCancel:
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
        }

    }
}

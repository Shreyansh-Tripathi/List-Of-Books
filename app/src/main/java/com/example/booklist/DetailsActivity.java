package com.example.booklist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    EditText name,genre,author;
    Button add;
    MainActivity main;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        name=(EditText)findViewById(R.id.name);
        genre=(EditText)findViewById(R.id.genre);
        author=(EditText)findViewById(R.id.author);
        add=(Button)findViewById(R.id.addbook);
        imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Add Book");
        actionBar.setSubtitle("Enter all Details");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        main=new MainActivity();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() || author.getText().toString().isEmpty()
                        || genre.getText().toString().isEmpty())
                {
                    Toast.makeText(DetailsActivity.this, "Empty Field(s)", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ApplicationClass.list.add(new Book(name.getText().toString().trim(), author.getText()
                                                  .toString().trim(), genre.getText().toString().trim()));
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    name.setText(null);
                    genre.setText(null);
                    author.setText(null);
                    Toast.makeText(DetailsActivity.this, "Added!", Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);
                    DetailsActivity.this.finish();
                }
            }
        });
    }
}

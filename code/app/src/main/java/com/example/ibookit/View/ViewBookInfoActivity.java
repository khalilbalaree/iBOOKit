package com.example.ibookit.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibookit.Functionality.BookStatusHandler;
import com.example.ibookit.Model.Book;
import com.example.ibookit.Model.OwnerShelf;
import com.example.ibookit.R;
import com.google.gson.Gson;


public class ViewBookInfoActivity extends AppCompatActivity {

    private static final String TAG = "ViewBookInfoActivity";
    private TextView mTitle, mAuthor, mIsbn, mStatus, mBorrower, mCategory;
    private Button submit;
    private OwnerShelf ownerShelf = new OwnerShelf();
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        mTitle = findViewById(R.id.bookTitleAdd);
        mAuthor = findViewById(R.id.bookAuthorAdd);
        mIsbn = findViewById(R.id.bookISBNAdd);
        mCategory = findViewById(R.id.bookCategoryAdd);
        mStatus = findViewById(R.id.statusAdd);
        mBorrower = findViewById(R.id.borrowerAdd);


        Intent intent = getIntent();
        String objStr = intent.getStringExtra("book");

        if (objStr != null) {
            Gson gson = new Gson();
            book = gson.fromJson(objStr, Book.class);
        } else {
            Log.d(TAG, "onCreate: no objStr");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mTitle.setText(book.getTitle());
        mAuthor.setText(book.getAuthor());
        mIsbn.setText(book.getIsbn());
        mCategory.setText(book.getCategory());

        BookStatusHandler handler = new BookStatusHandler();
        mStatus.setText(handler.StatusString(book));

        if (book.getCurrentBorrower().length() == 0) {
            mBorrower.setText("No borrower");
            mBorrower.setTextColor(Color.RED);
        } else {
            mBorrower.setText(book.getCurrentBorrower());
        }



    }
}

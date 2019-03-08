package com.example.ibookit.View;

import android.content.DialogInterface;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.ibookit.Functionality.CreateRequestHandler;
import com.example.ibookit.Functionality.SearchForBook;
import com.example.ibookit.Functionality.SearchForUser;
import com.example.ibookit.ListAdapter.BookListAdapter;
import com.example.ibookit.ListAdapter.UserListAdapter;
import com.example.ibookit.Model.Book;
import com.example.ibookit.Model.Request;
import com.example.ibookit.Model.User;
import com.example.ibookit.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EditSearchActivity extends AppCompatActivity {

    private static final String TAG = "EditSearchActivity";
    private ListView searchResultListView;
    private ArrayAdapter<Book> bookArrayAdapter;
    private ArrayAdapter<User> userArrayAdapter;
    private ArrayList<Book> bookResult;
    private ArrayList<User> uerResult;
    private String type, searchValue;
    private SearchView sv;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        searchResultListView = findViewById(R.id.search_result_list);

        intent = getIntent();
        type = intent.getStringExtra("type");
        searchValue = intent.getStringExtra("SearchValue");

        sv = findViewById(R.id.search_bar);
//        sv.setQueryHint(searchValue);

        configure_SearchButtonsAndSearchBar();
        load_resultList();
        ListViewClickHandler();
        setBottomNavigationView();

    }


    private void load_resultList(){
        if (type.equals("SearchUser")) {
            Log.d(TAG, "onCreate: " + searchValue);
            SearchForUser userSearch = new SearchForUser();
            ArrayList<User> searchResult= new ArrayList<>();

            Log.d(TAG, "onCreate: " + searchResult);

            userArrayAdapter = new UserListAdapter(this, R.layout.adapter_user, searchResult);
            searchResultListView.setAdapter(userArrayAdapter);
//            searchResultListView.setClickable(true);  //this will be activated if need to show more info about user
            userSearch.searchByKeyword(searchValue, searchResult, userArrayAdapter);



        } else if (type.equals("SearchCategory")) {

            Log.d(TAG, "onCreate: " + searchValue);
            SearchForBook bookSearch = new SearchForBook();
            ArrayList<Book> searchResult = new ArrayList<>();

            Log.d(TAG, "onCreate: " + searchResult);

            bookArrayAdapter = new BookListAdapter(this, R.layout.adapter_book, searchResult);
            searchResultListView.setAdapter(bookArrayAdapter);
            searchResultListView.setClickable(true);
            bookSearch.searchByCategory(searchValue, searchResult, bookArrayAdapter);


        } else if (type.equals("SearchTitle")) {

            Log.d(TAG, "onCreate: " + searchValue);
            SearchForBook bookSearch = new SearchForBook();
            ArrayList<Book> searchResult = new ArrayList<>();

            Log.d(TAG, "onCreate: " + searchResult);

            bookArrayAdapter = new BookListAdapter(this, R.layout.adapter_book, searchResult);
            searchResultListView.setAdapter(bookArrayAdapter);
            searchResultListView.setClickable(true);
            bookSearch.searchByTitle(searchValue, searchResult, bookArrayAdapter);

        } else {
            Log.d(TAG, "onCreate: Unexpected type: " + type);
        }


    }

    private void ListViewClickHandler () {
        final ListView finalList = searchResultListView;
        if (this.type.equals("SearchCategory" ) || this.type.equals("SearchTitle")) {
            searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Book book = (Book) finalList.getItemAtPosition(position);

                    setDialog(book);
                }
            });
        }
        else if (this.type.equals("SearchUser")){
            searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    User user = (User) finalList.getItemAtPosition(position);

                    Intent resultProfile = new Intent(EditSearchActivity.this, UserProfileActivity.class);
//                    resultProfile.putExtra("type", "showSearchUserResult");
                    Gson gson = new Gson();
                    String userResult = gson.toJson(user);
                    resultProfile.putExtra("UserResult", userResult);
                    resultProfile.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(resultProfile);
                }
            });
        }
    }

    private void configure_SearchButtonsAndSearchBar(){
        Button searchUser = findViewById(R.id.re_search_user);
        Button viewCategory = findViewById(R.id.re_search_category);
        Button searchBook = findViewById(R.id.re_search_book);
        sv = findViewById(R.id.re_search_bar);

        //todo:this works, but not really sure if it is the best solution
        //did this started a intent from this activity to this activity or homeSearchActivity to
        //this activity?
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                intent.putExtra("type", "SearchUser");
//                intent.putExtra("SearchValue", sv.getQuery().toString());
//
//                finish(); //should I do this? If i don't then user will go back to previous search, but this might consume memory?
//                startActivity(intent);
                type = "SearchUser";
                searchValue = sv.getQuery().toString();
                load_resultList();
                sv.clearFocus();


            }
        });
        viewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               setCategoryDialog();

            }
        });
        searchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                intent.putExtra("type", "SearchTitle");
//                intent.putExtra("SearchValue", sv.getQuery().toString());
//
//                finish();
//                startActivity(intent);
                type = "SearchTitle";
                searchValue = sv.getQuery().toString();
                load_resultList();
                sv.clearFocus();
            }
        });

    }





    private void setBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_add:
                        Intent add = new Intent(EditSearchActivity.this, AddBookAsOwnerActivity.class);
                        add.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(add);
                        break;

                    case R.id.action_home:
                        break;

                    case R.id.action_myshelf:
                        Intent myshelf = new Intent(EditSearchActivity.this, MyShelfOwnerActivity.class);
                        myshelf.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(myshelf);
                        break;

                    case R.id.action_profile:
                        Intent profile = new Intent(EditSearchActivity.this, UserProfileActivity.class);
                        profile.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(profile);
                        break;

                    case R.id.action_request:
                        Intent request = new Intent(EditSearchActivity.this, CheckRequestsActivity.class);
                        request.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(request);

                        break;
                }

                return false;
            }
        });
    }



    private void setDialog(final Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Send request to owner?");
        builder.setCancelable(true);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(book);

                CreateRequestHandler createRequest = new CreateRequestHandler();
                createRequest.SendRequestToOwner(request);

                Toast.makeText(EditSearchActivity.this, "send request successful",
                        Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    private void setCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //some of these options will be changed later, this is just for test
        final CharSequence[] options  = {"fine", "fivestar", "KKK", "Westeast", "thrilling"};
        builder.setTitle("Choose a category").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                intent.putExtra("type", "SearchCategory");
//                intent.putExtra("SearchValue", options[which]);
//                finish();
//                startActivity(intent);
                type = "SearchCategory";
                searchValue = options[which].toString();
                load_resultList();
                sv.clearFocus();

            }
        });




        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}

package books.curs.bcamp2007;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import books.curs.bcamp2007.books.curs.services.Book;
import books.curs.bcamp2007.books.curs.services.UserServiceInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends Activity {

  private ListView listView;
  private ArrayAdapter<Book> bookAdapter;
  private ArrayList<Book> books = new ArrayList<>();
  private Button btnLogout;

  private UserServiceInterface mUserIntf;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    listView = (ListView)findViewById(R.id.listView);
    bookAdapter = new ArrayAdapter<Book>(this,android.R.layout.simple_list_item_1,books);
    btnLogout = (Button) findViewById(R.id.btnLogout);

    mUserIntf = AppController.getInstance().getUserServiceInterface();

    AppController.getInstance().getBookServiceInterface().getAllBooks().enqueue(new Callback<List<Book>>() {
      @Override
      public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
        if(response.isSuccessful()) {
          //books.clear();
          books.addAll(response.body());

          bookAdapter.notifyDataSetChanged();
        }
      }

      @Override
      public void onFailure(Call<List<Book>> call, Throwable t) {

      }
    });

    mUserIntf.isLoggedIn().enqueue(new Callback<Boolean>() {
      @Override
      public void onResponse(Call<Boolean> call, Response<Boolean> response) {
        if(!response.isSuccessful() && !response.body().booleanValue()) {
          logoutUser();
        }
      }

      @Override
      public void onFailure(Call<Boolean> call, Throwable t) {
        Toast.makeText(getApplicationContext(),
            "Error in isLoggedIn request:" + t.getMessage(), Toast.LENGTH_LONG).show();

      }
    });


    // Logout button click event
    btnLogout.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        logoutUser();
      }
    });
  }

  /**
   * Logging out the user. Will set isLoggedIn flag to false in shared
   * preferences Clears the user data from sqlite users table
   */
  private void logoutUser() {

    mUserIntf.logout().enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if(response.isSuccessful()) {
          Intent intent = new Intent(MainActivity.this, LoginActivity.class);
          startActivity(intent);
          finish();
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        Toast.makeText(getApplicationContext(),
            "Error in logout request:" + t.getMessage(), Toast.LENGTH_LONG).show();

      }
    });
    // Launching the login activity

  }
}
package books.curs.bcamp2007;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import books.curs.bcamp2007.books.curs.services.Book;
import books.curs.bcamp2007.books.curs.services.UserServiceInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends Activity {

  private static final String TAG = "MainActivity";

  private ListView listView;
  private ArrayAdapter<Book> bookListAdapter;
  private ArrayList<Book> bookList = new ArrayList<>();
  private Button btnLogout;
  private Button btnRefresh;

  private UserServiceInterface mUserIntf;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    listView = (ListView) findViewById(R.id.listView);
    btnLogout = (Button) findViewById(R.id.btnLogout);
    btnRefresh = (Button) findViewById(R.id.btnRefresh);

    bookListAdapter = new BookListAdapter(this, bookList);
    listView.setAdapter(bookListAdapter);

    mUserIntf = AppController.getInstance().getUserServiceInterface();

    mUserIntf.isLoggedIn().enqueue(new Callback<Boolean>() {
      @Override
      public void onResponse(Call<Boolean> call, Response<Boolean> response) {
        if (!response.isSuccessful() && !response.body().booleanValue()) {
          logoutUser();
        } else {
          loadBooks();
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
    btnRefresh.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        loadBooks();
      }
    });
  }


  private void loadBooks() {
    Log.d(TAG,"loadBooks - Before request");
    AppController.getInstance().getBookServiceInterface().getAllBooks().enqueue(new Callback<List<Book>>() {
      @Override
      public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
        if (response.isSuccessful()) {
          bookList.clear();
          bookList.addAll(response.body());
          Log.d(TAG, "Books:" + bookList);
          bookListAdapter.notifyDataSetChanged();
        } else {
          Log.d(TAG, "Err:" + response);
          Toast.makeText(getApplicationContext(),
                  "Error :" + response, Toast.LENGTH_LONG).show();

        }
      }

      @Override
      public void onFailure(Call<List<Book>> call, Throwable t) {
        Log.d(TAG, "Err:" + t.getMessage());
        Toast.makeText(getApplicationContext(),
                "Error in getAlBooks request:" + t.getMessage(), Toast.LENGTH_LONG).show();
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
        if (response.isSuccessful()) {
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

  public static class BookListAdapter extends ArrayAdapter<Book> {
    public BookListAdapter(Context context, List<Book> itemsList) {
      super(context, 0, itemsList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_row, null);
      }

      //convertView.setBackgroundColor(colors[position % colors.length]);

      TextView code = (TextView) convertView.findViewById(R.id.book_author);
      code.setText(getItem(position).getAuthor());

      TextView name = (TextView) convertView.findViewById(R.id.book_title);
      name.setText(getItem(position).getTitle());

      TextView avail = (TextView) convertView.findViewById(R.id.book_avail);
      avail.setText("" + getItem(position).getAvailableCount());
      return convertView;
    }
  }
}
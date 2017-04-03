package books.curs.bcamp2007;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import books.curs.bcamp2007.books.curs.services.User;
import books.curs.bcamp2007.books.curs.services.UserServiceInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
  private static final String TAG = LoginActivity.class.getSimpleName();
  private Button btnLogin;
  private Button btnLinkToRegister;
  private EditText inputEmail;
  private EditText inputPassword;
  private ProgressDialog pDialog;

  private UserServiceInterface mUserIntf;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    inputEmail = (EditText) findViewById(R.id.email);
    inputPassword = (EditText) findViewById(R.id.password);
    btnLogin = (Button) findViewById(R.id.btnLogin);
    btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

    // Progress dialog
    pDialog = new ProgressDialog(this);
    pDialog.setCancelable(false);
    mUserIntf = AppController.getInstance().getUserServiceInterface();

    mUserIntf.isLoggedIn().enqueue(new Callback<Boolean>() {
      @Override
      public void onResponse(Call<Boolean> call, Response<Boolean> response) {
        if(response.isSuccessful()) {
          if(response.body().booleanValue()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
          }
        } else {
          Toast.makeText(getApplicationContext(),
              "isLoggedIn error", Toast.LENGTH_LONG)
              .show();
        }
      }

      @Override
      public void onFailure(Call<Boolean> call, Throwable t) {
        Log.e(TAG,t.getMessage());
        Toast.makeText(getApplicationContext(),
            "isLoggedIn error:" + t.getMessage(), Toast.LENGTH_LONG)
            .show();
      }
    });



    // Login button Click Event
    btnLogin.setOnClickListener(new View.OnClickListener() {

      public void onClick(View view) {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Check for empty data in the form
        if (!email.isEmpty() && !password.isEmpty()) {
          // login user
          checkLogin(email, password);
        } else {
          // Prompt user to enter credentials
          Toast.makeText(getApplicationContext(),
              "Please enter the credentials!", Toast.LENGTH_LONG)
              .show();
        }
      }

    });

    // Link to Register Screen
    btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

      public void onClick(View view) {
        Intent i = new Intent(getApplicationContext(),
            RegisterActivity.class);
        startActivity(i);
        finish();
      }
    });

  }

  /**
   * function to verify login details in mysql db
   * */
  private void checkLogin(final String email, final String password) {
    // Tag used to cancel the request
    String tag_string_req = "req_login";

    pDialog.setMessage("Logging in ...");
    showDialog();
    User user = new User();
    user.setLoginName(email);
    user.setPasswd(password);
    mUserIntf.login(user).enqueue(new Callback<Boolean>() {
      @Override
      public void onResponse(Call<Boolean> call, Response<Boolean> response) {
        hideDialog();
        if(response.isSuccessful()) {
          if(response.body().booleanValue()) {
            Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
            startActivity(intent);
            finish();

          } else {
            Toast.makeText(getApplicationContext(),
                "Login failed", Toast.LENGTH_LONG).show();
          }
        } else {
          Toast.makeText(getApplicationContext(),
              "Error in login request", Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<Boolean> call, Throwable t) {
        hideDialog();
        Toast.makeText(getApplicationContext(),
            "Error in login request:" + t.getMessage(), Toast.LENGTH_LONG).show();
      }
    });
  }

  private void showDialog() {
    if (!pDialog.isShowing())
      pDialog.show();
  }

  private void hideDialog() {
    if (pDialog.isShowing())
      pDialog.dismiss();
  }
}
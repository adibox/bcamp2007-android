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

import java.util.HashMap;
import java.util.Map;

import books.curs.bcamp2007.books.curs.services.User;
import books.curs.bcamp2007.books.curs.services.UserServiceInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends Activity {
  private static final String TAG = RegisterActivity.class.getSimpleName();
  private Button btnRegister;
  private Button btnLinkToLogin;
  private EditText inputFullName;
  private EditText inputEmail;
  private EditText inputPassword;
  private ProgressDialog pDialog;
  private UserServiceInterface mUserIntf;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    inputFullName = (EditText) findViewById(R.id.name);
    inputEmail = (EditText) findViewById(R.id.email);
    inputPassword = (EditText) findViewById(R.id.password);
    btnRegister = (Button) findViewById(R.id.btnRegister);
    btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

    // Progress dialog
    pDialog = new ProgressDialog(this);
    pDialog.setCancelable(false);

    mUserIntf = AppController.getInstance().getUserServiceInterface();

    mUserIntf.isLoggedIn().enqueue(new Callback<Boolean>() {
      @Override
      public void onResponse(Call<Boolean> call, Response<Boolean> response) {
        if (response.isSuccessful()) {
          if (response.body().booleanValue()) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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
        Log.e(TAG, t.getMessage());
        Toast.makeText(getApplicationContext(),
            "isLoggedIn error:" + t.getMessage(), Toast.LENGTH_LONG)
            .show();
      }
    });
    // Register Button Click event
    btnRegister.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        String name = inputFullName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
          registerUser(name, email, password);
        } else {
          Toast.makeText(getApplicationContext(),
              "Please enter your details!", Toast.LENGTH_LONG)
              .show();
        }
      }
    });

    // Link to Login Screen
    btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

      public void onClick(View view) {
        Intent i = new Intent(getApplicationContext(),
            LoginActivity.class);
        startActivity(i);
        finish();
      }
    });

  }

  /**
   * Function to store user in MySQL database will post params(tag, name,
   * email, password) to register url
   */
  private void registerUser(final String name, final String email,
                            final String password) {
    // Tag used to cancel the request
    String tag_string_req = "req_register";

    pDialog.setMessage("Registering ...");
    showDialog();

    User user = new User();
    user.setLoginName(email);
    user.setPasswd(password);
    mUserIntf.register(user).enqueue(new Callback<Boolean>() {
      @Override
      public void onResponse(Call<Boolean> call, Response<Boolean> response) {
        hideDialog();
        if (response.isSuccessful()) {
          if (response.body().booleanValue()) {
            Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

            // Launch login activity
            Intent intent = new Intent(
                RegisterActivity.this,
                LoginActivity.class);
            startActivity(intent);
            finish();
          } else {
            Toast.makeText(getApplicationContext(),
                "Register failed", Toast.LENGTH_LONG).show();
          }
        }
      }

      @Override
      public void onFailure(Call<Boolean> call, Throwable t) {
        Toast.makeText(getApplicationContext(),
            "Register failed:" + t.getMessage(), Toast.LENGTH_LONG).show();
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
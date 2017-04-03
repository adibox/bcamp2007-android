package books.curs.bcamp2007;

import android.app.Application;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import books.curs.bcamp2007.books.curs.services.PersistentCookieStore;
import books.curs.bcamp2007.books.curs.services.UserServiceInterface;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class AppController extends Application {
  public static final String TAG = AppController.class.getSimpleName();

  private UserServiceInterface mUserService;
  private OkHttpClient mOKClient;

  private static AppController mInstance;

  @Override
  public void onCreate() {
    super.onCreate();
    mInstance = this;
  }

  public static synchronized AppController getInstance() {
    return mInstance;
  }

  public UserServiceInterface getUserServiceInterface() {
    if (mUserService == null) {
      // init okhttp 3 logger
      CookieHandler cookieHandler = new CookieManager(
          new PersistentCookieStore(getApplicationContext()), CookiePolicy.ACCEPT_ALL);
      // init okhttp 3 logger
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      // init OkHttpClient
      mOKClient = new OkHttpClient.Builder()
          .cookieJar(new JavaNetCookieJar(cookieHandler))
          .addInterceptor(logging)
          .build();

      Retrofit client = new Retrofit.Builder().baseUrl(AppConfig.URL_LOGIN).client(mOKClient).addConverterFactory(JacksonConverterFactory.create()).build();
      mUserService = client.create(UserServiceInterface.class);
    }

    return mUserService;
  }
}

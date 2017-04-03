package books.curs.bcamp2007.books.curs.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by User on 4/3/2017.
 */

public interface UserServiceInterface {
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @POST("users/login")
  Call<Boolean> login(@Body User pUser);

  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @POST("users/logout")
  Call<Void> logout();

  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @POST("users/register")
  Call<Boolean> register(@Body User pUser);

  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @GET("users")
  Call<Boolean> isLoggedIn();

  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @GET("users/role/{role_name}")
  Call<Boolean> hasRole(@Path("role_name") String pRole);

}

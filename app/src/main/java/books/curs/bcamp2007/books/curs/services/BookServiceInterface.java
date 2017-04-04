package books.curs.bcamp2007.books.curs.services;


import java.util.Collection;
import java.util.List;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookServiceInterface {
  @Headers({

      "Accept: application/json",
      "Content-Type: application/json"
  })
  @GET("books")
  public Call<List<Book>> getAllBooks();

  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @GET("books/{id}")
  public Call<Book> getBookById(@Path("id") Long pBookId);

  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @POST("books")
  public Call<Book> addBook(@Body Book pBook);

  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @PUT("books")
  public Call<Book> updateBook(@Body Book pBook);

  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @DELETE("books/delete")
  public Call<Book> deleteBook(@Query("book_id") Long pBookId);


}

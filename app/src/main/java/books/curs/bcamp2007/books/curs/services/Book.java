package books.curs.bcamp2007.books.curs.services;

/**
 * Created by User on 4/4/2017.
 */

public class Book {
  private String id ;
  private String title ;
  private String author ;
  private int available;
  private int booked;

  public String getId() {
    return id;
  }

  public void setId(String pId) {
    id = pId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String pTitle) {
    title = pTitle;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String pAuthor) {
    author = pAuthor;
  }

  public int getAvailableCount() {
    return available;
  }

  public void setAvailableCount(int pAvailable) {
    available = pAvailable;
  }

  public int getSellCount() {
    return booked;
  }

  public void setSellCount(int pBooked) {
    booked = pBooked;
  }

  public String toString() {
    return author + "/" + title;
  }
}

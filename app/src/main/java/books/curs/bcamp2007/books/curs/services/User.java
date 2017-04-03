package books.curs.bcamp2007.books.curs.services;

import java.util.ArrayList;
import java.util.Collection;

public class User {

  private String mLoginName;
  private String mPasswd;
  private Collection<String> mRoles = new ArrayList<>();

  public String getLoginName() {
    return mLoginName;
  }

  public void setLoginName(String pLoginName) {
    mLoginName = pLoginName;
  }

  public String getPasswd() {
    return mPasswd;
  }

  public void setPasswd(String pPasswd) {
    mPasswd = pPasswd;
  }

  public Collection<String> getRoles() {
    return mRoles;
  }

  public void setRoles(Collection<String> pRoles) {
    mRoles = pRoles;
  }

}

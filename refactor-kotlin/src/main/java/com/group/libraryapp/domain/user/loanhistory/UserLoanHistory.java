package com.group.libraryapp.domain.user.loanhistory;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class UserLoanHistory {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private Long userId;

  private String bookName;

  private boolean isReturn;

  public UserLoanHistory() {

  }

  public UserLoanHistory(Long userId, String bookName, boolean isReturn) {
    this.userId = userId;
    this.bookName = bookName;
    this.isReturn = isReturn;
  }

  public String getBookName() {
    return this.bookName;
  }

  public void doReturn() {
    this.isReturn = true;
  }

}

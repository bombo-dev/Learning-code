package com.group.libraryapp.user.domain;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

public class User {

    private final Long id;

    private String name;

    private Integer age;

    private final List<UserLoanHistory> userLoanHistories = new ArrayList<>();

    private User(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public static User createNewUser(String name, Integer age) {
        return new User(null, name, age);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void loanBook(Book book) {
        this.userLoanHistories.add(new UserLoanHistory(this, book.getName(), false));
    }

    public void returnBook(String bookName) {
        UserLoanHistory targetHistory = this.userLoanHistories.stream()
                .filter(history -> history.getBookName().equals(bookName))
                .findFirst()
                .orElseThrow();
        targetHistory.doReturn();
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

}

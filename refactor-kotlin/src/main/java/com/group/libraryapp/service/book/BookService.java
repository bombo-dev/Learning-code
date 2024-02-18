package com.group.libraryapp.service.book;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.user.adapter.out.persistence.UserJpaEntityRepository;
import com.group.libraryapp.user.domain.User;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.loan.adapter.in.web.dto.BookLoanRequest;
import com.group.libraryapp.book.adapter.in.web.dto.BookRequest;
import com.group.libraryapp.loan.adapter.in.web.dto.BookReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

  private final BookRepository bookRepository;
  private final UserJpaEntityRepository userRepository;
  private final UserLoanHistoryRepository userLoanHistoryRepository;

  public BookService(
      BookRepository bookRepository,
      UserJpaEntityRepository userRepository,
      UserLoanHistoryRepository userLoanHistoryRepository
  ) {
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
    this.userLoanHistoryRepository = userLoanHistoryRepository;
  }

  @Transactional
  public void saveBook(BookRequest request) {
    Book newBook = new Book(request.getName());
    bookRepository.save(newBook);
  }

//  @Transactional
//  public void loanBook(BookLoanRequest request) {
//    Book book = bookRepository.findByName(request.getBookName()).orElseThrow(IllegalArgumentException::new);
//    if (userLoanHistoryRepository.findByBookNameAndIsReturn(request.getBookName(), false) != null) {
//      throw new IllegalArgumentException("진작 대출되어 있는 책입니다");
//    }
//
//    User user = userRepository.findByName(request.getUserName()).orElseThrow(IllegalArgumentException::new);
//    user.loanBook(book);
//  }
//
//  @Transactional
//  public void returnBook(BookReturnRequest request) {
//    User user = userRepository.findByName(request.getUserName()).orElseThrow(IllegalArgumentException::new);
//    user.returnBook(request.getBookName());
//  }

}

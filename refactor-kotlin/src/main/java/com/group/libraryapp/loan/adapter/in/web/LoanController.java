package com.group.libraryapp.loan.adapter.in.web;

import com.group.libraryapp.loan.adapter.in.web.dto.BookLoanRequest;
import com.group.libraryapp.loan.adapter.in.web.dto.BookReturnRequest;
import com.group.libraryapp.service.book.BookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

    private final BookService bookService;

    public LoanController(BookService bookService) {
        this.bookService = bookService;
    }

//    @PostMapping("/book/loan")
//    public void loanBook(@RequestBody BookLoanRequest request) {
//        bookService.loanBook(request);
//    }
//
//    @PutMapping("/book/return")
//    public void returnBook(@RequestBody BookReturnRequest request) {
//        bookService.returnBook(request);
//    }
}

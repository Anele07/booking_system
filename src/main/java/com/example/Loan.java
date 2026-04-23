package com.example;

import java.time.LocalDate;

public class Loan {
    private Book book;
    private Member member;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(Book book, Member member, LocalDate loanDate, LocalDate dueDate){
        this.book = book;
        this.member = member;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    public Book getBook(){
        return book;
    }

    public Member getMemberID(){
        return member;
    }

    public LocalDate getDuedate(){
        return dueDate;
    }

    public LocalDate getLoanDate(){
        return loanDate;
    }

    public boolean isOverdue(LocalDate currDate){
        return currDate.isAfter(dueDate);
    }

    public void markReturned(LocalDate returnDate){
        if (this.returnDate != null) {
            throw new IllegalStateException("This loan has already been returned.");
        }
        if (returnDate.isBefore(loanDate)) {
            throw new IllegalArgumentException("Return date cannot be before the loan date.");
        }
        this.returnDate = returnDate;
    }

    public boolean isReturned(){
        if(this.returnDate == null) return false;
        return true;
    }
}

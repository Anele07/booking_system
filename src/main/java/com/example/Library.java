package com.example;

import java.time.LocalDate;
import java.util.*;

public class Library {
    private Map<String, Book> books;
    private Map<String, Member> members;
    private List<Loan> loans;

    public Library() {
        books = new HashMap<>();
        members = new HashMap<>();
        loans = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public void registerMember(Member member) {
        members.put(member.getMemberID(), member);
    }

    public Book findBookByIsbn(String isbn) {
        return books.get(isbn);
    }

    public Member findMemberById(String memberId) {
        return members.get(memberId);
    }

    public Loan loanBook(String isbn, String memberId, LocalDate loanDate, LocalDate dueDate) {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);

        if (book == null) {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " not found.");
        }

        if (member == null) {
            throw new IllegalArgumentException("Member with ID " + memberId + " not found.");
        }

        Loan loan = new Loan(book, member, loanDate, dueDate);
        loans.add(loan);
        member.addLoan(loan);

        return loan;
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public List<Loan> getOverdueLoans(LocalDate currentDate) {
        List<Loan> overdue = new ArrayList<>();
        for (Loan loan : loans) {
            if (!loan.isReturned() && loan.isOverdue(currentDate)) {
                overdue.add(loan);
            }
        }
        return overdue;
    }
}


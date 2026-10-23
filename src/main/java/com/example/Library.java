package com.example;

import java.time.LocalDate;
import java.util.*;

public class Library {
    private final Map<String, Book> books;
    private final Map<String, Member> members;
    private final List<Loan> loans;

    public Library() {
        books = new HashMap<>();
        members = new HashMap<>();
        loans = new ArrayList<>();
    }

    public void addBook(Book book) {
        if (books.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("A book with ISBN " + book.getIsbn() + " already exists.");
        }
        books.put(book.getIsbn(), book);
    }

    public void registerMember(Member member) {
        if (members.containsKey(member.getMemberID())) {
            throw new IllegalArgumentException("A member with ID " + member.getMemberID() + " already exists.");
        }
        members.put(member.getMemberID(), member);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
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

        if (!isBookAvailable(isbn)) {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " is already on loan.");
        }

        Loan loan = new Loan(book, member, loanDate, dueDate);
        loans.add(loan);
        member.addLoan(loan);

        return loan;
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public List<Loan> getActiveLoans() {
        List<Loan> activeLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (!loan.isReturned()) {
                activeLoans.add(loan);
            }
        }
        return activeLoans;
    }

    public boolean isBookAvailable(String isbn) {
        for (Loan loan : loans) {
            if (!loan.isReturned() && loan.getBook().getIsbn().equals(isbn)) {
                return false;
            }
        }
        return true;
    }

    public Loan returnBook(String isbn, String memberId, LocalDate returnDate) {
        for (Loan loan : loans) {
            if (!loan.isReturned()
                    && loan.getBook().getIsbn().equals(isbn)
                    && loan.getMemberID().getMemberID().equals(memberId)) {
                loan.markReturned(returnDate);
                return loan;
            }
        }
        throw new IllegalArgumentException("No active loan found for ISBN " + isbn + " and member " + memberId + ".");
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


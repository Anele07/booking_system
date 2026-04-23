package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class LoanTest {
    @Test
    public void testLoanConstructorAndGetters() {
        Book book = new Book("Test Book", "Author", "ISBN789", 2020);
        Member member = new Member("Eve", "M005");
        LocalDate loanDate = LocalDate.of(2022, 1, 1);
        LocalDate dueDate = LocalDate.of(2022, 1, 15);

        Loan loan = new Loan(book, member, loanDate, dueDate);

        assertSame(book, loan.getBook());
        assertSame(member, loan.getMemberID());
    }

    @Test
    public void testIsOverdue() {
        Book book = new Book("Test Book", "Author", "ISBN789", 2020);
        Member member = new Member("Frank", "M006");
        LocalDate loanDate = LocalDate.of(2022, 1, 1);
        LocalDate dueDate = LocalDate.of(2022, 1, 10);

        Loan loan = new Loan(book, member, loanDate, dueDate);

        assertTrue(loan.isOverdue(LocalDate.of(2022, 1, 11)));
        assertFalse(loan.isOverdue(LocalDate.of(2022, 1, 9)));
    }

    @Test
    public void testMarkReturnedSuccessfully() {
        Book book = new Book("Return Test", "Author", "ISBN999", 2015);
        Member member = new Member("George", "M007");
        LocalDate loanDate = LocalDate.of(2022, 1, 1);
        LocalDate dueDate = LocalDate.of(2022, 1, 15);
        Loan loan = new Loan(book, member, loanDate, dueDate);

        loan.markReturned(LocalDate.of(2022, 1, 10));

        assertTrue(loan.isReturned());
    }

    @Test
    public void testMarkReturnedThrowsIfAlreadyReturned() {
        Book book = new Book("Already Returned", "Author", "ISBN1234", 2010);
        Member member = new Member("Henry", "M008");
        Loan loan = new Loan(book, member, LocalDate.now(), LocalDate.now().plusDays(10));
        loan.markReturned(LocalDate.now());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            loan.markReturned(LocalDate.now().plusDays(1));
        });

        assertEquals("This loan has already been returned.", exception.getMessage());
    }

    
}

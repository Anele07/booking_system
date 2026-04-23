package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

public class MemberTest {
    @Test
    public void testMemberConstructorAndGetters() {
        Member member = new Member("Alice", "M001");

        assertEquals("Alice", member.getName());
        assertEquals("M001", member.getMemberID());
    }

    @Test
    public void testAddLoanAndLoanHistory() {
        Member member = new Member("Bob", "M002");
        Book book = new Book("Brave New World", "Aldous Huxley", "ISBN456", 1932);
        Loan loan = new Loan(book, member, LocalDate.now(), LocalDate.now().plusDays(14));

        member.addLoan(loan);

        List<Loan> history = member.getLoanHistory();
        assertEquals(1, history.size());
        assertSame(loan, history.get(0));
    }

     @Test
    public void testGetActiveLoans() {
        Member member = new Member("Carol", "M003");
        Book book1 = new Book("Book1", "Author1", "ISBN1", 2000);
        Book book2 = new Book("Book2", "Author2", "ISBN2", 2001);

        Loan loan1 = new Loan(book1, member, LocalDate.now(), LocalDate.now().plusDays(7));
        Loan loan2 = new Loan(book2, member, LocalDate.now(), LocalDate.now().plusDays(7));
        loan2.markReturned(LocalDate.now().plusDays(1));

        member.addLoan(loan1);
        member.addLoan(loan2);

        List<Loan> activeLoans = member.getActiveLoans();
        assertEquals(1, activeLoans.size());
        assertSame(loan1, activeLoans.get(0));
    }
}

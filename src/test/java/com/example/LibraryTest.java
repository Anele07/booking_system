package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

public class LibraryTest {

    @Test
    public void testAddBookAndFindBook() {
        Library library = new Library();
        Book book = new Book("1984", "George Orwell", "ISBN123", 1949);
        library.addBook(book);

        Book found = library.findBookByIsbn("ISBN123");
        assertNotNull(found);
        assertEquals("1984", found.getTitle());
    }

    @Test
    public void testRegisterMemberAndFindMember() {
        Library library = new Library();
        Member member = new Member("Alice", "M001");
        library.registerMember(member);

        Member found = library.findMemberById("M001");
        assertNotNull(found);
        assertEquals("Alice", found.getName());
    }

    @Test
    public void testLoanBookSuccessfully() {
        Library library = new Library();
        Book book = new Book("Dune", "Frank Herbert", "ISBN456", 1965);
        Member member = new Member("Bob", "M002");

        library.addBook(book);
        library.registerMember(member);

        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(14);

        Loan loan = library.loanBook("ISBN456", "M002", loanDate, dueDate);

        assertNotNull(loan);
        assertEquals(book, loan.getBook());
        assertEquals(member, loan.getMemberID());
        assertFalse(loan.isReturned());

        List<Loan> allLoans = library.getAllLoans();
        assertEquals(1, allLoans.size());
    }

    @Test
    public void testLoanBookFailsForInvalidISBN() {
        Library library = new Library();
        Member member = new Member("Carol", "M003");
        library.registerMember(member);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            library.loanBook("INVALID_ISBN", "M003", LocalDate.now(), LocalDate.now().plusDays(14));
        });

        assertEquals("Book with ISBN INVALID_ISBN not found.", ex.getMessage());
    }

    @Test
    public void testLoanBookFailsForInvalidMember() {
        Library library = new Library();
        Book book = new Book("Foundation", "Isaac Asimov", "ISBN789", 1951);
        library.addBook(book);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            library.loanBook("ISBN789", "INVALID_MEMBER", LocalDate.now(), LocalDate.now().plusDays(14));
        });

        assertEquals("Member with ID INVALID_MEMBER not found.", ex.getMessage());
    }

    @Test
    public void testGetOverdueLoans() {
        Library library = new Library();
        Book book = new Book("Neuromancer", "William Gibson", "ISBN987", 1984);
        Member member = new Member("Dave", "M004");

        library.addBook(book);
        library.registerMember(member);

        LocalDate loanDate = LocalDate.now().minusDays(30);
        LocalDate dueDate = LocalDate.now().minusDays(15);

        library.loanBook("ISBN987", "M004", loanDate, dueDate);

        List<Loan> overdue = library.getOverdueLoans(LocalDate.now());

        assertEquals(1, overdue.size());
    }

    @Test
    public void testBookCannotBeLoanedTwiceUntilReturned() {
        Library library = new Library();
        library.addBook(new Book("Dune", "Frank Herbert", "ISBN456", 1965));
        library.registerMember(new Member("Bob", "M002"));
        library.registerMember(new Member("Alice", "M001"));
        LocalDate loanDate = LocalDate.now();

        library.loanBook("ISBN456", "M002", loanDate, loanDate.plusDays(14));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.loanBook("ISBN456", "M001", loanDate, loanDate.plusDays(14)));
        assertEquals("Book with ISBN ISBN456 is already on loan.", exception.getMessage());
    }

    @Test
    public void testReturningBookMakesItAvailableAgain() {
        Library library = new Library();
        library.addBook(new Book("Dune", "Frank Herbert", "ISBN456", 1965));
        library.registerMember(new Member("Bob", "M002"));
        LocalDate loanDate = LocalDate.now();
        Loan loan = library.loanBook("ISBN456", "M002", loanDate, loanDate.plusDays(14));

        Loan returnedLoan = library.returnBook("ISBN456", "M002", loanDate.plusDays(2));

        assertSame(loan, returnedLoan);
        assertTrue(returnedLoan.isReturned());
        assertTrue(library.isBookAvailable("ISBN456"));
        assertTrue(library.getActiveLoans().isEmpty());
    }
}


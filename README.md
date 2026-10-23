# Library Management System

A small desktop application for managing a library's books, members, and loans. It is built with Java Swing and provides a tabbed interface for common circulation tasks.

## Features

- Add books with a title, author, ISBN, and publication year.
- Register members with a name and member ID.
- Loan an available book to a member for 14 days.
- Return a book using its ISBN and the borrowing member's ID.
- Browse the catalog, including each book's availability.
- View members and their active-loan counts.
- Review active and overdue loans.
- Prevent duplicate ISBNs, duplicate member IDs, and simultaneous loans of the same book.

## Requirements

- JDK 17 or newer
- Apache Maven
- A desktop environment capable of displaying Swing windows

## Build and Run

Run these commands from the `booking_system` project directory:

```sh
mvn test
mvn compile
java -cp target/classes com.example.LibraryGUI
```

The first command runs the tests. The second compiles the application, and the third opens the desktop interface.

## Using the Application

1. Add books from the **Add Book** tab and register borrowers from **Register Member**.
2. Use **Loan Book** with a book ISBN and member ID to check out a book.
3. Return it from the same tab using its ISBN and the borrowing member's ID.
4. Check **Catalog**, **Members**, **Active Loans**, and **Overdue Loans** to review current records.

The application reports validation errors and successful actions in the message area at the bottom of the window.

## Project Structure

```text
src/
	main/java/com/example/
		Book.java        Book details
		Member.java      Member details and loan history
		Loan.java        Loan dates and return status
		Library.java     Library operations and loan rules
		LibraryGUI.java  Swing desktop interface
	test/java/com/example/
		*Test.java       JUnit tests
```

## Data Storage

Books, members, and loans are held in memory while the application is running. They are not saved to a file or database, so closing the application clears the current records.
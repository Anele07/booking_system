package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookTest {
    @Test
    void testBookCreation() {
        Book book = new Book("1984", "George Orwell", "ISBN123", 1949);
        assertEquals("1984", book.getTitle());
        assertEquals("George Orwell", book.getAuthor());
        assertEquals("ISBN123", book.getIsbn());
        assertEquals(1949, book.getYear());
    }

    

}

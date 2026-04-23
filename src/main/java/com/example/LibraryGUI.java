package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class LibraryGUI {
    private Library library;

    // GUI components
    private JFrame frame;
    private JTextField bookTitleField, bookAuthorField, bookIsbnField, bookYearField;
    private JTextField memberNameField, memberIdField;
    private JTextField loanIsbnField, loanMemberIdField;

    private JTextArea outputArea;

    public LibraryGUI() {
        library = new Library();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Library System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        // Tabs for different features
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panels for tabs
        tabbedPane.add("Add Book", createAddBookPanel());
        tabbedPane.add("Register Member", createRegisterMemberPanel());
        tabbedPane.add("Loan Book", createLoanBookPanel());
        tabbedPane.add("Overdue Loans", createOverdueLoansPanel());

        frame.add(tabbedPane, BorderLayout.CENTER);

        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel createAddBookPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2));

        bookTitleField = new JTextField();
        bookAuthorField = new JTextField();
        bookIsbnField = new JTextField();
        bookYearField = new JTextField();

        panel.add(new JLabel("Title:"));
        panel.add(bookTitleField);
        panel.add(new JLabel("Author:"));
        panel.add(bookAuthorField);
        panel.add(new JLabel("ISBN:"));
        panel.add(bookIsbnField);
        panel.add(new JLabel("Published Year:"));
        panel.add(bookYearField);

        JButton addButton = new JButton("Add Book");
        panel.add(addButton);

        addButton.addActionListener(e -> {
            String title = bookTitleField.getText().trim();
            String author = bookAuthorField.getText().trim();
            String isbn = bookIsbnField.getText().trim();
            int year;

            try {
                year = Integer.parseInt(bookYearField.getText().trim());
            } catch (NumberFormatException ex) {
                output("Invalid year");
                return;
            }

            Book book = new Book(title, author, isbn, year);
            library.addBook(book);
            output("Book added: " + title);
        });

        return panel;
    }

    private JPanel createRegisterMemberPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));

        memberNameField = new JTextField();
        memberIdField = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(memberNameField);
        panel.add(new JLabel("Member ID:"));
        panel.add(memberIdField);

        JButton registerButton = new JButton("Register Member");
        panel.add(registerButton);

        registerButton.addActionListener(e -> {
            String name = memberNameField.getText().trim();
            String id = memberIdField.getText().trim();

            Member member = new Member(name, id);
            library.registerMember(member);
            output("Member registered: " + name);
        });

        return panel;
    }

    private JPanel createLoanBookPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));

        loanIsbnField = new JTextField();
        loanMemberIdField = new JTextField();

        panel.add(new JLabel("Book ISBN:"));
        panel.add(loanIsbnField);
        panel.add(new JLabel("Member ID:"));
        panel.add(loanMemberIdField);

        JButton loanButton = new JButton("Loan Book");
        panel.add(loanButton);

        loanButton.addActionListener(e -> {
            String isbn = loanIsbnField.getText().trim();
            String memberId = loanMemberIdField.getText().trim();

            try {
                Loan loan = library.loanBook(isbn, memberId, LocalDate.now(), LocalDate.now().plusDays(14));
                output("Loan successful:\n" + loan.getBook().getTitle() + " loaned to " + loan.getMemberID().getName());
            } catch (IllegalArgumentException ex) {
                output("Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createOverdueLoansPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton viewButton = new JButton("View Overdue Loans");
        panel.add(viewButton, BorderLayout.NORTH);

        JTextArea overdueTextArea = new JTextArea(10, 40);
        overdueTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(overdueTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        viewButton.addActionListener(e -> {
            List<Loan> overdueLoans = library.getOverdueLoans(LocalDate.now());
            if (overdueLoans.isEmpty()) {
                overdueTextArea.setText("No overdue loans.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Loan loan : overdueLoans) {
                    sb.append("Book: ").append(loan.getBook().getTitle())
                      .append(" | Borrower: ").append(loan.getMemberID().getName())
                      .append(" | Due: ").append(loan.getDuedate())
                      .append("\n");
                }
                overdueTextArea.setText(sb.toString());
            }
        });

        return panel;
    }

    private void output(String message) {
        outputArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryGUI::new);
    }
}


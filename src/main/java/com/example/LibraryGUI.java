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
    private JTextField returnIsbnField, returnMemberIdField;

    private JTextArea outputArea;
    private JTable catalogTable;
    private JTable membersTable;
    private JTable activeLoansTable;

    public LibraryGUI() {
        library = new Library();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Library System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650);
        frame.setMinimumSize(new Dimension(760, 520));
        frame.setLayout(new BorderLayout());

        // Tabs for different features
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panels for tabs
        tabbedPane.add("Add Book", createAddBookPanel());
        tabbedPane.add("Register Member", createRegisterMemberPanel());
        tabbedPane.add("Loan Book", createLoanBookPanel());
        tabbedPane.add("Active Loans", createActiveLoansPanel());
        tabbedPane.add("Catalog", createCatalogPanel());
        tabbedPane.add("Members", createMembersPanel());
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
            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                output("Enter a title, author, and ISBN.");
                return;
            }

            int year;

            try {
                year = Integer.parseInt(bookYearField.getText().trim());
            } catch (NumberFormatException ex) {
                output("Invalid year");
                return;
            }

            try {
                library.addBook(new Book(title, author, isbn, year));
                output("Book added: " + title);
                bookTitleField.setText("");
                bookAuthorField.setText("");
                bookIsbnField.setText("");
                bookYearField.setText("");
                refreshTables();
            } catch (IllegalArgumentException ex) {
                output("Error: " + ex.getMessage());
            }
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
            if (name.isEmpty() || id.isEmpty()) {
                output("Enter a name and member ID.");
                return;
            }

            try {
                library.registerMember(new Member(name, id));
                output("Member registered: " + name);
                memberNameField.setText("");
                memberIdField.setText("");
                refreshTables();
            } catch (IllegalArgumentException ex) {
                output("Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createLoanBookPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        loanIsbnField = new JTextField();
        loanMemberIdField = new JTextField();

        panel.add(new JLabel("Book ISBN:"));
        panel.add(loanIsbnField);
        panel.add(new JLabel("Member ID:"));
        panel.add(loanMemberIdField);

        JButton loanButton = new JButton("Loan Book");
        panel.add(loanButton);
        panel.add(new JLabel());
        panel.add(new JLabel("Return by ISBN and member ID:"));
        returnIsbnField = new JTextField();
        returnMemberIdField = new JTextField();
        panel.add(new JLabel("Book ISBN:"));
        panel.add(returnIsbnField);
        panel.add(new JLabel("Member ID:"));
        panel.add(returnMemberIdField);
        JButton returnButton = new JButton("Return Book");
        panel.add(returnButton);
        panel.add(new JLabel());

        loanButton.addActionListener(e -> {
            String isbn = loanIsbnField.getText().trim();
            String memberId = loanMemberIdField.getText().trim();

            try {
                Loan loan = library.loanBook(isbn, memberId, LocalDate.now(), LocalDate.now().plusDays(14));
                output("Loan successful:\n" + loan.getBook().getTitle() + " loaned to " + loan.getMemberID().getName());
                loanIsbnField.setText("");
                loanMemberIdField.setText("");
                refreshTables();
            } catch (IllegalArgumentException ex) {
                output("Error: " + ex.getMessage());
            }
        });

        returnButton.addActionListener(e -> {
            String isbn = returnIsbnField.getText().trim();
            String memberId = returnMemberIdField.getText().trim();
            try {
                Loan loan = library.returnBook(isbn, memberId, LocalDate.now());
                output("Returned: " + loan.getBook().getTitle());
                returnIsbnField.setText("");
                returnMemberIdField.setText("");
                refreshTables();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                output("Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createCatalogPanel() {
        catalogTable = createTable(new String[] {"Title", "Author", "ISBN", "Year", "Availability"});
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(catalogTable), BorderLayout.CENTER);
        refreshCatalog();
        return panel;
    }

    private JPanel createMembersPanel() {
        membersTable = createTable(new String[] {"Member ID", "Name", "Active Loans"});
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(membersTable), BorderLayout.CENTER);
        refreshMembers();
        return panel;
    }

    private JTable createTable(String[] columns) {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(26);
        table.setAutoCreateRowSorter(true);
        return table;
    }

    private void refreshTables() {
        refreshCatalog();
        refreshMembers();
        refreshActiveLoans();
    }

    private void refreshCatalog() {
        if (catalogTable == null) return;
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) catalogTable.getModel();
        model.setRowCount(0);
        for (Book book : library.getAllBooks()) {
            model.addRow(new Object[] {book.getTitle(), book.getAuthor(), book.getIsbn(), book.getYear(),
                    library.isBookAvailable(book.getIsbn()) ? "Available" : "On loan"});
        }
    }

    private void refreshMembers() {
        if (membersTable == null) return;
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) membersTable.getModel();
        model.setRowCount(0);
        for (Member member : library.getAllMembers()) {
            model.addRow(new Object[] {member.getMemberID(), member.getName(), member.getActiveLoans().size()});
        }
    }

    private void refreshActiveLoans() {
        if (activeLoansTable == null) return;
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) activeLoansTable.getModel();
        model.setRowCount(0);
        for (Loan loan : library.getActiveLoans()) {
            model.addRow(new Object[] {loan.getBook().getIsbn(), loan.getBook().getTitle(),
                    loan.getMemberID().getMemberID(), loan.getMemberID().getName(), loan.getLoanDate(), loan.getDuedate()});
        }
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

    private JPanel createActiveLoansPanel() {
        activeLoansTable = createTable(new String[] {"ISBN", "Book", "Member ID", "Borrower", "Loan Date", "Due Date"});
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(activeLoansTable), BorderLayout.CENTER);
        refreshActiveLoans();
        return panel;
    }

    private void output(String message) {
        outputArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryGUI::new);
    }
}


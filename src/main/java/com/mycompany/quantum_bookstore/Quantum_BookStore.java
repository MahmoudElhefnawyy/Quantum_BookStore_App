package com.mycompany.quantum_bookstore;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract class Book {
    protected String isbn;
    protected String title;
    protected int year;
    protected double price;

    public Book(String isbn, String title, int year, double price) {
        this.isbn = isbn;
        this.title = title;
        this.year = year;
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public abstract void processPurchase(int quantity, String email, String address) throws BookstoreException;
}

class PaperBook extends Book {
    private int stock;

    public PaperBook(String isbn, String title, int year, double price, int stock) {
        super(isbn, title, year, price);
        this.stock = stock;
    }

    @Override
    public void processPurchase(int quantity, String email, String address) throws BookstoreException {
        if (quantity > stock) {
            throw new BookstoreException("Insufficient stock for " + title);
        }
        if (address == null || address.isEmpty()) {
            throw new BookstoreException("Address required for paper book purchase");
        }
        stock -= quantity;
        ShippingService.sendToShipping(isbn, quantity, address);
    }
}

class EBook extends Book {
    private String fileType;

    public EBook(String isbn, String title, int year, double price, String fileType) {
        super(isbn, title, year, price);
        this.fileType = fileType;
    }

    @Override
    public void processPurchase(int quantity, String email, String address) throws BookstoreException {
        if (email == null || email.isEmpty()) {
            throw new BookstoreException("Email required for ebook purchase");
        }
        MailService.sendEBook(isbn, email);
    }
}

class ShowcaseBook extends Book {
    public ShowcaseBook(String isbn, String title, int year, double price) {
        super(isbn, title, year, price);
    }

    @Override
    public void processPurchase(int quantity, String email, String address) throws BookstoreException {
        throw new BookstoreException("Showcase books are not for sale");
    }
}

class BookstoreException extends Exception {
    public BookstoreException(String message) {
        super(message);
    }
}

class ShippingService {
    public static void sendToShipping(String isbn, int quantity, String address) {
        System.out.println("Shipping " + quantity + " copies of book " + isbn + " to " + address);
    }
}

class MailService {
    public static void sendEBook(String isbn, String email) {
        System.out.println("Sending ebook " + isbn + " to " + email);
    }
}

class Bookstore {
    private Map<String, Book> inventory;

    public Bookstore() {
        this.inventory = new HashMap<>();
    }

    public void addBook(Book book) {
        inventory.put(book.getIsbn(), book);
    }

    public List<Book> removeOutdatedBooks(int yearsThreshold) {
        int currentYear = Year.now().getValue();
        List<Book> outdatedBooks = inventory.values().stream()
                .filter(book -> (currentYear - book.getYear()) > yearsThreshold)
                .collect(Collectors.toList());

        outdatedBooks.forEach(book -> inventory.remove(book.getIsbn()));
        return outdatedBooks;
    }

    public double buyBook(String isbn, int quantity, String email, String address) throws BookstoreException {
        Book book = inventory.get(isbn);
        if (book == null) {
            throw new BookstoreException("Book with ISBN " + isbn + " not found");
        }
        if (quantity <= 0) {
            throw new BookstoreException("Invalid quantity");
        }

        book.processPurchase(quantity, email, address);
        return book.getPrice() * quantity;
    }
}

class Quantum_BookStore {
    public static void main(String[] args) {
        Bookstore bookstore = new Bookstore();

        bookstore.addBook(new PaperBook("1234", "Java Programming", 2020, 29.99, 10));
        bookstore.addBook(new EBook("5678", "Python Guide", 2018, 19.99, "PDF"));
        bookstore.addBook(new ShowcaseBook("9012", "Antique Collection", 2015, 99.99));

        try {
            double cost = bookstore.buyBook("1234", 2, null, "123 Main St");
            System.out.println("Paper book purchase cost: $" + cost);

            cost = bookstore.buyBook("5678", 1, "user@example.com", null);
            System.out.println("EBook purchase cost: $" + cost);

            try {
                bookstore.buyBook("9012", 1, "user@example.com", null);
            } catch (BookstoreException e) {
                System.out.println("Expected error: " + e.getMessage());
            }

            try {
                bookstore.buyBook("9999", 1, "user@example.com", null);
            } catch (BookstoreException e) {
                System.out.println("Expected error: " + e.getMessage());
            }

            List<Book> outdated = bookstore.removeOutdatedBooks(5);
            System.out.println("Removed outdated books: " + outdated.size());

        } catch (BookstoreException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
# Quantum_BookStore_App
<div align="center">
  <img src="Run Preview.png" alt="Quantum Bookstore App Preview" width="1200" height="800"/>
  
  [![Java](https://img.shields.io/badge/Java-17-blue)](https://www.java.com/)
  [![Maven](https://img.shields.io/badge/Maven-3.9.6-orange)](https://maven.apache.org/)
</div>

This is a Java-based application that implements an online bookstore system, known as the Quantum Bookstore. The application supports various types of books, including:
- **PaperBook**: Physical books with stock that can be shipped.
- **EBook**: Digital books with a filetype that can be sent via email.
- **ShowcaseBook**: Demo books not available for sale.

## Features
- Add books to the inventory with details such as ISBN, title, publication year, and price.
- Remove outdated books based on a specified number of years.
- Buy books by providing ISBN, quantity, email, and address.
- Manage stock and handle purchase errors (e.g., insufficient stock).
- Integrate with hypothetical `ShippingService` and `MailService` for delivery.
- Extensible design to support new product types without major modifications.

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/MahmoudElfhnawy/Quantum_BookStore_App.git

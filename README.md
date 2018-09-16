# Ticket Service Application
---

Java implementation of simple ticket service interface that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

Technologies used to develop this application : Spring Boot, Java 1.8 , Maven, Junit (testing purpose).

## How to install it and use it:
---

1. Clone the project:

        git clone https://github.com/Antonio-Redondo/ticket-service.git

2. Run the application:

        -Executing test cases:

        mvn clean install spring-boot:run

        -Skipping test cases:

        mvn clean install spring-boot:run -Dmaven.test.skip=true

## Implementation:
---

Objects created:

    SeatHold: Contain information about the customer and time request in order to hold seats.
    Venue: Contain information about capacity, availability and gives you a matrix of the available seats.
    Status : It is an enum with the different seat status (AVAILABLE, HOLD, RESERVED)
    Locator: Seats number indexes.
    Seat: Contains information about the customer, status and seat number.
    Customer: Name and email of the customer.

Service implementation:

First action in all the methods below is to check the timing for the holding request through expiryCheck();


* numSeatsAvailable()--> It will return a matrix of available seats.

* SeatHold findAndHoldSeats(int numSeats, String customerEmail)--> It will return an SeatHold object
with a list of Seats helds and customer information.

* String reserveSeats(int seatHoldId, String customerEmail) --> It will return an String with the generated ticket number and confirmation.








   

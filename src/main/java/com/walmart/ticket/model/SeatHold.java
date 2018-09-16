package com.walmart.ticket.model;

import java.time.Instant;
import java.util.List;

/**
 * @author Antonio Redondo
 */
public class SeatHold {

    private static int counter;
    private int id;
    private Customer customer;
    private Instant createdAt;
    private List<Seat> seatsHeld;
    static {
        counter = 0;
    }
    public SeatHold() {
        this.id = ++counter;
    }
    /**
     * @return the counter
     */
    public static int getCounter() {
        return counter;
    }
    /**
     * @param counter the counter to set
     */
    public static void setCounter(int counter) {
        SeatHold.counter = counter;
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }
    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    /**
     * @return the createdAt
     */
    public Instant getCreatedAt() {
        return createdAt;
    }
    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    /**
     * @return the seatsHeld
     */
    public List<Seat> getSeatsHeld() {
        return seatsHeld;
    }
    /**
     * @param seatsHeld the seatsHeld to set
     */
    public void setSeatsHeld(List<Seat> seatsHeld) {
        this.seatsHeld = seatsHeld;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SeatHold [").append(id).append(", ");
        if (customer != null)
            builder.append(customer).append(", ");
        if (createdAt != null)
            builder.append(createdAt).append(", ");
        if (seatsHeld != null){
            builder.append(seatsHeld.size() + " seats held: [");
            for(Seat st: seatsHeld){
                builder.append(st.getSeatNo()); builder.append(" ");
            }
            builder.append("]");
        }
        builder.append("]");
        return builder.toString();
    }
}

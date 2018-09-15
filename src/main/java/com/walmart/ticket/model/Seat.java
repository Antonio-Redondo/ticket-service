package com.walmart.ticket.model;


/**
 * @author Antonio Redondo
 */
public class Seat {

    Locator seatNo;
    Customer reservedBy;
    Status Status;

    public Seat(Locator seatNo) {
        super();
        this.seatNo = seatNo;
    }

    public Seat(Locator seatNo, Status Status) {
        this(seatNo);
        this.Status = Status;
    }

    public Locator getSeatNo() {
        return seatNo;
    }
    public void setSeatNo(Locator seatNo) {
        this.seatNo = seatNo;
    }
    public Customer getReservedBy() {
        return reservedBy;
    }
    public void setReservedBy(Customer reservedBy) {
        this.reservedBy = reservedBy;
    }
    public Status getStatus() {
        return Status;
    }
    public void setStatus(Status Status) {
        this.Status = Status;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Seat<");
        if (seatNo != null)
            builder.append(seatNo).append(", ");
        if (reservedBy != null)
            builder.append("reservedBy=").append(reservedBy).append(", ");
        if (Status != null)
            builder.append("Status=").append(Status);
        builder.append(">");
        return builder.toString();
    }

}

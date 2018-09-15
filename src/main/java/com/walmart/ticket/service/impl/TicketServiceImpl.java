package com.walmart.ticket.service.impl;

import com.walmart.ticket.model.*;
import com.walmart.ticket.service.TicketService;
import com.walmart.ticket.utils.BookingHelper;
import java.time.Instant;
import java.util.*;

/**
 * @author Antonio Redondo
 */
public class TicketServiceImpl implements TicketService {

    private int available;
    private Venue v;
    private Map<Integer, SeatHold> seatHoldMapper;
    private long seconds = 100L;

    /**
     * Constructor
     * @param v venue
     */
    public TicketServiceImpl(Venue v) {
        super();
        this.v = v;
        this.available = v.getCapacity();
        seatHoldMapper = new TreeMap<Integer, SeatHold>();
    }

    /**
     * Contructor
     * @param v venue
     * @param secs seconds
     */
    public TicketServiceImpl(Venue v, long secs) {
        this(v);
        this.seconds = secs;
    }

    /**
     * Method in charge of printing seats available
     * @return
     */
    public int numSeatsAvailable() {
        expiryCheck();
        System.out.println(v.prettyPrint());
        return available;
    }

    /**
     * Check expiry time
     */
    private void expiryCheck() {
        for (Iterator<Map.Entry<Integer, SeatHold>> it = seatHoldMapper.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Integer, SeatHold> entry = it.next();
            SeatHold tempSH = entry.getValue();
            long now = Instant.now().getEpochSecond();
            if ((now - tempSH.getCreatedAt().getEpochSecond()) > this.seconds) {
                updateStatus(tempSH.getSeatsHeld(), Status.AVAILABLE);
                this.available += tempSH.getSeatsHeld().size();
                it.remove();
            }
        }
    }

    /**
     * Check expiry time
     * @param seatHoldId id of the seat hold
     */
    private void expiryCheck(int seatHoldId) {
        SeatHold tempSH = seatHoldMapper.get(seatHoldId);
        if(tempSH!=null){
            long now = Instant.now().getEpochSecond();
            if((now - tempSH.getCreatedAt().getEpochSecond())> this.seconds){
                updateStatus(tempSH.getSeatsHeld(), Status.AVAILABLE);
                this.available += tempSH.getSeatsHeld().size();
                seatHoldMapper.remove(seatHoldId);
            }
        }
    }

    /**
     * Method in charge of finding and holding seats
     * @param numSeats the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return SeatHold Object
     */
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        expiryCheck();
        List<Seat> holdingSeats = findGoodSeats(numSeats);
        updateStatus(holdingSeats, Status.HOLD);
        this.available -= holdingSeats.size();
        SeatHold hold = generateSeatHold(holdingSeats, customerEmail);
        if(hold!=null){
            seatHoldMapper.put(hold.getId(), hold);
        }
        return hold;
    }

    /**
     * Update status of the seat
     * @param seats list of seats
     * @param Status status of the seat
     */
    private void updateStatus(List<Seat> seats, Status Status){
        for(Seat st: seats){
            st.setStatus(Status);
        }
    }

    /**
     * Method in charge of creating SeatHold object
     * @param holdingSeats list of seats
     * @param customerEmail customer emailID
     * @return SeatHold Object
     */
    private SeatHold generateSeatHold(List<Seat> holdingSeats, String customerEmail){
        if(holdingSeats.size()<1){
            return null;
        }
        SeatHold hold = new SeatHold();
        hold.setCustomer(new Customer(customerEmail));
        hold.setSeatsHeld(holdingSeats);
        hold.setCreatedAt(Instant.now());

        return hold;
    }

    /**
     * Method in charge of finding proper seats
     * @param numSeats number of seats the customer wishs to book
     * @return list of seats
     */
    private List<Seat> findGoodSeats(int numSeats){
        if(this.available < numSeats){
            System.out.println("There are only " + this.available + " seats available now!");
            return new LinkedList<Seat>();
        }
        Seat[][] seats = v.getSeats();
        List<Seat> storeSeats = new LinkedList<Seat>();
        boolean breakFlag = false;
        for(int i=0; i<v.getRows(); i++){
            if(breakFlag){
                break;
            }
            for(int j=0; j<v.getSeatsPerRow(); j++){
                Seat st = seats[i][j];
                if(Status.AVAILABLE == st.getStatus()){
                    storeSeats.add(st);
                    if(--numSeats == 0){
                        breakFlag = true;
                        break;
                    }
                }
            }
        }
        return storeSeats;
    }

    /**
     *
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold is assigned
     * @return String result failure or successful booking
     */
    public String reserveSeats(int seatHoldId, String customerEmail) {
        expiryCheck(seatHoldId);
        SeatHold seatHold = finder(seatHoldId);
        if(seatHold == null){
            System.out.println("Either seatHoldId is invalid or is expired! ");
            return "Please, try to set a higher expiration time\n";
        }
        boolean isValidCustomer = BookingHelper.validateCustomer(customerEmail, seatHold.getCustomer().getEmail());
        if(!isValidCustomer){
            return "cannot verify customer. Please request reservation with correct customer email.";
        }
        updateStatus(seatHold.getSeatsHeld(), Status.RESERVED);
        String result =  BookingHelper.reservationCode(seatHold);
        seatHoldMapper.remove(seatHoldId);
        return result;
    }

    private SeatHold finder(int seatHoldId){
        return seatHoldMapper.get(seatHoldId);
    }
}

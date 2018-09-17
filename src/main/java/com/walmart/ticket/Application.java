package com.walmart.ticket;

import com.walmart.ticket.model.SeatHold;
import com.walmart.ticket.model.Venue;
import com.walmart.ticket.service.TicketService;
import com.walmart.ticket.service.impl.TicketServiceImpl;
import com.walmart.ticket.utils.BookingHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
		Scanner sc = new Scanner(System.in);
		System.out.println("\n");
		System.out.println("\t\t\t ********************************");
		System.out.println("\t\t\t TicketService System Application");
		System.out.println("\t\t\t ********************************\n");
		int rows = 5;
		int seatsProw = 5;
		System.out.println("\t To book please choose an option from below:"
				+ "( "+ rows + " rows & " + seatsProw + " seats are the default value per venue " + ")" +  "\n");

		boolean loop = true;
		String options = "\t1. start/reset \t 2. Available Seats \t 3. Request for Hold \t 4. Reserve/commit \t 5. Exit.";
		Venue v = new Venue(rows, seatsProw);
		TicketService service = new TicketServiceImpl(v);
		while(loop) {
			System.out.println("\t*************************************************************************************************");
			System.out.println(options);
			System.out.println("\t*************************************************************************************************\n");
			String str = sc.next();
			boolean isvalidInput = BookingHelper.isValidNo(str);
			if(!isvalidInput){
				System.out.println("Select only numbers.");
				continue;
			}
			int input = Integer.parseInt(str);
			switch(input){
				case 1:
					System.out.println("How many rows would you like to book?");
					String xr = sc.next();
					boolean isvalidRow = BookingHelper.isValidNo(xr);
					if(!isvalidRow){
						while(!isvalidRow){
							System.out.println("Invalid row no.");
							System.out.println("Enter valid no:");
							xr = sc.next();
							isvalidRow = BookingHelper.isValidNo(xr);
						}
					}
					rows = Integer.parseInt(xr);
					System.out.println("How many seats per rows would you like to book?");
					String xst = sc.next();
					boolean isvalidStPRw = BookingHelper.isValidNo(xst);
					if(!isvalidStPRw){
						while(!isvalidStPRw){
							System.out.println("Invalid seat no.");
							System.out.println("Enter valid no:");
							xst = sc.next();
							isvalidStPRw = BookingHelper.isValidNo(xst);
						}
					}
					seatsProw = Integer.parseInt(xst);
					System.out.println("Please set an expiration time (seconds) : ");
					int exp;
					try {
						exp = Integer.parseInt(sc.next());
					} catch (NumberFormatException e) {
						exp = -1;
					}
					v = new Venue(rows, seatsProw);
					service = (exp==-1)?new TicketServiceImpl(v):new TicketServiceImpl(v, exp);
					System.gc();
					break;
				case 2:
					System.out.println("\nNo of seats available now: " + service.numSeatsAvailable() + "\n");
					break;
				case 3:
					System.out.println("How many seats would you like to hold?");
					String xs = sc.next();
					boolean isvalidSeat = BookingHelper.isValidNo(xs);
					if(!isvalidSeat){
						while(!isvalidSeat){
							System.out.println("Invalid seat no.");
							System.out.println("Enter valid no:");
							xs = sc.next();
							isvalidSeat = BookingHelper.isValidNo(xs);
						}
					}
					int seats = Integer.parseInt(xs);
					System.out.println("Please, provide the customer email?");
					String email = sc.next();
					boolean isvalid = BookingHelper.isValidEmail(email);
					if(!isvalid){
						while(!isvalid){
							System.out.println("Invalid email pattern.");
							System.out.println("Enter valid email:");
							email = sc.next();
							isvalid = BookingHelper.isValidEmail(email);
						}
					}
					SeatHold hold = service.findAndHoldSeats(seats, email);
					if(hold!=null){
						System.out.println("\n" + seats + " held!\n" + hold);
					}else{
						System.out.println("\nYour request has been failed! Please try again!");
					}
					break;
				case 4:
					System.out.println("SeatHold Id?");
					String x = sc.next();
					boolean isvalidDigit = BookingHelper.isValidNo(x);
					if(!isvalidDigit){
						while(!isvalidDigit){
							System.out.println("Invalid no pattern.");
							System.out.println("Enter valid no:");
							x = sc.next();
							isvalidDigit = BookingHelper.isValidNo(x);
						}
					}
					int id = Integer.parseInt(x);
					System.out.println("Associated with which customer email?");
					String cust = sc.next();
					boolean isvalidEmail = BookingHelper.isValidEmail(cust);
					if(!isvalidEmail){
						while(!isvalidEmail){
							System.out.println("Invalid email pattern.");
							System.out.println("Enter valid email:");
							cust = sc.next();
							isvalidEmail = BookingHelper.isValidEmail(cust);
						}
					}
					System.out.println("\n" + service.reserveSeats(id, cust));
					break;
				case 5:
					loop = false;
					System.out.println("\nThanks!");
					break;
				default:
					System.out.println("Invalid option.");
			}
		}
		sc.close();

	}
}

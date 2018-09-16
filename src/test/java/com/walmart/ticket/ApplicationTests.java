package com.walmart.ticket;

import com.walmart.ticket.model.SeatHold;
import com.walmart.ticket.model.Venue;
import com.walmart.ticket.service.impl.TicketServiceImpl;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * @author Antonio Redondo
 */
@SpringBootTest
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

	private TicketServiceImpl service;
	private int second = 3;
	private int wait = 4000;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		service = new TicketServiceImpl(new Venue(1,1), second);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void numSeatsAvailable() throws InterruptedException{
		int no = service.numSeatsAvailable();
		assert(no == 1);
		service = new TicketServiceImpl(new Venue(2,3), second);
		no = service.numSeatsAvailable();
		assert(no == (2*3));
		service.findAndHoldSeats(2, "antonioredondo@gmail.com");
		no = service.numSeatsAvailable();
		assert(no == ((2*3)-2));
		Thread.sleep(wait);
		System.out.println("After waiting: " + service.numSeatsAvailable());
		assert((2*3) == service.numSeatsAvailable());
		service.findAndHoldSeats((2*3), "antonioredondo@gmail.com");
		assert(0 == service.numSeatsAvailable());
		Thread.sleep(wait);
		assert((2*3) == service.numSeatsAvailable());
		SeatHold sh = service.findAndHoldSeats((2*3), "antonioredondo@gmail.com");
		no = service.numSeatsAvailable();
		service.reserveSeats(sh.getId(), "antonioredondo@gmail.com");
		assert(no == service.numSeatsAvailable());
		System.gc();
		service = new TicketServiceImpl(new Venue(2,3), second);
		sh = service.findAndHoldSeats((2*3), "antonioredondo@gmail.com");
		no = service.numSeatsAvailable();
		Thread.sleep(wait);
		service.reserveSeats(sh.getId(), "antonioredondo@gmail.com");
		assert((no + sh.getSeatsHeld().size()) == service.numSeatsAvailable());
	}

	@Test
	public void findAndHoldSeats() throws InterruptedException{
		SeatHold s1 = service.findAndHoldSeats(1, "test@gmail.com");
		assertNotNull(s1);
		assert(1 == s1.getSeatsHeld().size());
		s1 = service.findAndHoldSeats(1, "test@gmail.com");
		assert(null == s1);
		Thread.sleep(wait);
		s1 = service.findAndHoldSeats(1, "test@gmail.com");
		assertNotNull(s1);
		assert(1 == s1.getSeatsHeld().size());
		Thread.sleep(wait);
		s1 = service.findAndHoldSeats(2, "test@gmail.com");
		assert(null == s1);
	}

	@Test
	public void reserveSeats() throws InterruptedException{
		SeatHold s1 = service.findAndHoldSeats(1, "test@gmail.com");
		String conf = service.reserveSeats(s1.getId(), "test@gmail.com");
		assertNotNull(conf);
		assertTrue(conf.contains("reserved!"));
		conf = service.reserveSeats(0, "test@gmail.com");
		assert(null != conf);
	}

}

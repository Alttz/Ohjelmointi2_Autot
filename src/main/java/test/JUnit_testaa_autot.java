package test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import model.Auto;
import model.dao.Dao;

@TestMethodOrder(OrderAnnotation.class)
class JUnit_testaa_autot {

	@Test
	@Order(1) 
	public void testPoistaKaikkiAutot() {
		Dao dao = new Dao();
		dao.removeAllItems("Nimda");
		ArrayList<Auto> autot = dao.getAllItems();
		assertEquals(0, autot.size());		
	}
	
	@Test
	@Order(2) 
	public void testLisaaAutot() {
		Dao dao = new Dao();
		Auto auto_1 = new Auto("AAA-111", "Honda", "Civic", 2015);
		Auto auto_2 = new Auto("BBB-222", "Fiat", "Scudo", 2016);
		Auto auto_3 = new Auto("CCC-333", "BMW", "330i", 2017);
		Auto auto_4 = new Auto("DDD-444", "Audi", "A4 Quattro 3.2", 2018);
		assertEquals(true, dao.addItem(auto_1)); //tai assertTrue(dao.addItem(auto_1));	
		assertEquals(true, dao.addItem(auto_2));
		assertEquals(true, dao.addItem(auto_3));
		assertEquals(true, dao.addItem(auto_4)); 	
		assertEquals(4, dao.getAllItems().size());		
	}
	
	@Test
	@Order(3) 
	public void testMuutaAuto() {
		//Muutetaan auton AAA-111 rekisterinumero A-1
		Dao dao = new Dao();		
		ArrayList<Auto> autot = dao.getAllItems("AAA-111");		
		autot.get(0).setRekno("A-1");		
		dao.changeItem(autot.get(0));
		autot = dao.getAllItems("A-1");
		assertEquals("A-1", autot.get(0).getRekno());
		assertEquals("Honda", autot.get(0).getMerkki());
		assertEquals("Civic", autot.get(0).getMalli());
		assertEquals(2015, autot.get(0).getVuosi());		
	}
	
	@Test
	@Order(4) 
	public void testPoistaAuto() {
		//Poistetaan se auto, jonka rekisterinumero on A-1
		Dao dao = new Dao();
		ArrayList<Auto> autot = dao.getAllItems("A-1");
		dao.removeItem(autot.get(0).getId());
		assertEquals(0, dao.getAllItems("A-1").size());					
	}
	
	@Test
	@Order(5) 
	public void testHaeOlematonAuto() {
		//Haetaan auto,jonka id on -1
		Dao dao = new Dao();
		assertNull(dao.getItem(-1));
	}
}
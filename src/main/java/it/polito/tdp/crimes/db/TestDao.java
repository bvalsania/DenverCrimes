package it.polito.tdp.crimes.db;

import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.db.EventsDao;


public class TestDao {

	public static void main(String[] args) {
		EventsDao dao = new EventsDao();
		for(Event e : dao.listAllEvents())
			System.out.println(e);
	}

}

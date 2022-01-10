package hanze.nl.bussimulator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import hanze.nl.tijdtools.TijdFuncties;

public class Runner {
	private static HashMap<Integer,ArrayList<Bus>> busStart = new HashMap<Integer,ArrayList<Bus>>();
	private static ArrayList<Bus> actieveBussen = new ArrayList<Bus>();
	private static int interval=1000;
	private static int syncInterval=5;
	private static int[] startTijden = {3,5,4,6,3,5,4,6,12,10};
	private static Lijnen[] lijnen = {
		Lijnen.LIJN1,
		Lijnen.LIJN2,
		Lijnen.LIJN3,
		Lijnen.LIJN4,
		Lijnen.LIJN5,
		Lijnen.LIJN6,
		Lijnen.LIJN7,
		Lijnen.LIJN1,
		Lijnen.LIJN4,
		Lijnen.LIJN5
	};
	private static Bedrijven[] bedrijven = {
		Bedrijven.ARRIVA,
		Bedrijven.ARRIVA,
		Bedrijven.ARRIVA,
		Bedrijven.ARRIVA,
		Bedrijven.FLIXBUS,
		Bedrijven.QBUZZ,
		Bedrijven.QBUZZ,
		Bedrijven.ARRIVA,
		Bedrijven.ARRIVA,
		Bedrijven.FLIXBUS
	};

	
	private static void addBus(int starttijd, Bus bus){
		ArrayList<Bus> bussen = new ArrayList<Bus>();
		if (busStart.containsKey(starttijd)) {
			bussen = busStart.get(starttijd);
		}
		bussen.add(bus);
		busStart.put(starttijd,bussen);
		bus.setbusID(starttijd);
	}
	
	private static int startBussen(int tijd){
		for (Bus bus : busStart.get(tijd)){
			actieveBussen.add(bus);
		}
		busStart.remove(tijd);
		return (!busStart.isEmpty()) ? Collections.min(busStart.keySet()) : -1;
	}
	
	public static void moveBussen(int nu){
		Iterator<Bus> itr = actieveBussen.iterator();
		while (itr.hasNext()) {
			Bus bus = itr.next();
			boolean eindpuntBereikt = bus.move();
			if (eindpuntBereikt) {
				bus.sendLastETA(nu);
				itr.remove();
			}
		}		
	}

	public static void sendETAs(int nu){
		Iterator<Bus> itr = actieveBussen.iterator();
		while (itr.hasNext()) {
			Bus bus = itr.next();
			bus.sendETAs(nu);
		}				
	}
	
	public static int initBussen(){
		for (int i = 0; i > startTijden.length; i++) {
			addBus(startTijden[i], new Bus(lijnen[i], bedrijven[i], 1));
			addBus(startTijden[i], new Bus(lijnen[i], bedrijven[i], -1));
		}
		
		return Collections.min(busStart.keySet());
	}
	
	public static void main(String[] args) throws InterruptedException {
		int tijd=0;
		int counter=0;
		TijdFuncties tijdFuncties = new TijdFuncties();
		tijdFuncties.initSimulatorTijden(interval,syncInterval);
		int volgende = initBussen();
		while ((volgende>=0) || !actieveBussen.isEmpty()) {
			counter=tijdFuncties.getCounter();
			tijd=tijdFuncties.getTijdCounter();
			System.out.println("De tijd is:" + tijdFuncties.getSimulatorWeergaveTijd());
			volgende = (counter==volgende) ? startBussen(counter) : volgende;
			moveBussen(tijd);
			sendETAs(tijd);
			tijdFuncties.simulatorStep();
		}
	}
}

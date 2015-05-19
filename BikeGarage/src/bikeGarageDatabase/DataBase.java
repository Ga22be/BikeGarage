package bikeGarageDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DataBase {
	private ArrayList<HashNode> hashTable;
	private ArrayList<Integer> bikeIDsInUse;
	private HashMap<Integer,Integer> pinCodesInUse;
	private Random rand;
	
	public DataBase() {
		hashTable = new ArrayList<HashNode>(10);
		bikeIDsInUse = new ArrayList<Integer>(500);
		pinCodesInUse = new HashMap<Integer,Integer>(500);
		for (int index = 0 ; index <= 10 ; index++) {
			hashTable.add(index, new HashNode());
		}
		rand = new Random();
	}
	
	public int bikeCount() {
		return bikeIDsInUse.size();
	}
	
	/**
	 * Returns true if the bikeOwner associated with the specified id is allowed to enter the garage.
	 * @param id the specified pin code or bikeID.
	 * @return true if the garage should open. Otherwise, false.
	 */
	public boolean checkIn(int id) {
		Long time = System.currentTimeMillis();
		String s = new String("" +id);
		BikeOwner owner;
		
		switch(s.length()) {
		case 4: //pin
			Integer hashCode = pinCodesInUse.get(s);
			HashNode hNode = hashTable.get(hashCode);
			try {
				owner = hNode.bikeOwners.getOwnerByPin(id);
			}
			catch(NoSuchElementException e) {
				return false;
			}
			owner.setTime(time);
			return true;
		case 5: //bikeID
			try {
				owner = getBikeOwner("" +id);
			}
			catch(NoSuchElementException e) {
				return false;
			}
			owner.setTime(time);
			List<Bike> bikes = owner.getBikes();
			for(Bike b : bikes) {
				if(b.getID() == id) {
					b.setCheckedIn(true);
					return true;
				}
			}
			return false; // setCheckedIn failed...		
		default:
			return false;
		}	
	}
	
	/**
	 * Returns true if the bike specified by the bikeId is allowed to leave the garage.
	 * @param bikeID the specified bikeID.
	 * @return true if garage should open. Otherwise, false.
	 */
	public boolean checkOut(int bikeID) {
		Long currentTime = System.currentTimeMillis();
		BikeOwner owner;
		try {
			owner = getBikeOwner("" +bikeID);
		}
		catch(NoSuchElementException e) {
			return false;
		}
		Long time = owner.getTime();
		if(currentTime - time <= 600000) {
			List<Bike> bikes = owner.getBikes();
			for(Bike b : bikes) {
				if(b.getID() == bikeID) {
					b.setCheckedIn(false);
					return true;
				}
			}
			return false; // setCheckedIn failed...
		}
		return false;
	}
	
	/**
	 * Adds a new BikeOwner to the database.
	 * @param owner the new owner to be added.
	 * @return true if the operation was successful.
	 * @throws IllegalArgumentException if an owner with the same PID already exists.
	 * @throws UnavailableOperationException if the garage capacity has been reached.
	 */
	public boolean addBikeOwner(String name, String pid, String email) throws IllegalArgumentException, UnavailableOperationException {	
		if(bikeIDsInUse.size() >= 500) {
			throw new UnavailableOperationException("The garage is full.");
		}
		int pin = generateUniquePin();
		BikeOwner owner = new BikeOwner(name, pid, email, new String("" +pin));
		int hashCode = getHashCode(owner.getPID());
		HashNode hNode = hashTable.get(hashCode);
		boolean success = hNode.bikeOwners.addBikeOwner(owner);
		if(success) {
			pinCodesInUse.put(pin, hashCode);
		}
		return success;
	}
	
	/**
	 * Removes the specified bikeOwner associated with the specified personID from the database.
	 * Also removes the BikeOwners pin and 
	 * @param personID the specified personID.
	 * @return true if the removal was successful.
	 * @throws NoSuchElementException if no such bikeOwner could be found in the database.
	 * @throws UnavailableOperationException if one of the owners bikes is still checkedIn.
	 */
	public boolean removeBikeOwner(String personID) throws NoSuchElementException, UnavailableOperationException {
		HashNode hNode = hashTable.get(getHashCode(personID));
		BikeOwner owner = getBikeOwner(personID);
		List<Bike> bikes = owner.getBikes();
		for(Bike b : bikes) {
			if(b.isCheckedIn()) {
				throw new UnavailableOperationException("Check out your bikes first.");
			}
		}
		pinCodesInUse.remove(owner.getPin());
		hNode.bikeOwners.removeBikeOwner(personID);
		Iterator<Bike> itr = bikes.iterator();
		while(itr.hasNext()) {
			bikeIDsInUse.remove(new Integer(itr.next().getID()));
		}
		return true;
	}
	
	/** Adds a Bike to the specified bikeOwner.
	 * @param personID the person-id of the bikeOwner.
	 * @return the bikeID associated with the new Bike.
	 * @throws NoSuchElementException if the personID could not be found in the database.
	 * @throws IllegalArgumentException
	 * @throws UnavailableOperationException if the database is full.
	 */
	public int addBike(String personID) throws NoSuchElementException, IllegalArgumentException, UnavailableOperationException {
		if(bikeIDsInUse.size() >= 500) {
			throw new UnavailableOperationException("The garage is full.");
		}
		BikeOwner owner = getBikeOwner(personID);
		int hashcode = getHashCode(personID);
		int bikeID = generateUniqueBikeID(hashcode);
		bikeIDsInUse.add(bikeID);
		List<Bike> bikes = owner.getBikes();
		bikes.add(new Bike(bikeID));
		return bikeID;
	}
	
	/** Removes bikeID from the specified bikeOwner. 
	 * Also removes the bikeOwner if it is his/hers only bike. 
	 * @param personID the specified personID.
	 * @param bikeID the specified bikeID
	 * @return true if the removal was successful.
	 * @throws NoSuchElementException if no such bike could be found registered to the specified bikeOwner.
	 * @throws UnavailableOperationException if the bikeOwners bike is still checkedIn.
	 */
	public boolean removeBike(String personID, String bikeID) throws NoSuchElementException, UnavailableOperationException {
		BikeOwner owner = getBikeOwner(personID);
		List<Bike> bikes = owner.getBikes();
		if(bikes.size() <= 1) {
			removeBikeOwner(personID);
		}
		if(bikes.remove(new Bike(Integer.parseInt(bikeID)))){
			return bikeIDsInUse.remove(new Integer(bikeID)); // assuming this never fails. (always true)
		}
		return false;
	}
	
	/**
	 * Returns a String[] representation of the bikeOwner associated with the specified id.
	 * @param id the specified bikeID or personID.
	 * @return String representation of the bikeOwner, if found.
	 * (return format: [0] == name
	 * 				   [1] == personID		
	 * 				   [2] == email
	 * 				   [3] == pin
	 * 				   [4] == lastActivationTime
	 * 				   [5] == bikeID #1
	 * 				   [6] == chechedIn/chechedOut #1
	 * 				   [7] == bikeID #2
	 * 				   [8] == chechedIn/chechedOut #2)
	 * 				   etc.
	 * @throws NoSuchElementException if no bikeOwner could be associated with the specified id.
	 */
	public String[] getOwner(String id) throws NoSuchElementException {
		HashNode hNode = hashTable.get(getHashCode(id));
		return hNode.bikeOwners.getBikeOwner(id).getInfo();
	}
	
	/**
	 *  Sets the name of the specified BikeOwner.
	 * @param pid the BikeOwner's personID.
	 * @param edit the new name.
	 */
	public void editOwner_name(String pid, String edit) {
		BikeOwner owner = getBikeOwner(pid);
		owner.setName(edit);
	}
	
	/**
	 *  Sets the email of the specified BikeOwner.
	 * @param pid the BikeOwner's personID.
	 * @param edit the new email address.
	 */
	public void editOwner_email(String pid, String edit) {
		BikeOwner owner = getBikeOwner(pid);
		owner.setEmail(edit);
	}
	
	/**
	 *  Sets the personID of the specified BikeOwner.
	 * @param pid the BikeOwner's personID.
	 * @param edit the new personID.
	 */
	public void editOwner_pid(String pid, String edit) {
		BikeOwner owner = getBikeOwner(pid);
		owner.setPid(edit);
	}
	
	/**
	 * Returns the names of all registered BikeOwners in the database.
	 * @return Set<String>
	 */
	public Set<String> getNames() {
		if(bikeIDsInUse.size() == 0) {
			return new TreeSet<String>();
		}
		StringBuilder sb = new StringBuilder();
		for (int index = 0 ; index <= 10 ; index++) {
			String s = hashTable.get(index).getNames();
			if(s != null) {
				sb.append(s);
			}
		}
		List<String> names_list = (List<String>) Arrays.asList(sb.toString().split("\n"));
		TreeMap<String, Integer> names = new TreeMap<String, Integer>();
		for(String str : names_list) {
			names.put(str, null);
		}
		return names.keySet();
	}
	
	/**
	 * Returns a reference to the specified bikeOwner.
	 * @param id the specified bikeID or personID.
	 * @return a reference to the bikeOwner or,
	 * @throws NoSuchElementException if no bikeOwner could be associated with the specified id.
	 */
	private BikeOwner getBikeOwner(String id) throws NoSuchElementException {
		HashNode hNode = hashTable.get(getHashCode(id));
		return hNode.bikeOwners.getBikeOwner(id);
	}
	
	/* Returns a unique bikeID
	 * in the range [10000,99999]. */
	private int generateUniqueBikeID(int hashcode) {
		int bikeID = generateRandomNumber()*10 + hashcode;
		while(bikeIDsInUse.contains(bikeID)) {
			bikeID = generateRandomNumber()*10 + hashcode;
		}
		return bikeID;
	}
	
	private int generateUniquePin() {
		int pin = generateRandomNumber();
		while(pinCodesInUse.containsKey(pin)) {
			pin = generateRandomNumber();
		}
		return pin;
	}
	
	/* Generates an int in the range [1000,9999] */
	private int generateRandomNumber() {
		int d1 = rand.nextInt(10)*1000;
		while(d1 == 0) {
			d1 = rand.nextInt(10)*1000;
		}
		int d2 = rand.nextInt(10)*100;
		int d3 = rand.nextInt(10)*10;
		int d4 = rand.nextInt(10);
		return (d1 + d2 + d3 + d4);
	}
		
	/**
	 * Returns the hashCode associated with the specified identifier.
	 * @param identifier the specified identifier (person- or bike-id).
	 * @return the hashCode of the identifier.
	 * @throws IllegalArgumentException if the identifier does not match 12 or 5 characters.
	 */
	private int getHashCode(String identifier) {
		 if(identifier.length() == 5) {
			 return Integer.parseInt("" + identifier.charAt(identifier.length() - 1));
		 }
		 else if (identifier.length() == 12) {
			 return Integer.parseInt("" + identifier.charAt(7)); // YYYYMMD(D)XXXX
		 }
		 else {
			 throw new IllegalArgumentException("DB: IllegalArgumentException in method getHashCode(String id)");
		 }
	}	
	
	/**
	 * Returns a String representation of all entries in the database.
	 * @return String (possibly empty)
	 * (format: bikeOwner1.toString()\n
	 * 			bikeOwner2.toString()\n
	 * 			etc.)
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int index = 0 ; index <= 10 ; index++) {
			String s = hashTable.get(index).toString();
			if(s != null) {
				sb.append(s);
			}
		}
		return sb.toString();
	}
	
	/* Node class for the HashTable  (ArrayList<HashNode> hashTable) */
	/* Stores the bikeOwners with hashCode hc in the HashNode.bikeOwnerLinkedList at "hashTable.index(hc)" */
	private static class HashNode {
		private BikeOwnerLinkedList bikeOwners;
		
		private HashNode() {
			bikeOwners = new BikeOwnerLinkedList();
		}
		
		public String getNames() {
			return bikeOwners.getNames();
		}
		
		public String toString() {
			return bikeOwners.toString();
		}	
	}
	
}

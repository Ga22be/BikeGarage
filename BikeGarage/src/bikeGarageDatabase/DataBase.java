package bikeGarageDatabase;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

	public class DataBase implements Serializable {
		private ArrayList<LinkedList<BikeOwner>> hashTable;
		private ArrayList<Integer> bikeIDsInUse;
		private HashMap<Integer,Integer> pinCodesInUse;
		private Random rand;
		private String outputFileName;
		
		public DataBase(String name) {
			hashTable = new ArrayList<LinkedList<BikeOwner>>(10);
			bikeIDsInUse = new ArrayList<Integer>(500);
			pinCodesInUse = new HashMap<Integer,Integer>(500);
			for (int index = 0 ; index <= 10 ; index++) {
				hashTable.add(index, new LinkedList<BikeOwner>());
			}
			rand = new Random();
			outputFileName = name;
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
			BikeOwner owner = null;
			try {
				owner = getBikeOwner("" +id);
			}
			catch(NoSuchElementException e) {
				return false;
			}
				
			if(s.length() == 5) {
				List<Bike> bikes = owner.getBikes();
				for(Bike b : bikes) {
					if(b.getID() == id) {
						b.setCheckedIn(true);
						break;
					}
				}
			}
			owner.setTime(time);
			printToFile(outputFileName);
			return true;
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
						printToFile(outputFileName);
						return true;
					}
				}
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
			int hashCode = getHashCode(pid);
			LinkedList<BikeOwner> list = hashTable.get(hashCode);
			if(!list.contains(owner)) {
				if(list.add(owner)) {
					pinCodesInUse.put(pin, hashCode);
					printToFile(outputFileName);
					return true;
				}
				return false;
			}
			throw new IllegalArgumentException("DB: A bike owner with that person-id already exists.");
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
			LinkedList<BikeOwner> list = hashTable.get(getHashCode(personID));
			Iterator<BikeOwner> itr = list.iterator();
			while(itr.hasNext()) {
				BikeOwner owner = itr.next();
				if(owner.getPID().equals(personID)) {
					List<Bike> bikes = owner.getBikes();
					for(Bike b : bikes) {
						if(b.isCheckedIn()) {
							throw new UnavailableOperationException("DB: Check out your bikes first.");
						}
					}
					pinCodesInUse.remove(owner.getPin());
					list.remove(owner);
					for(Bike b : bikes) {
						bikeIDsInUse.remove(new Integer(b.getID()));
					}
					printToFile(outputFileName);
					return true;
				}
			}
			throw new NoSuchElementException();
		}
	
		
		/** Adds a Bike to the specified bikeOwner.
		 * @param personID the person-id of the bikeOwner.
		 * @return the bikeID associated with the new Bike.
		 * @throws NoSuchElementException if the personID could not be found in the database.
		 * @throws UnavailableOperationException if the database is full.
		 */
		public int addBike(String personID) throws NoSuchElementException, UnavailableOperationException {
			if(bikeIDsInUse.size() >= 500) {
				throw new UnavailableOperationException("The garage is full.");
			}
			int hashCode = getHashCode(personID);
			LinkedList<BikeOwner> list = hashTable.get(hashCode);
			Iterator<BikeOwner> itr = list.iterator();
			while(itr.hasNext()) {
				BikeOwner owner = itr.next();
				if(owner.getPID().equals(personID)) {
					int bikeID = generateUniqueBikeID(hashCode);
					bikeIDsInUse.add(bikeID);
					List<Bike> bikes = owner.getBikes();
					bikes.add(new Bike(bikeID));
					printToFile(outputFileName);
					return bikeID;
				}
			}
			throw new NoSuchElementException();
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
			boolean success;
			if(bikes.size() <= 1) {
				success = removeBikeOwner(personID);
				printToFile(outputFileName);
				return success;
			}
			if(bikes.remove(new Bike(Integer.parseInt(bikeID)))){
				success = bikeIDsInUse.remove(new Integer(bikeID));
				printToFile(outputFileName);
				return success; 
			}
			return false;
		}
		
				
		/**
		 * Returns a reference to the specified bikeOwner.
		 * @param id the specified bikeID, personID, or pin code.
		 * @return a reference to the bikeOwner or,
		 * @throws NoSuchElementException if no bikeOwner could be associated with the specified id.
		 */
		private BikeOwner getBikeOwner(String id) {
			int hashCode;
			LinkedList<BikeOwner> list;
			Iterator<BikeOwner> itr;
			switch(id.length()) {
			case 4:
				for (int index = 0 ; index <= 10 ; index++) {
					list = hashTable.get(index);
					itr = list.iterator();
					while(itr.hasNext()) {
						BikeOwner owner = itr.next();
						if(owner.getPin().equals(id)) {
							return owner;
						}
					}
				}
				throw new NoSuchElementException("DB: No bike owner could be associated with the specified pin code.");
			case 5:
				hashCode = getHashCode(id);
				list = hashTable.get(hashCode);
				itr = list.iterator();
				List<Bike> bikes = null;
				while(itr.hasNext()) {
					BikeOwner owner = itr.next();
					bikes = owner.getBikes();
					for(Bike b : bikes) {
						if(("" +b.getID()).equals(id)) {
							return owner;
						}
					}
				}
				throw new NoSuchElementException("DB: No bike owner could be associated with the specified bike-id.");
			case 12:
				hashCode = getHashCode(id);
				list = hashTable.get(hashCode);
				itr = list.iterator();
				while(itr.hasNext()) {
					BikeOwner owner = itr.next();
					if(owner.getPID().equals(id)) {
						return owner;
					}
				}
				throw new NoSuchElementException("DB: No bike owner could be associated with the specified person-id.");
			default:
				throw new IllegalArgumentException("Method getBikeOwner(String):  Illegal string-length.");
			}
		}
		
		
		/**
		 * Returns true if the specified person-id can be associated with a registered bikeOwner. (can be used with bikeID as well.)
		 * @param id the specified person-id (or bikeID).
		 * @return true if a registered bikeOwner could 
		 * 	be associated with the specified person-id, else false.
		 * @throws IllegalArgumentException if the id is not 12 or 5 digits long. 
		 */
		public boolean isOwner(String id) throws IllegalArgumentException {
			try{
				getBikeOwner(id);
				return true;
			}
			catch(NoSuchElementException e) {
				return false;
			}
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
		 * @throws IllegalArgumentException if the specified String "id" has the wrong length.
		 * 	Length must be either 5 or 12 characters {0-9}.
		 */
		public String[] getOwner(String id) throws NoSuchElementException, IllegalArgumentException {
			BikeOwner owner = getBikeOwner(id);
			return owner.getInfo();
		}
				
		
		/**
		 *  Sets the name of the specified BikeOwner.
		 * @param pid the BikeOwner's personID.
		 * @param edit the new name.
		 */
		public void editOwner_name(String pid, String edit) {
			BikeOwner owner = getBikeOwner(pid);
			owner.setName(edit);
			printToFile(outputFileName);
		}
		
		/**
		 *  Sets the email of the specified BikeOwner.
		 * @param pid the BikeOwner's personID.
		 * @param edit the new email address.
		 */
		public void editOwner_email(String pid, String edit) {
			BikeOwner owner = getBikeOwner(pid);
			owner.setEmail(edit);
			printToFile(outputFileName);
		}
		
		/**
		 *  Sets the personID of the specified BikeOwner.
		 * @param pid the BikeOwner's personID.
		 * @param edit the new personID.
		 */
		public void editOwner_pid(String pid, String edit) {
			BikeOwner owner = getBikeOwner(pid);
			owner.setPid(edit);
			printToFile(outputFileName);
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
			LinkedList<BikeOwner> list = null;
			Iterator<BikeOwner> itr = null;
			for (int index = 0 ; index <= 10 ; index++) {
				list = hashTable.get(index);
				itr = list.iterator();
				while(itr.hasNext()) {
					BikeOwner owner = itr.next();
					sb.append(owner.getName());
					sb.append("-");
					sb.append(owner.getPID());
					sb.append("\n");
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
		
		
		/* Returns a unique bikeID
		 * in the range [10000,99999]. */
		private int generateUniqueBikeID(int hashcode) {
			int bikeID = generateRandomNumber()*10 + hashcode;
			while(bikeIDsInUse.contains(bikeID)) {
				bikeID = generateRandomNumber()*10 + hashcode;
			}
			return bikeID;
		}
		
		
		/* generates a unique pin code */
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
		 * Returns a String representation of all entries in the database.
		 * @return String (possibly empty)
		 * (format: bikeOwner1.toString()\n
		 * 			bikeOwner2.toString()\n
		 * 			etc.)
		 */
		public String toString() {
			StringBuilder sb = new StringBuilder();
			LinkedList<BikeOwner> list = null;
			Iterator<BikeOwner> itr = null;
			for (int index = 0 ; index <= 10 ; index++) {
				list = hashTable.get(index);
				itr = list.iterator();
				while(itr.hasNext()) {
					BikeOwner owner = itr.next();
					sb.append(owner.toString());
					sb.append("\n");
				}
			}
			return sb.toString();
		}		
		
		
		/**
		 *  Prints out the Database-object to a file.
		 * @param filename : the file name of the output file.
		 */
		public void printToFile(String fileName) {
			try {
				ObjectOutputStream out = 
						new ObjectOutputStream(new FileOutputStream(fileName));
				out.writeObject(this);
				out.flush();
				out.close();
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		
	}

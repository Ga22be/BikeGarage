package bikeGarageDatabase;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BikeOwnerLinkedList {
	private Node root;
	private Node last;
	
	/**
	 * Creates an empty BikeOwnerLinkedList.
	 */
	public BikeOwnerLinkedList() {
		root = null;
		last = null;
	}
	
	/** Adds a new BikeOwner at the end of the linked list.
	 * @param owner the BikeOwner to be added to the list.
	 * @return true if the operation was successful.
	 * @throws IllegalArgumentException if the specified bikeOwner is already 
	 * registered in the list.
	 */
	public boolean addBikeOwner(BikeOwner owner) throws IllegalArgumentException {
		try {
			getBikeOwner(owner.getPID());
			throw new IllegalArgumentException("DB: The specified social security number already exists.");
		}catch (NoSuchElementException e) {/* Ok, personID not in use.*/}
		// add the new BikeOwner to the list.
		if(root != null) {
			Node n = new Node(owner);
			if(last == null) {
				root.next = n;
				last = n;
			}
			else {
				last.next = n;
				last = n;
			}
		}
		else {
			root = new Node(owner); 
			// last must still be null. Not a cyclic list.
		}
		return true;
	}
	
	/** Returns a reference to the specified BikeOwner, if registered to the list.
	 * @param identifier a person- or bike-id number, 12 and 5 characters long, respectively.
	 * @return a reference to the specified BikeOwner-object or null.
	 * @throws NoSuchElementException if no such bikeOwner was found.
	 * @throws IllegalArgumentException if the specified identifier does not match in length (5 or 12 only).
	 */
	public BikeOwner getBikeOwner(String identifier) throws NoSuchElementException {
		Node temp = root;
		switch(identifier.length()) {
			case 5:
				long bikeID = Long.parseLong(identifier);
				while(temp != null) {
					List<Bike> bikes = temp.element.getBikes();
					Iterator<Bike> itr = bikes.iterator();
					while(itr.hasNext()) {
						Bike bike = itr.next();
						if(bike.getID() == bikeID) {
							temp.element.setSearchedBike(bike); // set quick access to bike.
							return temp.element;
						}
					}
					temp = temp.next;
				}
				throw new NoSuchElementException("DB: No bike could be associated with the specified bikeID.");
			case 12:
				while(temp != null) {
					BikeOwner owner = temp.element;
					if(owner.getPID().equals(identifier)) {
						return owner;
					}
					temp = temp.next;
				}
				throw new NoSuchElementException("DB: No bike owner could be associated with the specified social security number.");
			default: 
				throw new IllegalArgumentException("DB: IllegalArgumentException in method getBikeOwner(), wrong input format.");
		}
	}
	
	/** Removes the specified BikeOwner from the list.
	 * @param pid a person-id.
	 * @throws NoSuchElementException if the BikeOwner could not be found in the list.
	 */
	public void removeBikeOwner(String pid) throws NoSuchElementException, IllegalArgumentException {
		if(pid.length() != 12) {
			throw new IllegalArgumentException("DB: IllegalArgumentException in method removeBikeOwner(), wrong input format.");
		}
		Node prevOwner = root;
		Node currentOwner = root;
		while(currentOwner != null) {
			if(currentOwner.element.getPID().equals(pid)) {
				prevOwner.next = currentOwner.next;
				return;
			}
			prevOwner = currentOwner;
			currentOwner = currentOwner.next;
		}
		throw new NoSuchElementException("DB: No bike owner could be associated with the specified social security number.");
	}
	
	/**
	 * Returns the bikeOwner-object associated with the specified pin code.
	 * @param pin the specified pin code.
	 * @return the associated bikeOwner.
	 * @throws NoSuchElementException if no such bikeOwner was found.
	 */
	public BikeOwner getOwnerByPin(Integer pin) throws NoSuchElementException {
		Node temp = root;
		while(temp != null) {
			if(pin.equals(temp.element.getPin())) {
				return temp.element;
			}
			temp = temp.next;
		}
		throw new NoSuchElementException("DB: No bike owner could be associated with the specified pin code.");
	}
	
	/**
	 * Returns the names and personIDs of all the bikeOwners registered in the list.
	 * @return a String with all names and personIDs. 
	 * Format: "name-personID", i.e., "-" separates name and personID.
	 * The newline character "\n" is used as a separator between bikeOwners.
 	 */
	public String getNames() {
		StringBuilder sb = new StringBuilder();
		Node temp = root;
		while(temp != null) {
			sb.append(temp.element.getName());
			sb.append("-");
			sb.append(temp.element.getPID());
			sb.append("\n");
			temp = temp.next;
		}
		if(sb.length() == 0) {
			return null;
		}
		else {
			return sb.toString();
		}
	}
	
	/**
	 * Returns a string representation of the list.
	 * @return String 
	 * (format: BikeOwner1.toString()\nBikeOwner2.toString()\n ... )
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node temp = root;
		while(temp != null) {
			sb.append(temp.element.toString());
			sb.append("\n");
			temp = temp.next;
		}
		if(sb.length() == 0) {
			return null;
		}
		else {
			return sb.toString();
		}
	}
	
	private static class Node {
		private Node next;
		private BikeOwner element;
		
		private Node(BikeOwner element) {
			this.element = element;
			next = null;
		}
	}
}

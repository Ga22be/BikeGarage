package bikeGarageDatabase;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class BikeOwner {
	 private String name;
	 private String pid;
	 private String email;
	 private long time;
	 private List<Bike> bikes;
	 private Bike currentBike;
	 private String pin;
	 	 
	 /**
	  * Creates a new bikeOwner.
	  * @param name the name of the bikeOwner.
	  * @param pid the personID of the bikeOwner.
	  * @param email the email-address of the bikeOwner.
	  */
	 public BikeOwner(String name, String pid, String email, String pin) {
		 this.name = name;
		 this.pid = pid;
		 this.email = email;
		 this.bikes = new LinkedList<Bike>();
		 currentBike = null;
		 time = 0; // undefined and error if one tries to checkOut a bike before checking in atleast one time before.
		 this.pin = pin;
	 }
	 
	 /**
	  * Returns the personID associated with the bikeOwner.
	  * @return the personID as a String.
	  */
	 public String getPID() {
		 return pid;
	 }
	 
	 public String getPin() {
		 return pin;
	 }
	 
	 public String getName() {
		 return name;
	 }
	 	 
	 
	 /**
	  * Sets the activation time for the bikeOwner. 
	  * @param t the current time.
	  */
	 public void setTime(long t) {
		 time = t;
	 }
	 
	 /**
	  * Returns the activation time for the bikeOwner.
	  * @return
	  */
	 public long getTime() {
		 return time;
	 }
	 
	 /**
	  * Returns the list of bikes owned by the bikeOwner.
	  * @return the list of bikes. 
	  */
	 public List<Bike> getBikes() {
		 return bikes;
	 }
	 
	 /**
	  * Set quick access to the specified bike.
	  * @param bike the specified bike.
	  */
	 public void setSearchedBike(Bike bike) {
		 currentBike = bike;
	 }
	
	 /**
	  * Get the quick access bike.
	  * @return the Bike-object referenced by the quick access or null.
	  */
	 public Bike getSearchedBike() {
		 Bike b = currentBike;
		 currentBike = null;
		 return b;
	 }

	/**
	 * Sets the name of the BikeOwner.
	 * @param name the specified name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the email of the BikeOwner.
	 * @param email the specified email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Sets the personID of the BikeOwner.
	 * @param pid the specified personID.
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}

	public String[] getInfo() {
		Long currentTime = System.currentTimeMillis();
		Long t = currentTime - time;
		String status;
		if(t <= 600000) { // 10 min
			status = new String("Aktiverad");
		}
		else {
			status = new String("Avaktiverad");
		}
		String[] ownerInfo = new String[5 + bikes.size()*2];
		ownerInfo[0] = name;
		ownerInfo[1] = pid;
		ownerInfo[2] = email;
		ownerInfo[3] = pin;
		ownerInfo[4] = status;
		Iterator<Bike> itr = bikes.iterator();
		for(int i = 5 ; i < 5 + bikes.size()*2 ; i = i + 2) {
			Bike b = itr.next();
			ownerInfo[i] = "" +b.getID();
			if(b.isCheckedIn()) {
				ownerInfo[i+1] = "Incheckad";
			}
			else {
				ownerInfo[i+1] = "Utcheckad";
			}
		}
		return ownerInfo;
	}
	
	/**
	  * Returns a String representation of the bikeOwner object.
	  * @return String (format: name +" " +pid +" " +email +" " +pin)
	  */
	 @Override
	 public String toString() {
		 StringBuilder sb = new StringBuilder();
		 sb.append(name +" " +pid +" " +email +" " +pin +" " +time);
		 Iterator<Bike> itr = bikes.iterator();
		 while(itr.hasNext()) {
			 Bike bike = itr.next();
			 sb.append(" " + bike.getID());
		 }
		 return sb.toString();
	 }
	 
}
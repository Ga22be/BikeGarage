package bikeGarageDatabase;

import java.io.Serializable;

public class Bike implements Serializable {
	private int bikeID;
	private boolean checkedIn;
	
	public Bike(int bikeID) {
		this.bikeID = bikeID;
		checkedIn = false;
	}
	
	public int getID() {
		return bikeID;
	}
	
	public boolean isCheckedIn() {
		return checkedIn;
	}
	
	public void setCheckedIn(boolean b) {
		checkedIn = b;
	}
	
	@Override
	public boolean equals(Object obj) {
		Bike b = (Bike) obj;
		return bikeID == b.getID();
	}
	
	@Override
	public String toString() {
		return new String("" +bikeID);
	}
}

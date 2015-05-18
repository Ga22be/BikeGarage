package bikeGarageDatabase;
import java.util.Set;


public class Main {
	public static void main(String[] args) throws IllegalArgumentException, UnavailableOperationException {
		DataBase db = new DataBase();
		db.addBikeOwner("cadam ohlsson", "111111111111", "adam@hotmail.com");
		db.addBikeOwner("bdam ohlsson", "222222222222", "adam@hotmail.com");
		db.addBikeOwner("adam ohlsson", "333333333333", "adam@hotmail.com");
		int b = db.addBike("111111111111");
		int c = db.addBike("111111111111");
		db.checkIn(b);
		db.checkIn(c);
		db.checkOut(b);
		Set<String> names = db.getNames();
		for(String str : names) {
			System.out.println(str);	
		}
		System.out.println("#: " +db.bikeCount());
		System.out.println(db.toString());
		int d = db.addBike("333333333333");
		db.removeBikeOwner("222222222222");
		db.editOwner_name("111111111111", "Adam Ohlsson");
		System.out.println("after remove and edit");
		db.removeBike("333333333333",new String("" +d));
		System.out.println(db.toString());
		System.out.println("#: " +db.bikeCount());
		String[] ownerInfo = db.getOwner("111111111111");
		for(String str : ownerInfo) {
			System.out.println(str);
		}
		
	}
}

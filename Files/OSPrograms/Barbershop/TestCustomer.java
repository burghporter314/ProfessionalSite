
public class TestCustomer {

	public static void main(String[] args) {
		
		BarberShop shop = new BarberShop(10);
		
		for(int i = 1; i <= 3; i++) {
			(new Barber(i, shop)).start();
		}
		
		for(int i = 1; i <= 10; i++) {
			(new Customer(i, shop)).start();
		}
		
	}

}


public class Customer extends Thread {

	private BarberShop barberShop;
	int id;
	
	public Customer(int i, BarberShop bs) {
		super("Customer" + i);
		id = i-1;
		barberShop = bs;
	}
	
	public void run() {

		while(true) {
			
			barberShop.addCustomer(id);
			try
			{
				sleep(3000);
			} catch(InterruptedException ie) {}
			
		}

	}
	
}

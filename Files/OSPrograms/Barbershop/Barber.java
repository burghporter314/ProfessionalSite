
public class Barber extends Thread{
	
	private BarberShop barberShop;
	private int id;
	
	public Barber(int i, BarberShop shop) {
		super("barber" + i);
		barberShop = shop;
		id=i-1;
	}
	
	public void run() {
		while(true) {
			barberShop.addBarber(id);
			try {
				sleep(3000);
			} catch(InterruptedException ie) {
				
			}
		}
	}
}

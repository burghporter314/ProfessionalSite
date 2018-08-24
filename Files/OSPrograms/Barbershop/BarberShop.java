/**
 * Dylan Porter
 * Program 1
 * Operating Systems
 * Dr. Drozdek
 * 
 * Collaborated with Brandon Messineo for ideas and code implementation
 */

class BarberShop {
	
	private String[] barberArray, sofaArray, standingArray;
	private int barberPos = 0, sofaPos = 0, standingPos = 0;
	private int barberPosAfter = 0, sofaPosAfter = 0, standingPosAfter = 0;
	private int numBarber = 0, numSofa = 0, numStanding = 0;
	
	private int[] state;
	
	public BarberShop(int numCustomers) {
		
		barberArray = new String[3];
		sofaArray = new String[4];
		standingArray = new String[13];
		state = new int[numCustomers+1];
		
		for(int i = 0; i < 3; i++) {barberArray[i] = "O";}
		for(int i = 0; i < 4; i++) {sofaArray[i] = "O";}
		for(int i = 0; i < 13; i++) {standingArray[i] = "O";}
		for(int i = 0; i < numCustomers; i++) {state[i] = 3;}
		
	}
	
	public synchronized void addBarber(int id) {
		
		while(id > state.length || state.length == 0) {
			try {
				System.out.println("No customers, so " + Thread.currentThread().getName() + " goes to sleep.");
				wait();
			} catch (InterruptedException ie) {
				
			}
		}
		
		System.out.println(Thread.currentThread().getName() + " opens station " + barberPos);
		barberArray[barberPos] = "O";
		
		barberPos = (barberPos + 1) % barberArray.length;
		
		if(numBarber != 0)
			numBarber--;
		else
			notifyAll();
		
	}
	
	public synchronized void addCustomer(int id) {
		
		while (barberArray.length == 0) {
			try {
				System.out.println("No barbers for " + Thread.currentThread().getName() + ", so they go to sleep.");
				wait();
			} catch (InterruptedException ie) {}
		}
		
		if(state[id] == 3)
			System.out.println(Thread.currentThread().getName() + " enters the barber shop.");
		else
			System.out.println(Thread.currentThread().getName() + " sees if barber is available. ");
		
		if((numBarber == barberArray.length && state[id] > 0)
				|| (numSofa > 0 && state[id] > 1)) 
		closeSofaSeat(id);
		else {
			if(state[id] < 3) {
				if(Thread.currentThread().getName().equals(sofaArray[sofaPos])) {
					openSofaSeat();
					barberArray[barberPosAfter] = Thread.currentThread().getName();
					state[id] = 0;
					System.out.println(Thread.currentThread().getName() + " gets a haircut at seat " + barberPosAfter);
					numBarber++;
					barberPosAfter = (barberPosAfter + 1) % barberArray.length;
				}
				
				else if(state[id] != 1)
					openSofaSeat();
			} else {
				barberArray[barberPosAfter] = Thread.currentThread().getName();
				state[id] = 0;
				System.out.println(Thread.currentThread().getName() + " gets a haircut at seat " + barberPosAfter);
				numBarber++;
				barberPosAfter = (barberPosAfter + 1) % barberArray.length;
			}
		}
		
		if(numBarber == barberArray.length-1) {notifyAll();}
		if(state[id] == 0) {
			System.out.println(Thread.currentThread().getName() + " has finished their haircut... making payment.");
			System.out.println(Thread.currentThread().getName() + " is waiting on their receipt... ");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) { }
			System.out.println(Thread.currentThread().getName() + " has collected their receipt and is now leaving");
			state[id] = 3;
		}

	}
	
	public synchronized void useCashier (int id) {
		
		if(numBarber != barberArray.length-1) {
			
		}
		state[id] = 3;
	}
	
	private void openSofaSeat() {
		sofaArray[sofaPos] = "O";
		sofaPos = (sofaPos + 1) % sofaArray.length;
		if(numSofa != 0) {numSofa--;}
	}
	
	private void closeSofaSeat (int id) {
		
		if((numSofa == sofaArray.length && state[id] > 0) ||
				(numStanding > 0 && state[id] > 2))
			closeStandingSpace(id);
		else {
			if(state[id] < 3 && state[id] > 1) {
				if(Thread.currentThread().getName().equals(standingArray[standingPos])) {
					openStandingSpace();
					System.out.println(Thread.currentThread().getName() + " sits on sofa the sofa on seat " + sofaPosAfter);
					state[id] = 1;
					sofaArray[sofaPosAfter] = Thread.currentThread().getName();
					sofaPosAfter = (sofaPosAfter + 1) % sofaArray.length;
					numSofa++;
				} else if (state[id] != 1) {
					openStandingSpace();
				}
			} else if (state[id] != 1) {
				System.out.println(Thread.currentThread().getName() + " sits on sofa the sofa on seat " + sofaPosAfter);
				state[id] = 1;
				sofaArray[sofaPosAfter] = Thread.currentThread().getName();
				sofaPosAfter = (sofaPosAfter + 1) % sofaArray.length;
				numSofa++;
			}
		}
	}
	
	private void openStandingSpace() {
		standingArray[standingPos] = "0";
		standingPos = (standingPos + 1) % standingArray.length;
		if(numStanding != 0) {numStanding--;}
	}
	
	private void closeStandingSpace (int id) {
		while(numStanding == standingArray.length && state[id] > 2) {
			try {
				System.out.println("No room for standing... " + Thread.currentThread().getName() + " leaves the shop.");
				wait();
			} catch (InterruptedException ie) {}
		}
		
		if(state[id] == 3) {
			System.out.println(Thread.currentThread().getName() + " goes to the standing room at spot " + standingPosAfter);
			state[id] = 2;
			standingArray[standingPosAfter] = Thread.currentThread().getName();
			standingPosAfter = (standingPosAfter + 1) % standingArray.length;
			numStanding++;
		}
	}
}

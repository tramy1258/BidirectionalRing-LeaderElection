package ringleader;

public class Test {
	public static void main (String[] args) {
		
		int nb;
		boolean red = false;
		try {
			nb = Integer.parseInt(args[0]);
			if (args.length > 1) {
				red = true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {			
			return;
		}
		
		System.out.println("Model of "+nb+"-process-ring");
		Ring ring = new Ring();
		if (nb <= 3) {
			ring.generate_ring(nb, 450*nb, red);
		} else {
			ring.generate_ring(nb, 1000, red);
		}

	}
}

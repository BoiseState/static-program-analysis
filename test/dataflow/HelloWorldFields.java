package dataflow;

public class HelloWorldFields {
	private static int x;
	private static int y;

	public static void main(String[] args) {
		x = x + 1;
		if(x < y) {
			x = x * 2;
			y = y - 1;
		}
		//System.out.println("x " + x);
		x = y - 2;
	}

}

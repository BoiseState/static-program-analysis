package context;

public class InterTest01 {
	private static int x;
	private static int y;

	public static void main(String[] args) {
		x = 0;
		y = 0;
		y = m1(3);
		x = m2();

	}
	
	public static int m1(int z) {
		x = x + 1;
		if( x< z) {
			x = x *2;
			y = y -1;
		}
		x = y -2;
		return x;
	}
	
	public static int m2() {
		 x = x -1;
		 m3();
		 x = y -2;
		 return x;
	}
	
	public static void m3() {
		while(x != y) {
			y = x - y;
		}
	}

}

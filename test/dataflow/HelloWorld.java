package dataflow;

public class HelloWorld {

	public int  main(int x, int y) {
		x = x + 1;
		if(x < y) {
			x = x *2;
		}
		//System.out.println("x " + x);
		x = x - 2;
		return x;

	}

}

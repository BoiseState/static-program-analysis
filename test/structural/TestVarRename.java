package structural;
public class TestVarRename {
	
	public int a(int a, int b) {
		while (b != 0) {
			if(a > b) {
				a = a - b;
			} else if (a==b){
				b = b - a;
			} else {
				b = 0;
			}
		}
		return a;
	}
}

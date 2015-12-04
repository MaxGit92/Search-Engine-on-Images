package main;

public class Test {

	public class Class1{
		public int[] tab;
		public Class1(){
			tab = new int[10];
		}
		public int[] getTab(){
			return tab;
		}
	}
	
	
	public static void main(String[] args) {
		Test t = new Test();
		Test.Class1 c = t.new Class1();
		for(int i=0; i<10; i++){
			System.out.print(c.getTab()[i]);
		}
		int[] tab = c.getTab();
		tab[5] = 100;
		System.out.println("");
		for(int i=0; i<10; i++){
			System.out.print(c.getTab()[i]);
		}
	}

}

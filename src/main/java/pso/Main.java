package pso;

public class Main {
	public static void main(String[] args) {
		if(args.length != 2){
			System.out.println("Musisz podac 2 argumenty!");
			return;
		}
		try {
			String function = new String();
			String algorithm = new String();
			function = args[0];
			algorithm = args[1];
			
			String option = new String();
		
			if(function.equalsIgnoreCase("rosenbrock")){ 
				if(algorithm.equalsIgnoreCase("pso"))
				{
					System.out.println("Optymalizacja funckji Rosenbrocka algorytmem roju czastek");
					option = "rPSO";
					new PSOAlgorithms().execute("rPSO");
				}
				else if(algorithm.equalsIgnoreCase("de"))
				{
					System.out.println("Optymalizacja funckji Rosenbrocka algorytmem DE");
					option = "rDE";
				}
				else{
					System.out.println("Nie potrafie uzyc takiego algorytmu!");
					return;
				}	
			}
			else if(function.equalsIgnoreCase("easom")){
				if(algorithm.equalsIgnoreCase("pso"))
				{
					System.out.println("Optymalizacja funckji Easom'a algorytmem roju czastek");
					option = "ePSO";
					new PSOAlgorithms().execute(option);
				}
				else if(algorithm.equalsIgnoreCase("de"))
				{
					System.out.println("Optymalizacja funckji Easom'a algorytmem DE");
					option = "eDE";
				}
				else{
					System.out.println("Nie potrafie uzyc takiego algorytmu!");
					return;
				}	
			}
			else{
				System.out.println("Nie potrafie zoptymalizowac takiej funckji!");
				return;
			}			
		}
			catch(Exception e) {}
	}

}

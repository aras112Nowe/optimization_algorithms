package pso;

import java.util.Random;

import pso.Vectors;

public class Particle {
	private Vectors position = new Vectors(0,0);
	private Vectors velocity = new Vectors(0,0);
	public double fitness;
	private Random randomGenerator;
	
	public void generateParticle() { //generuje losowa predkosc i polozenie czasstki
		this.position.setX(randomGenerator.nextDouble() * (100000 - (-100000)) -100000);
		this.position.setY(randomGenerator.nextDouble() * (100000 - (-100000)) -100000);
		
		this.velocity.setX(randomGenerator.nextDouble() * (100000 - (-100000)) -100000);
		this.velocity.setY(randomGenerator.nextDouble() * (100000 - (-100000)) -100000);
	}
	
	public Particle() {
		this.randomGenerator = new Random();
	}
	
	public void setVelocity(Vectors velocity) {
		this.velocity = velocity;
	}
	
	public Vectors getVelocity() {
		return velocity;
	}
	
	public void setPosition(Vectors position) {
		this.position = position;
	}
	
	public Vectors getPosition() {
		return position;
	}
	
	public void getFitness(String option) {
		double x = this.position.getX();
		double y = this.position.getY();
		
		if (option.equals("rPSO")) {
			//funkcja Rosenbrocka
			fitness = (Math.pow((1-x),2)) + (100*(Math.pow((y-Math.pow(x,2)),2)));
		}else if (option.equals("ePSO")) {
			//funkcja Easom'a
			fitness = -Math.cos(x)*Math.cos(y)*Math.exp(-(Math.pow((x - Math.PI), 2) + Math.pow(y - Math.PI, 2)) );
		}
	}
}

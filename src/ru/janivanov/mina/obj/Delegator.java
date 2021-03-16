package ru.janivanov.mina.obj;

public class Delegator {
	
	private double sum;
	private String name;
	
	public Delegator(String name, double sum) {
		this.name = name;
		this.sum = sum;
	}
	
	public double getSum() {
		return this.sum;
	}
	
	public String getName() {
		return this.name;
	}

}

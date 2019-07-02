package com.cs.aman.ordermatching.entity;

import java.math.BigInteger;

import org.springframework.lang.NonNull;

public class Stock {

	
	final String ticker;
	final String name;
	@NonNull
	BigInteger quantity;
	@NonNull
	double currentPrice;
	public Stock(String ticker, String name, BigInteger quantity, double currentPrice) {
		super();
		this.ticker = ticker;
		this.name = name;
		this.quantity = quantity;
		this.currentPrice = currentPrice;
	}
	@Override
	public String toString() {
		return "Stock [ticker=" + ticker + ", name=" + name + ", quantity=" + quantity + ", currentPrice="
				+ currentPrice + "]";
	}
	
	
}

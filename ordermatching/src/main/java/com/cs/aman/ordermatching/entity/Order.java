package com.cs.aman.ordermatching.entity;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.cs.aman.ordermatching.enums.OrderType;
import com.cs.aman.ordermatching.enums.TransactionType;

public class Order {
	
	private static final AtomicLong count = new AtomicLong(0); 
	
	private final long id;

	private final TransactionType transactionType;
	
	private final OrderType orderType;
	
	private BigInteger quantity;
	
	private double price;
	
	private final long entryTime;
	

	public Order(TransactionType transactionType, OrderType orderType, BigInteger quantity, double price) {
		
		id = count.getAndIncrement();
		this.transactionType = transactionType;
		this.orderType = orderType;
		this.quantity = quantity;
		this.price = price;
		this.entryTime = System.nanoTime(); // using nano time precision. We might opt to choose more precise way to differentiate order.
	}



	public static AtomicLong getCount() {
		return count;
	}



	public long getId() {
		return id;
	}



	public TransactionType getTransactionType() {
		return transactionType;
	}



	public OrderType getOrderType() {
		return orderType;
	}



	public BigInteger getQuantity() {
		return quantity;
	}



	public double getPrice() {
		return price;
	}



	public long getEntryTime() {
		return entryTime;
	}



	public void setQuantity(BigInteger quantity) {
		this.quantity = quantity;
	}

	public void setPrice(double price) {
		this.price = price;
	}





	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id != other.id)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Order [id=" + id + ", transactionType=" + transactionType + ", orderType=" + orderType + ", quantity="
				+ quantity + ", price=" + price + ", entryTime=" + entryTime + "]";
	}



	
}

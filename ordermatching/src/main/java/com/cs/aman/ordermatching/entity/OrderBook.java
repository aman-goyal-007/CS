package com.cs.aman.ordermatching.entity;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.aman.ordermatching.enums.TransactionType;
import com.cs.aman.ordermatching.exception.InvalidPriceException;

public class OrderBook {
	Logger logger = LoggerFactory.getLogger(OrderBook.class);

	private final Stock stock;
	
	
	private Queue<Order> bidOrders;
	
	private Queue<Order> askOrders;
	
	static final int INITIALCAPACITY = 7000;
	
	public OrderBook(Stock stock){
		this.stock = stock;
		
		bidOrders = new PriorityBlockingQueue<>(INITIALCAPACITY,(o1,o2)->{
			int type = o1.getOrderType().compareTo(o2.getOrderType());
			if(type == 0) {
				return (int)(o2.getPrice()-o1.getPrice());
			}
			return type;
		});
		
		askOrders = new PriorityBlockingQueue<>(INITIALCAPACITY, (o1, o2) ->{

				int type = o1.getOrderType().compareTo(o2.getOrderType());
				if(type == 0) {
					return (int)(o1.getPrice()-o2.getPrice());
				}
				return type;
			
		});

	}

	public Stock getStock() {
		return stock;
	}

	public Queue<Order> getBidOrders() {
		return bidOrders;
	}

	public Queue<Order> getAskOrders() {
		return askOrders;
	}

	public boolean addAskOrder(Order newOrder) throws Exception {
		if(newOrder.getPrice() <= 0) {
			Exception invalidPriceException = new InvalidPriceException();
			logger.error("error in add bid order",invalidPriceException);
			throw invalidPriceException;
		}
		return askOrders.add(newOrder);
	}
	
	public boolean addBidOrder(Order newOrder) throws Exception {
		if(newOrder.getPrice() <= 0) {
			Exception invalidPriceException = new InvalidPriceException();
			logger.error("error in add bid order",invalidPriceException);
			throw invalidPriceException;
		}
		return bidOrders.add(newOrder);
	}
	
	public void cancelOrder(Order orderToBeCancel) {
		TransactionType transactionType = orderToBeCancel.getTransactionType();
		Queue<Order> queue = null;
		if(transactionType.equals(TransactionType.BUY)) {
			queue = this.getBidOrders();
			deleteOrder(orderToBeCancel, queue);
		}
		else if(transactionType.equals(TransactionType.SELL)) {
			queue = this.getAskOrders();
			deleteOrder(orderToBeCancel, queue);
		}
	}

	private void deleteOrder(Order orderToBeCancel, Queue<Order> queue) {
		Optional<Order> optional = queue.stream().filter(order -> order.equals(orderToBeCancel)).findFirst();
		if(optional.isPresent()) {
			this.getBidOrders().remove(optional.get());
		}
	}
	

	@Override
	public String toString() {
		return "OrderBook [stock=" + stock + ", bidOrders=" + bidOrders + ", askOrders=" + askOrders + "]";
	}
	
	
	
}

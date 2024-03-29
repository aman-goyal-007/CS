package com.cs.aman.ordermatching.entity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.aman.ordermatching.AskComparator;
import com.cs.aman.ordermatching.BidComparator;
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
		
		bidOrders = new PriorityBlockingQueue<>(INITIALCAPACITY,new BidComparator());
		
		askOrders = new PriorityBlockingQueue<>(INITIALCAPACITY, new AskComparator());

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
	
	/*
	 * Summary Data Converted into Immutable Structure so that consumers of this api don't intentionally/accidently modify the order.
	 */
	public Map<String, Map<String,Double>> getSummary() {
		Map<String, Map<String,Double>> summaryMap = new LinkedHashMap<>();
		Map<String,Double> bidOrderMap = new LinkedHashMap<>();
		this.getBidOrders().stream().forEach(order -> {
			bidOrderMap.put(order.getId()+"-"+order.getQuantity().toString() , order.getPrice());
		});
		summaryMap.put(TransactionType.BUY.name(), bidOrderMap);

		Map<String,Double> askOrderMap = new LinkedHashMap<>();
		this.getAskOrders().stream().forEach(order -> {
			askOrderMap.put(order.getId()+"-"+order.getQuantity().toString() , order.getPrice());
		});
		summaryMap.put(TransactionType.SELL.name(), askOrderMap);
		return summaryMap;
	}

	@Override
	public String toString() {
		return "OrderBook [stock=" + stock + ", bidOrders=" + bidOrders + ", askOrders=" + askOrders + "]";
	}
	
	
}

package com.cs.aman.ordermatching.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.aman.ordermatching.entity.Order;
import com.cs.aman.ordermatching.entity.OrderBook;
import com.cs.aman.ordermatching.entity.Stock;
import com.cs.aman.ordermatching.enums.TransactionType;
import com.cs.aman.ordermatching.exception.InvalidPriceException;
import com.cs.aman.ordermatching.service.OrderBookService;

public class OrderBookServiceImpl implements OrderBookService{

	Logger logger = LoggerFactory.getLogger(OrderBookServiceImpl.class);

	public OrderBook createOrderBook(Stock stock) {
		return new OrderBook(stock);
	}
	
	
	public boolean addAskOrder(OrderBook orderBook, Order newOrder) throws Exception {
		if(newOrder.getPrice() <= 0) {
			Exception invalidPriceException = new InvalidPriceException();
			logger.error("error in add bid order",invalidPriceException);
			throw invalidPriceException;
		}
		return orderBook.getAskOrders().add(newOrder);
	}
	
	public boolean addBidOrder(OrderBook orderBook,Order newOrder) throws Exception {
		if(newOrder.getPrice() <= 0) {
			Exception invalidPriceException = new InvalidPriceException();
			logger.error("error in add bid order",invalidPriceException);
			throw invalidPriceException;
		}
		return orderBook.getBidOrders().add(newOrder);
	}
	
	public void cancelOrder(OrderBook orderBook,Order orderToBeCancel) {
		TransactionType transactionType = orderToBeCancel.getTransactionType();
		Queue<Order> queue = null;
		if(transactionType.equals(TransactionType.BUY)) {
			queue = orderBook.getBidOrders();
			deleteOrder(orderBook,orderToBeCancel, queue);
		}
		else if(transactionType.equals(TransactionType.SELL)) {
			queue = orderBook.getAskOrders();
			deleteOrder(orderBook, orderToBeCancel, queue);
		}
	}
	private void deleteOrder(OrderBook orderBook,Order orderToBeCancel, Queue<Order> queue) {
		Optional<Order> optional = queue.stream().filter(order -> order.equals(orderToBeCancel)).findFirst();
		if(optional.isPresent()) {
			orderBook.getBidOrders().remove(optional.get());
		}
	}
	
	
	/*
	 * Summary Data Converted into Immutable Structure so that consumers of this api don't intentionally/accidently modify the order.
	 */
	public Map<String, Map<String,Double>> getSummary(OrderBook orderBook) {
		Map<String, Map<String,Double>> summaryMap = new LinkedHashMap<>();
		Map<String,Double> bidOrderMap = new LinkedHashMap<>();
		orderBook.getBidOrders().stream().forEach(order -> {
			bidOrderMap.put(order.getId()+"-"+order.getQuantity().toString() , order.getPrice());
		});
		summaryMap.put(TransactionType.BUY.name(), bidOrderMap);

		Map<String,Double> askOrderMap = new LinkedHashMap<>();
		orderBook.getAskOrders().stream().forEach(order -> {
			askOrderMap.put(order.getId()+"-"+order.getQuantity().toString() , order.getPrice());
		});
		summaryMap.put(TransactionType.SELL.name(), askOrderMap);
		return summaryMap;
	}
}

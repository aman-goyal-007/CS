package com.cs.aman.ordermatching.service;

import java.util.Map;

import com.cs.aman.ordermatching.entity.Order;
import com.cs.aman.ordermatching.entity.OrderBook;
import com.cs.aman.ordermatching.entity.Stock;

public interface OrderBookService {
	
	public OrderBook createOrderBook(Stock stock);
	
	public boolean addAskOrder(OrderBook orderBook, Order newOrder) throws Exception;
	
	public boolean addBidOrder(OrderBook orderBook,Order newOrder) throws Exception;
	
	public void cancelOrder(OrderBook orderBook,Order orderToBeCancel);
	
	public Map<String, Map<String,Double>> getSummary(OrderBook orderBook);

}

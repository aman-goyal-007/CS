package com.cs.aman.ordermatching.engine.impl;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.aman.ordermatching.engine.ExecutionEngine;
import com.cs.aman.ordermatching.entity.Order;
import com.cs.aman.ordermatching.entity.OrderBook;
import com.cs.aman.ordermatching.enums.OrderType;

public class BasicOrderExecutionAlgo implements ExecutionEngine{
	private static final String matchedOrderLogPrefix = "Matched Order : ";
	Logger logger = LoggerFactory.getLogger(BasicOrderExecutionAlgo.class);


	public void executeOrderBook(OrderBook orderBook) {
		Queue<Order> allBidOrders = orderBook.getBidOrders();
		Queue<Order> allAskOrders = orderBook.getAskOrders();
		
		while(true) {
			Order bidOrder = allBidOrders.peek();
			Order askOrder = allAskOrders.peek();
			if(validateOrder(bidOrder, askOrder)) {
				break;
			}
			BigInteger bidQuantity = bidOrder.getQuantity();
			BigInteger askQuantity = askOrder.getQuantity();
			OrderType bidOrderType = bidOrder.getOrderType();
			OrderType askOrderType = askOrder.getOrderType();
			double bidPrice = bidOrder.getPrice();
			double askPrice = askOrder.getPrice();
			
			if(OrderType.MARKET.equals(bidOrderType)) {
				bidPrice = askPrice;
			}else if(OrderType.MARKET.equals(askOrderType)) {
				askPrice = bidPrice;
			}
			
			if(bidPrice >= askPrice) {
				
				processCurrentMatched(allBidOrders, allAskOrders, bidOrder, askOrder, bidQuantity, askQuantity,
						bidPrice, askPrice);
				
			}else {
				break;
			}
			
		}
		
	}


	private boolean validateOrder(Order bidOrder, Order askOrder) {
		return Objects.isNull(bidOrder) || Objects.isNull(askOrder);
	}


	private void processCurrentMatched(Queue<Order> allBidOrders, Queue<Order> allAskOrders, Order bidOrder,
			Order askOrder, BigInteger bidQuantity, BigInteger askQuantity, double bidPrice, double askPrice) {
		if(bidQuantity.equals(askQuantity)) {
			allQuantitiesMatched(allBidOrders, allAskOrders, bidPrice, askPrice);
		}else if(bidQuantity.compareTo(askQuantity) > 1) {
			reduceMatchedBidQuantity(allBidOrders, allAskOrders, bidOrder, bidQuantity, askQuantity, bidPrice,
					askPrice);
		}else if(bidQuantity.compareTo(askQuantity) < 1) {
			reduceMatchedAskQuantities(allBidOrders, allAskOrders, askOrder, bidQuantity, askQuantity, bidPrice,
					askPrice);
			
		}
	}


	private void reduceMatchedAskQuantities(Queue<Order> allBidOrders, Queue<Order> allAskOrders, Order askOrder,
			BigInteger bidQuantity, BigInteger askQuantity, double bidPrice, double askPrice) {
		setupUpdatedPrice(allBidOrders, allAskOrders, bidPrice, askPrice);
		BigInteger newAskQuantity = askQuantity.subtract(bidQuantity);
		askOrder.setQuantity(newAskQuantity);
		Order processedBidOrder = allBidOrders.poll();

		Order processedAskOrder = askOrder;
		logger.info(matchedOrderLogPrefix+processedBidOrder +","+processedAskOrder);
	}


	private void reduceMatchedBidQuantity(Queue<Order> allBidOrders, Queue<Order> allAskOrders, Order bidOrder,
			BigInteger bidQuantity, BigInteger askQuantity, double bidPrice, double askPrice) {
		setupUpdatedPrice(allBidOrders, allAskOrders, bidPrice, askPrice);
		BigInteger newBidQuantity = bidQuantity.subtract(askQuantity);
		bidOrder.setQuantity(newBidQuantity);
		Order processedAskOrder = allAskOrders.poll();

		Order processedBidOrder = bidOrder;
		logger.info(matchedOrderLogPrefix+processedBidOrder +","+processedAskOrder);
	}


	private void allQuantitiesMatched(Queue<Order> allBidOrders, Queue<Order> allAskOrders, double bidPrice,
			double askPrice) {
		setupUpdatedPrice(allBidOrders, allAskOrders, bidPrice, askPrice);
		Order processedBidOrder = allBidOrders.poll();
		Order processedAskOrder = allAskOrders.poll();
		logger.info(matchedOrderLogPrefix+processedBidOrder +","+processedAskOrder);
	}


	private void setupUpdatedPrice(Queue<Order> allBidOrders, Queue<Order> allAskOrders, double bidPrice,
			double askPrice) {
		double newBidPrice = bidPrice;
		double newAskPrice = askPrice;
		allBidOrders.stream().forEach( order -> order.setPrice(newBidPrice));
		allAskOrders.stream().forEach( order -> order.setPrice(newAskPrice));
	}
}

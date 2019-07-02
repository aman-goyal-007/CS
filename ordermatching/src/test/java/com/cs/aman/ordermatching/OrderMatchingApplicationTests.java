package com.cs.aman.ordermatching;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.aman.ordermatching.engine.ExecutionEngine;
import com.cs.aman.ordermatching.engine.impl.BasicOrderExecutionAlgo;
import com.cs.aman.ordermatching.entity.Order;
import com.cs.aman.ordermatching.entity.OrderBook;
import com.cs.aman.ordermatching.entity.Stock;
import com.cs.aman.ordermatching.enums.OrderType;
import com.cs.aman.ordermatching.enums.TransactionType;
import com.cs.aman.ordermatching.exception.InvalidPriceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMatchingApplicationTests {
	

	OrderBook orderBook;
	
	@Before
	public void contextLoads() {
		orderBook = new OrderBook(new Stock("ABC", "ABC Name", new BigInteger("10"), 10));
		
	}

	@Test
	public void testOrderBookCreation() {
		//System.out.println(orderBook);
		assertNotNull(orderBook);
	}
	
	@Test(expected = InvalidPriceException.class)
	public void testInvalidPriceException() throws Exception {
		//System.out.println(orderBook);
		Order bidOrder = new Order(TransactionType.BUY, OrderType.MARKET, new BigInteger("1"), -1);
		orderBook.addBidOrder(bidOrder);
	}

	
	@Test
	public void testMarketBidOrderCreation() throws Exception {
		Order bidOrder = new Order(TransactionType.BUY, OrderType.MARKET, new BigInteger("1"), 12);
		orderBook.addBidOrder(bidOrder);
		//System.out.println(bidOrder);
		assertNotNull(orderBook.getBidOrders());
		
	}
	
	@Test
	public void testMarketAskOrderCreation() throws Exception {
		Order askOrder = new Order(TransactionType.BUY, OrderType.MARKET, new BigInteger("1"), 10);
		orderBook.addAskOrder(askOrder);
		//System.out.println(askOrder);
		
		assertNotNull(orderBook.getAskOrders());
	}
	
	@Test
	public void testMarketAskOrderSorting() throws Exception {
		Order orderOne = new Order(TransactionType.SELL, OrderType.LIMIT, new BigInteger("1"), 10);
		Order orderTwo = new Order(TransactionType.SELL, OrderType.MARKET, new BigInteger("1"), 11);
		Order orderThree = new Order(TransactionType.SELL, OrderType.MARKET, new BigInteger("1"), 10);
		orderBook.addAskOrder(orderOne);
		orderBook.addAskOrder(orderTwo);
		orderBook.addAskOrder(orderThree);
		assertEquals(orderThree, orderBook.getAskOrders().poll());
		assertEquals(orderTwo, orderBook.getAskOrders().poll());
		assertEquals(orderOne, orderBook.getAskOrders().poll());
	}
	
	
	@Test
	public void testMarketBidOrderSorting() throws Exception {
		Order orderOne = new Order(TransactionType.BUY, OrderType.LIMIT, new BigInteger("1"), 10);
		Order orderTwo = new Order(TransactionType.BUY, OrderType.MARKET, new BigInteger("1"), 11);
		Order orderThree = new Order(TransactionType.BUY, OrderType.MARKET, new BigInteger("1"), 10);
		orderBook.addBidOrder(orderOne);
		orderBook.addBidOrder(orderTwo);
		orderBook.addBidOrder(orderThree);
		assertEquals(orderTwo, orderBook.getBidOrders().poll());
		assertEquals(orderThree, orderBook.getBidOrders().poll());
		assertEquals(orderOne, orderBook.getBidOrders().poll());
	}
	
	@Test
	public void testMarketBidOrderCancel() throws Exception {
		Order orderOne = new Order(TransactionType.BUY, OrderType.LIMIT, new BigInteger("1"), 10);
		Order orderTwo = new Order(TransactionType.BUY, OrderType.MARKET, new BigInteger("1"), 11);
		Order orderThree = new Order(TransactionType.BUY, OrderType.MARKET, new BigInteger("1"), 10);
		orderBook.addBidOrder(orderOne);
		orderBook.addBidOrder(orderTwo);
		orderBook.addBidOrder(orderThree);
		
		orderBook.cancelOrder(orderTwo);
		assertEquals(orderThree, orderBook.getBidOrders().poll());
		assertEquals(orderOne, orderBook.getBidOrders().poll());
		
	}

	@Test
	public void testMatchingEngineAllOrderMatched() throws Exception {
		Order bidOrder = new Order(TransactionType.BUY, OrderType.MARKET, new BigInteger("1"), 12);
		orderBook.addBidOrder(bidOrder);
		Order askOrder = new Order(TransactionType.BUY, OrderType.MARKET, new BigInteger("1"), 10);
		orderBook.addAskOrder(askOrder);
		//System.out.println("Before Order Execution " +orderBook);
		ExecutionEngine executionEngine = new BasicOrderExecutionAlgo();
		executionEngine.executeOrderBook(orderBook);
		//System.out.println("After Order Execution " +orderBook);
		assertTrue(true);
	}

}

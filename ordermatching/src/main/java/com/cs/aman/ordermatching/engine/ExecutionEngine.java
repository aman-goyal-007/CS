package com.cs.aman.ordermatching.engine;

import com.cs.aman.ordermatching.entity.OrderBook;

public interface ExecutionEngine {
	public void executeOrderBook(OrderBook orderBook);

}

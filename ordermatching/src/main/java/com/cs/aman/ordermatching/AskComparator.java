package com.cs.aman.ordermatching;

import java.util.Comparator;

import com.cs.aman.ordermatching.entity.Order;

public class AskComparator implements Comparator<Order> {

	@Override
	public int compare(Order o1, Order o2) {
		int type = o1.getOrderType().compareTo(o2.getOrderType());
		if(type == 0) {
			int priceCompare =  (int)(o1.getPrice()-o2.getPrice());
			if(priceCompare == 0) {
				return (int)(o1.getEntryTime()-o2.getEntryTime());
			}
			else return priceCompare;
		}
		return type;
	}


}

package com.lmax.util;

import com.lmax.api.FixedPointNumber;
import com.lmax.api.TimeInForce;
import com.lmax.api.order.Order;
import com.lmax.api.order.OrderType;

public class CurrentOrder implements Order {
	
	 private final String instructionId;
	    private final String originalInstructionId;
	    private final String orderId;
	    private final long instrumentId;
	    private final long accountId;
	    private final OrderType orderType;
	    private final FixedPointNumber quantity;
	    private final FixedPointNumber filledQuantity;
	    private final FixedPointNumber cancelledQuantity;
	    private final FixedPointNumber limitPrice;
	    private final FixedPointNumber stopReferencePrice;
	    private final FixedPointNumber stopLossOffset;
	    private final FixedPointNumber stopProfitOffset;
	    private final FixedPointNumber commission;
	    private final TimeInForce timeInForce;
	    
	    private FixedPointNumber currentQuantity;

	    public CurrentOrder(final String originalInstructionId, final String instructionId, final String orderId, final long instrumentId, final long accountId, final OrderType orderType,
	                     TimeInForce timeInForce, final FixedPointNumber quantity, final FixedPointNumber filledQuantity, final FixedPointNumber cancelledQuantity, final FixedPointNumber limitPrice,
	                     final FixedPointNumber stopReferencePrice, final FixedPointNumber stopLossOffset, final FixedPointNumber stopProfitOffset, final FixedPointNumber commission)
	    {
	        this.instructionId = instructionId;
	        this.orderId = orderId;
	        this.instrumentId = instrumentId;
	        this.accountId = accountId;
	        this.orderType = orderType;
	        this.timeInForce = timeInForce;
	        this.quantity = quantity;
	        this.filledQuantity = filledQuantity;
	        this.cancelledQuantity = cancelledQuantity;
	        this.limitPrice = limitPrice;
	        this.stopReferencePrice = stopReferencePrice;
	        this.stopLossOffset = stopLossOffset;
	        this.stopProfitOffset = stopProfitOffset;
	        this.originalInstructionId = originalInstructionId;
	        this.commission = commission;
	    }
	    
	    

	    public FixedPointNumber getCurrentQuantity() {
			return currentQuantity;
		}



		public void setCurrentQuantity(FixedPointNumber currentQuantity) {
			this.currentQuantity = currentQuantity;
		}



		@Override
	    public FixedPointNumber getCancelledQuantity()
	    {
	        return cancelledQuantity;
	    }

	    @Override
	    public FixedPointNumber getCommission()
	    {
	        return commission;
	    }

	    @Override
	    public String getInstructionId()
	    {
	        return instructionId;
	    }

	    @Override
	    public String getOrderId()
	    {
	        return orderId;
	    }

	    @Override
	    public long getInstrumentId()
	    {
	        return instrumentId;
	    }

	    @Override
	    public long getAccountId()
	    {
	        return accountId;
	    }

	    @Override
	    public OrderType getOrderType()
	    {
	        return orderType;
	    }

	    @Override
	    public TimeInForce getTimeInForce()
	    {
	        return timeInForce;
	    }

	    @Override
	    public FixedPointNumber getQuantity()
	    {
	        return quantity;
	    }

	    @Override
	    public FixedPointNumber getFilledQuantity()
	    {
	        return filledQuantity;
	    }

	    @Override
	    public FixedPointNumber getLimitPrice()
	    {
	        return limitPrice;
	    }

	    @Override
	    public FixedPointNumber getStopReferencePrice()
	    {
	        return stopReferencePrice;
	    }

	    @Override
	    public FixedPointNumber getStopLossOffset()
	    {
	        return stopLossOffset;
	    }

	    @Override
	    public FixedPointNumber getStopProfitOffset()
	    {
	        return stopProfitOffset;
	    }

	    @Override
	    public String getOriginalInstructionId()
	    {
	        return originalInstructionId;
	    }

	    @Override
	    public String toString()
	    {
	        return "OrderImpl{" +
	               "instructionId='" + instructionId + '\'' +
	               ", originalInstructionId='" + originalInstructionId + '\'' +
	               ", orderId='" + orderId + '\'' +
	               ", instrumentId=" + instrumentId +
	               ", accountId=" + accountId +
	               ", orderType=" + orderType +
	               ", timeInForce=" + timeInForce +
	               ", quantity=" + quantity +
	               ", currentQuantity=" + currentQuantity + 
	               ", filledQuantity=" + filledQuantity +
	               ", cancelledQuantity=" + cancelledQuantity +
	               ", limitPrice=" + limitPrice +
	               ", stopReferencePrice=" + stopReferencePrice +
	               ", stopLossOffset=" + stopLossOffset +
	               ", stopProfitOffset=" + stopProfitOffset +
	               ", commission=" + commission +
	               '}';
	    }
	
	

}

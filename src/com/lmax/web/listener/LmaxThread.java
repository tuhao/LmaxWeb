package com.lmax.web.listener;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lmax.api.Callback;
import com.lmax.api.FailureResponse;
import com.lmax.api.FixedPointNumber;
import com.lmax.api.LmaxApi;
import com.lmax.api.Session;
import com.lmax.api.SubscriptionRequest;
import com.lmax.api.TimeInForce;
import com.lmax.api.account.AccountStateEvent;
import com.lmax.api.account.AccountStateEventListener;
import com.lmax.api.account.AccountSubscriptionRequest;
import com.lmax.api.account.LoginCallback;
import com.lmax.api.account.LoginRequest;
import com.lmax.api.account.LoginRequest.ProductType;
import com.lmax.api.order.ClosingOrderSpecification;
import com.lmax.api.order.MarketOrderSpecification;
import com.lmax.api.order.Order;
import com.lmax.api.order.OrderEventListener;
import com.lmax.api.order.OrderSubscriptionRequest;
import com.lmax.api.orderbook.Instrument;
import com.lmax.api.orderbook.OrderBookEvent;
import com.lmax.api.orderbook.OrderBookEventListener;
import com.lmax.api.orderbook.OrderBookSubscriptionRequest;
import com.lmax.api.orderbook.SearchInstrumentCallback;
import com.lmax.api.orderbook.SearchInstrumentRequest;
import com.lmax.api.position.PositionEvent;
import com.lmax.api.position.PositionEventListener;
import com.lmax.api.position.PositionSubscriptionRequest;
import com.lmax.util.ActionCallback;
import com.lmax.util.CurrentOrder;

public class LmaxThread extends Thread implements LoginCallback,OrderBookEventListener,OrderEventListener,PositionEventListener,AccountStateEventListener{
	
		private String url;
		private String username;
		private String password;
		private ProductType productType;
		private SessionListener sessionListener;
		
		private Session session = null;
		
		private List<Long> instrumentIds = new LinkedList<Long>();
		
		private static Log log = LogFactory.getLog(LmaxThread.class);

		public LmaxThread(String url, String username, String password,
				String demo,SessionListener sessionListener) {
			this.url = url;
			this.username = username;
			this.password = password;
			this.productType = ProductType.valueOf(demo);
			this.sessionListener = sessionListener;
		}
		


		@Override
		public void run() {
			// TODO Auto-generated method stub
			LmaxApi lmaxApi = new LmaxApi(url);
			lmaxApi.login(new LoginRequest(username, password, productType),this);
		}

		@Override
		public void onLoginFailure(FailureResponse arg0) {
			// TODO Auto-generated method stub
			log.error(arg0);
		}

		@Override
		public void onLoginSuccess(Session session) {
			// TODO Auto-generated method stub
			this.session = session;
			session.registerSessionDisconnectedListener(sessionListener);
			
			session.registerOrderEventListener(this);
			subscribe(session, new OrderSubscriptionRequest(), "Orders");
			
			session.registerPositionEventListener(this);
			subscribe(session, new PositionSubscriptionRequest(), "Positions");
			
			session.registerAccountStateEventListener(this);
			subscribe(session, new AccountSubscriptionRequest(), "Account Updates");
			
			session.registerOrderBookEventListener(this);
			loadAllInstruments();
			for(long id:instrumentIds){
				subscribeToInstrument(session, id);
			}
			session.start();
		}

		private void loadAllInstruments(){
			final long[] offset = { 0 };
			final boolean[] hasMore = { true };

			while (hasMore[0]) {
				session.searchInstruments(new SearchInstrumentRequest("",offset[0]), new SearchInstrumentCallback() {
					@Override
					public void onSuccess(List<Instrument> instruments,boolean hasMoreResults) {
						hasMore[0] = hasMoreResults;
						for (Instrument instrument : instruments) {
							log.info(String.format("Instrument:%d,%s",instrument.getId(),instrument.getName()));
							offset[0] = instrument.getId();
							instrumentIds.add(instrument.getId());
						}
					}

					@Override
					public void onFailure(FailureResponse failureResponse) {
						hasMore[0] = false;
						throw new RuntimeException("Failed: " + failureResponse);
					}
				});
			}
		}

		private void subscribeToInstrument(final Session session,final long instrumentId) {
			session.subscribe(new OrderBookSubscriptionRequest(instrumentId),new Callback() {
						public void onSuccess() {
							log.info(String.format("Subscribed to instrument %d.%n",instrumentId));
						}

						public void onFailure(
								final FailureResponse failureResponse) {
							log.error(String.format("Failed to subscribe to instrument %d: %s%n",instrumentId,failureResponse));
							}
					});
		}

		@Override
		public void notify(OrderBookEvent orderBookEvent) {
			// TODO Auto-generated method stub
			sessionListener.orderBookEventMap.put(orderBookEvent.getInstrumentId(), orderBookEvent);
		}
		
		/**
		 * 
		 * @param instrumentId
		 * @param quantity
		 * @param sessionListener
		 */
		public void placeOrder(long instrumentId,long quantity,final ActionCallback actionCallback){
			session.placeMarketOrder(new MarketOrderSpecification(instrumentId,FixedPointNumber.valueOf(quantity),TimeInForce.IMMEDIATE_OR_CANCEL), actionCallback);
		}
		
		/**
		 * 
		 * @param instructionId
		 * @param instrumentId
		 * @param quantity
		 * @param sessionListener
		 */
		public void closeOrder(String instructionId,long instrumentId,long quantity,ActionCallback actionCallback){
			session.placeClosingOrder(new ClosingOrderSpecification(instructionId.concat("0"),instrumentId,instructionId,FixedPointNumber.valueOf(quantity)), actionCallback);
			
		}
		
		private void subscribe(final Session session, final SubscriptionRequest request, final String subscriptionDescription)
		    {
		        session.subscribe(request,new Callback()
		        {
		            public void onSuccess()
		            {
		            	log.info(String.format("Subscribed to %s",subscriptionDescription));
		            }

		            public void onFailure(final FailureResponse failureResponse)
		            {
		            	log.error(String.format("Failed to subscribe to %s:%s%n",subscriptionDescription,failureResponse));
		            }
		        });
		    }




		@Override
		public void notify(Order o) {
			// TODO Auto-generated method stub
			log.info(o);
			String key = o.getInstructionId();
			String originId = key.substring(0, key.length() - 1);
			CurrentOrder order = sessionListener.orderMap.get(originId);
			if(order != null){
				if(Math.abs(o.getQuantity().longValue()) >= Math.abs(order.getCurrentQuantity().longValue())){
					sessionListener.orderMap.remove(originId);
				}else{
					order.setCurrentQuantity(FixedPointNumber.valueOf(o.getQuantity().longValue() + order.getCurrentQuantity().longValue()));
					sessionListener.orderMap.put(originId, order);
				}
			}else{
				CurrentOrder currentOrder = new CurrentOrder(o.getOriginalInstructionId(),o.getInstructionId(),
						o.getOrderId(),o.getInstrumentId(),o.getAccountId(),o.getOrderType(),o.getTimeInForce(),
						o.getQuantity(),o.getFilledQuantity(),o.getCancelledQuantity(),o.getLimitPrice(),
						o.getStopReferencePrice(),o.getStopLossOffset(),o.getStopProfitOffset(),o.getCommission());
				currentOrder.setCurrentQuantity(o.getQuantity());
				sessionListener.orderMap.put(key, currentOrder);
			}
		}



		@Override
		public void notify(final PositionEvent positionEvent) {
			// TODO Auto-generated method stub
			log.info(positionEvent);
			sessionListener.positionInfo = positionEvent.toString();
		}



		@Override
		public void notify(AccountStateEvent accountStateEvent) {
			// TODO Auto-generated method stub
			log.info(accountStateEvent);
			sessionListener.accountInfo = accountStateEvent.toString();
		}

}

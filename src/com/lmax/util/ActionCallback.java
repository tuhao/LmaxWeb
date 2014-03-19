package com.lmax.util;

import com.lmax.api.FailureResponse;
import com.lmax.api.order.OrderCallback;

public class ActionCallback implements OrderCallback{
	
	public String result = null;

	@Override
	public void onFailure(FailureResponse arg0) {
		// TODO Auto-generated method stub
		this.result = arg0.toString();
	}

	@Override
	public void onSuccess(String arg0) {
		// TODO Auto-generated method stub
		this.result = arg0;
	}

}

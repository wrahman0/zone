package com.wasiur.render;

import android.os.AsyncTask;

import com.wasiur.parser.ParserResponse;
import com.wasiur.parser.RequestInformation;
import com.wasiur.parser.ResponseHolder;

public class MainScreenRender extends AsyncTask<Void, Void, ResponseHolder>{
	
	private ParserResponse mListener;
	private RequestInformation mRequestInformation;

	public MainScreenRender(ParserResponse listener, RequestInformation requestInformation) {
		this.mListener = listener;
		this.mRequestInformation = requestInformation;
	}
	
	@Override
	protected void onPreExecute() {
		mListener.startProgressSpinner();
		super.onPreExecute();
	}

	@Override
	protected ResponseHolder doInBackground(Void... args) {
		return mRequestInformation.setResponseHolder();
	}
	
	@Override
	protected void onPostExecute(ResponseHolder responseHolder) {
		mListener.initializeTabs(responseHolder);
		mListener.stopProgressSpinner();
		super.onPostExecute(responseHolder);
	}

}

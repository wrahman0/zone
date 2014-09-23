package com.wasiur.parser;

public interface ParserResponse {
	void onParseComplete(ResponseHolder responseHolder);
	void stopProgressSpinner();
	void startProgressSpinner();
	void initializeTabs();
}

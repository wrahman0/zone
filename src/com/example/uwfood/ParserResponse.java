package com.example.uwfood;

import java.util.ArrayList;

import org.json.JSONObject;

public interface ParserResponse {
	void onParseComplete(JSONObject json);
	void onParseComplete(ArrayList<Outlet> arrayListOfOutlets);
}

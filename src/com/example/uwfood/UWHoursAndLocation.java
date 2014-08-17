//package com.example.uwfood;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.ArrayList;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class UWHoursAndLocation {
//	
//	private static final String url = "https://uwaterloo.ca/food-services/locations-and-hours";
//	
//	//Stores all the different on campus outlets
//	//May or may not contain [Debit info, Description, Hours of operation, Exceptions, Notes, Images of the location]
//	private ArrayList<Element> outletElementArrayList;
//	private ArrayList<Outlet> outletObjectArrayList;
//	
//	private ParserResponse listener;
//	
//	public UWHoursAndLocation(ParserResponse listener, boolean flag){
//		this.listener = listener;
//		outletObjectArrayList = new ArrayList<Outlet>();
//		outletElementArrayList = new ArrayList<Element>();
//		if (flag) connect();
//	}
//	
//	public void connect(){
//		new MyAsyncTask().execute(url);
//	}
//	
//	protected void setOutletElementArrayList(ArrayList<Element> outletElementArrayList){
//		this.outletElementArrayList= outletElementArrayList;
//	}
//	
//	protected ArrayList<Element> getOutletElementArrayList(){
//		return outletElementArrayList;
//	}
//	
//	public ArrayList<Outlet> getOutletObjectArrayList() {
//		return outletObjectArrayList;
//	}
//
//	protected void setOutletObjectArrayList(ArrayList<Outlet> outletObjectArrayList) {
//		this.outletObjectArrayList = outletObjectArrayList;
//	}
//
//
//
//	private class MyAsyncTask extends AsyncTask<String, String, String>{
//		@Override
//		protected String doInBackground(String... args) {
//			
//			try {
//				
//				URL url = new URL(args[0]);
//				URLConnection connection = url.openConnection();
//				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//				StringBuilder response = new StringBuilder();
//				String line;
//				
//				while ((line = bufferedReader.readLine())!=null){
//					response.append(line);
//				}
//				
//				Log.e("HOURS AND LOCATION", response.toString());
//				Document doc = Jsoup.parse(response.toString());
//				
//				Elements outletElements = doc.select(".views-row");
//				ArrayList<Element> elements = new ArrayList<Element>();
//				
//				for (Element ele:outletElements){
//					elements.add(ele);	
//				}
//				
//				setOutletElementArrayList(elements);
//				
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			} catch (IOException e){
//				e.printStackTrace();
//			}
//			
//			return null;
//			
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			ArrayList<Element> arrayOfElements = getOutletElementArrayList();
//			ArrayList<Outlet> arrayOfOutlets = getOutletObjectArrayList();
//			for (Element ele: arrayOfElements){
////				Outlet outlet = new Outlet(ele);
////				arrayOfOutlets.add(outlet);
//			}
//			setOutletObjectArrayList(arrayOfOutlets);
//			listener.onParseComplete(arrayOfOutlets);
//		}
//		
//	}
//}

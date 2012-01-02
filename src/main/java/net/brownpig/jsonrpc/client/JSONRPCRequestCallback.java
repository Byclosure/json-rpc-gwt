package net.brownpig.jsonrpc.client;

import net.brownpig.jsonrpc.client.vo.JSONObjectVO;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class JSONRPCRequestCallback<T> implements RequestCallback {

	private static final int HTTP_OK = 200;
	private AsyncCallback<T> callback;
	private String id;

	
	public JSONRPCRequestCallback(AsyncCallback<T> callback, String id )
	{
		this.callback = callback;
		this.id = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onResponseReceived(Request request, Response response) {
		// TODO Auto-generated method stub
		if( response.getStatusCode() == HTTP_OK )
		{
			JSONObject jsonObject = JSONParser.parseStrict(response.getText()).isObject();
			if( jsonObject != null )
			{
				callback.onSuccess((T)deserialize(jsonObject.get("result")));
			}
		}
	}
	@Override
	public void onError(Request request, Throwable exception) {
		// TODO Auto-generated method stub

	}
	private Object deserialize( JSONValue value )
	{		
		if(value instanceof JSONString)
		{
			return deserializeString(value.isString());
			
		} else if( value instanceof JSONArray ) {
			
			return deserializeArray(value.isArray());
			
		} else if( value instanceof JSONBoolean ) {
			
			return deserializeBoolean(value.isBoolean());
			
		} else if( value instanceof JSONNumber ) {
			
			return deserializeNumber(value.isNumber());
			
		} else if( value instanceof JSONObject ) {
			
			return deserializeObject(value.isObject());
			
		}
		return null;
	}
	private String deserializeString(JSONString value)
	{
		return value.stringValue();
	}
	private Number deserializeNumber(JSONNumber value)
	{
		return value.doubleValue();
	}
	private Boolean deserializeBoolean(JSONBoolean value)
	{
		return value.booleanValue();
	}
	private JSONObjectVO deserializeObject(JSONObject value )
	{
		JSONObjectVO jsonObjectVO = new JSONObjectVO();
		
		for( String key : value.keySet() )
		{
			jsonObjectVO.setValue(key, deserialize(value.get(key)));
		}
		return jsonObjectVO;
	}
	private Object[] deserializeArray(JSONArray value)
	{
		Object[] result = new Object[value.size()];
		for( int n=0; n<value.size(); n++ )
		{
			result[n] = deserialize( value.get(n) );
		}
		return result;
	}
}
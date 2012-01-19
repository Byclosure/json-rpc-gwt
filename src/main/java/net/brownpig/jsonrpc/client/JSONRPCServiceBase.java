package net.brownpig.jsonrpc.client;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class JSONRPCServiceBase implements ServiceDefTarget {

	public static String rpcServiceUrl = "http://localhost/~gtejeda/ctrlprint/service/json.php";

	public String getRpcServiceUrl() {
		return rpcServiceUrl;
	}
	public void setRpcServiceUrl(String rpcServiceUrl) {
		this.rpcServiceUrl = rpcServiceUrl;
	}
	protected RequestBuilder createBuilder()
	{
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, rpcServiceUrl);
		builder.setHeader("Content-type", "application/json; charset=utf-8");
		return builder;
	}
	protected JSONObject createRequestData(String method, JSONArray values, String id )
	{
		JSONObject requestData = new JSONObject();
		requestData.put("jsonrpc", new JSONString("2.0"));
		requestData.put("id", new JSONString(id));
		requestData.put("method", new JSONString(method));
		requestData.put("params", values);
		return requestData;
	}

	@Override
	public String getSerializationPolicyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServiceEntryPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRpcRequestBuilder(RpcRequestBuilder builder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServiceEntryPoint(String address) {
		// TODO Auto-generated method stub
		
	}
	protected JSONValue serialize( Object value )
	{
		if( value instanceof String )
		{
			return serializeJSONString((String)value);
			
		} else if( value instanceof Number ) {
			
			return serializeJSONNumber((Number)value);
			
		} else if( value instanceof Boolean) {
			
			return serializeJSONBoolean((Boolean)value);
			
		} else if( value instanceof Object[]) {
			
			return serializeJSONArray((Object[])value);
		} else if( value instanceof JSONValue) {
			return (JSONObject) value;
		}
		
		return null;
	}
	private JSONString serializeJSONString(String value)
	{
		return new JSONString(value);
	}
	private JSONNumber serializeJSONNumber(Number value)
	{
		return new JSONNumber(value.doubleValue());
	}
	private JSONArray serializeJSONArray(Object[] value)
	{
		JSONArray result = new JSONArray();
		for( int n=0; n<value.length; n++ )
		{
			result.set(n, serialize( value[n] ));
		}
		return result;
	}
	private JSONBoolean serializeJSONBoolean(Boolean value)
	{
		return JSONBoolean.getInstance(value);
	}
	
	protected native String randomUUID()
	/*-{
		
	  var s = [], itoh = '0123456789ABCDEF';
	 
	  // Make array of random hex digits. The UUID only has 32 digits in it, but we
	  // allocate an extra items to make room for the '-'s we'll be inserting.
	  for (var i = 0; i <36; i++) s[i] = Math.floor(Math.random()*0x10);
	 
	  // Conform to RFC-4122, section 4.4
	  s[14] = 4;  // Set 4 high bits of time_high field to version
	  s[19] = (s[19] & 0x3) | 0x8;  // Specify 2 high bits of clock sequence
	 
	  // Convert to hex chars
	  for (var i = 0; i <36; i++) s[i] = itoh[s[i]];
	 
	  // Insert '-'s
	  s[8] = s[13] = s[18] = s[23] = '-';
	 
	  return s.join('');
			
	}-*/;
}

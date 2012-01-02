package net.brownpig.jsonrpc.client;

public class JSONRPCEmptyResponseException extends Throwable {

	private static final long serialVersionUID = 1L;

	public JSONRPCEmptyResponseException() {
		super("Empty response");
	}

}

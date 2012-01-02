package net.brownpig.jsonrpc.rebind;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;


import net.brownpig.jsonrpc.client.JSONRPCServiceBase;
import net.brownpig.jsonrpc.client.RemoteJSONRPCService;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class JSONRPCProxyGenerator extends Generator {

	private SourceWriter sourceWriter;
	private StringWriter debugWriter;
	
	private Object[] nativesTypes = new Object[] {
			
		String.class.getName(),
		Number.class.getName(),
		Object[].class.getName()
	};
	
	private static void logAndThrow(TreeLogger logger, String errorMessage)
		      throws UnableToCompleteException {
		    logger.log(TreeLogger.ERROR, errorMessage, null);
		    throw new UnableToCompleteException();
		  }
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		
		TypeOracle oracle = context.getTypeOracle();
		JClassType wrappedInterface = oracle.findType(typeName);
		
		if (!wrappedInterface.isAssignableTo(oracle
		        .findType(RemoteJSONRPCService.class.getCanonicalName()))) {
		      logAndThrow(logger, "Type " + typeName
		          + " should inherit from RemoteJSONService.");
		    }
		
		JClassType wrappedAsyncInterface = oracle.findType(typeName+"Async");
		String wrappedClassName = simpleStubClassName(wrappedAsyncInterface.getName());
		
		sourceWriter = getSourceWriter(logger,
				context, 
				wrappedAsyncInterface.getPackage().getName(),
				wrappedClassName, 
				wrappedAsyncInterface.getName(), 
				wrappedInterface);
		if(sourceWriter!=null)
		{
			for( JMethod method : wrappedAsyncInterface.getMethods())
			{
				printAsyncMethod(method);
			}
			sourceWriter.commit(logger);
		}
		return wrappedAsyncInterface.getPackage().getName()+"."+wrappedClassName;
	}
	private SourceWriter getSourceWriter( TreeLogger logger,GeneratorContext context, String packageName, String className, String interfaceName, JClassType wrappedInterface )
	{
		PrintWriter printWriter = context.tryCreate(logger, packageName, className);
		
		if(printWriter == null)
		{
			return null;
		}
		
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, className);
		
		composer.addImport( "com.google.gwt.http.client.RequestException");
		composer.addImport( "com.google.gwt.json.client.JSONArray");
		composer.addImport( "com.google.gwt.json.client.JSONNumber");
		composer.addImport( "com.google.gwt.json.client.JSONObject");
		composer.addImport( "com.google.gwt.json.client.JSONString");
		composer.addImport( "com.google.gwt.json.client.JSONValue");
		composer.addImport( "com.google.gwt.http.client.RequestBuilder");
		composer.addImport( "net.brownpig.jsonrpc.client.JSONRPCRequestCallback");
		
		
		composer.setSuperclass(JSONRPCServiceBase.class.getName());
		composer.addImplementedInterface(interfaceName);
		
		return composer.createSourceWriter(context, printWriter);
	}
	private void printAsyncMethod( JMethod method )
	{
		String asyncParamName = method.getParameters()[method.getParameters().length-1].getName();
		
		print("public void "+method.getName());
		printAsyncMethodParams(method.getParameters());
		
		println("{");
		
		indent();

		println( "String id = randomUUID();" ); 
		
		println( "JSONRPCRequestCallback requestCallback = new JSONRPCRequestCallback("+asyncParamName+", id );" );
		println( "RequestBuilder builder = createBuilder();");
		
		println( "JSONArray data = new JSONArray();");
		for(int n=0; n<method.getParameters().length-1; n++)
		{
			println( "data.set("+String.valueOf(n)+", "+"serialize("+method.getParameters()[n].getName()+"));");
		}
		println( "JSONObject requestData = createRequestData(\""+method.getName()+"\", data, id );");
		println( "try");
		println( "{");
		indent();
		println( "builder.sendRequest( requestData.toString(), requestCallback );");
		outdent();
		println( "} catch( RequestException e) {");
		indent();
		println( "e.printStackTrace();");
		outdent();
		println("}");
		outdent();
		println("}");
		
	}
	private void createNonNativesTypes(GeneratorContext context, JType type )
	{
		if( !Arrays.asList(nativesTypes).contains(type.getClass().getName()) )
		{
			
		}
	}
	private void printAsyncMethodParams( JParameter[] parameters )
	{
		print("(");
		
		for( int n=0; n<parameters.length; n++)
		{
			if(n>0)
			{
				print(", ");
			}
			printAsyncMethodParam(parameters[n]);
		}
		
		print(")");
	}
	private void printAsyncMethodParam( JParameter parameter )
	{
		print(parameter.getType().getQualifiedSourceName()+" "+parameter.getName());
	}
	private void print( String value )
	{
		sourceWriter.print(value);
	}
	private void println( String value )
	{
		sourceWriter.println( value );
	}
	private String simpleStubClassName( String className )
	{
		return className+"Impl";
	}
	private void indent()
	{
		sourceWriter.indent();
	}
	private void outdent()
	{
		sourceWriter.outdent();
	}
}

package net.brownpig.jsonrpc.client.vo;

import java.util.ArrayList;

public class JSONObjectVO {

	protected ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	
	public Attribute getAttribute(String name)
	{
		for( int n = 0; n< attributes.size(); n++ )
		{
			Attribute item = attributes.get(n);
			if( item.getName().equals(name) )
			{
				return item;
			}
		}
		Attribute attribute = new Attribute();
		attribute.setName(name);
		attributes.add(attribute);
		return attribute;
	}
	public Object getValue(String name)
	{
		return getAttribute(name).getValue();
	}
	public void setValue(String name, Object value )
	{
		getAttribute(name).setValue(value);
	}
	
}

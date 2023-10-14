package javaosc;

import java.util.Enumeration;
import java.util.Vector;

import javaosc.utility.*;

public class OSCMessage extends OSCPacket {

	protected String address;
	protected Vector<Object> arguments;

	public OSCMessage() {
		arguments = new Vector<Object>();
	}

	public OSCMessage(String address) {
		this(address, null);
	}

	public OSCMessage(String address, Object[] arguments) {
		this.address = address;
		if (null != arguments) {
			this.arguments = new Vector<Object>(arguments.length);
			for (int i = 0; i < arguments.length; i++) {
				this.arguments.add(arguments[i]);
			}
		} else {
			this.arguments = new Vector<Object>();
		}
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String anAddress) {
		address = anAddress;
	}
	
	public void addArgument(Object argument) {
		arguments.add(argument);
	}
	
	public void setArguments(Object[] arguments) {
		this.arguments.add(arguments);
	}
		
	public Object[] getArguments() {
		return arguments.toArray();
	}
	
	public Object getArgument(int i) {
		return arguments.get(i);
	}

	protected void computeAddressByteArray(OSCJavaToByteArrayConverter stream) {
		stream.write(address);
	}

	
	protected void computeArgumentsByteArray(OSCJavaToByteArrayConverter stream) {
		stream.write(',');
		if (null == arguments)
			return;
		stream.writeTypes(arguments);
		Enumeration enumerator = arguments.elements();
		while (enumerator.hasMoreElements()) {
			stream.write(enumerator.nextElement());
		}
	}
	
	protected void computeByteArray(OSCJavaToByteArrayConverter stream) {
		computeAddressByteArray(stream);
		computeArgumentsByteArray(stream);
		byteArray = stream.toByteArray();
	}
	
	public String toString() {
		String out = address;
		for(Object arg : arguments) {
			out += " "+arg.toString();
		}
		return out;
	}
}
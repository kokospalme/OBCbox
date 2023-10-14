package javaosc;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;

import java.util.Vector;
import java.util.Date;

import javaosc.utility.OSCByteArrayToJavaConverter;


import java.lang.reflect.Method;

public class OSCPortIn extends OSCPort implements Runnable {

	private boolean isListening;
	
	protected OSCByteArrayToJavaConverter converter = new OSCByteArrayToJavaConverter();

	
	Method[] eventMethod;
	
	Vector<OSCListener> listeners;
	

	
	public OSCPortIn( int port) {
		this.port = port;
		
		listeners = new Vector<OSCListener>();
		
		try {
			socket = new DatagramSocket(port);
			startListening();
		} catch (SocketException e) {
			System.err.println("OSCPortIn couldn't bind to port "+port+".");
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		byte[] buffer = new byte[1536];
		DatagramPacket packet = new DatagramPacket(buffer, 1536);
		while (isListening) {
			try {
				socket.receive(packet);
				OSCPacket oscPacket = converter.convert(buffer, packet.getLength());
				dispatchPacket(oscPacket);
			} catch (Exception e) {
				System.err.println("Error receiving packet on port "+port+".");
				e.printStackTrace();
			}
		}
	}
	
	public void dispatchPacket(OSCPacket packet) {
		dispatchPacket(packet, null);
	}
	
	public void dispatchPacket(OSCPacket packet, Date timestamp) {
		if (packet instanceof OSCBundle) dispatchBundle((OSCBundle) packet);
		else dispatchMessage((OSCMessage) packet, timestamp);
	}
	
	
	private void dispatchBundle(OSCBundle bundle) {
		Date timestamp = bundle.getTimestamp();
		
		OSCPacket[] packets = bundle.getPackets();
		
		for(OSCPacket packet : packets) {
			dispatchPacket(packet, timestamp);
		}
	}
	
	private void dispatchMessage(OSCMessage message) {
		dispatchMessage(message, null);
	}
	
	private void dispatchMessage(OSCMessage message, Date time) {
		for(OSCListener listener : listeners) {
			listener.acceptMessage(time, message, port);
		}
		
//		if(eventMethod[0] != null) {
//			try {
//				eventMethod[0].invoke(parent, new Object[] { message });
//			} catch (Exception e) {
//				System.err.println("\nJavaOSC event method 0 disabled...");
//				e.printStackTrace();
//				eventMethod[0] = null;
//			}
//		}
//		if(eventMethod[1] != null) {
//			try {
//				eventMethod[1].invoke(parent, new Object[] { message, port });
//			} catch (Exception e) {
//				System.err.println("\nJavaOSC event method 1 disabled...");
//				e.printStackTrace();
//				eventMethod[1] = null;
//			}
//		}
//		if(eventMethod[2] != null) {
//			try {
//				eventMethod[2].invoke(parent, new Object[] { message, port, time });
//			} catch (Exception e) {
//				System.err.println("\nJavaOSC event method 2 disabled...");
//				e.printStackTrace();
//				eventMethod[2] = null;
//			}
//		}
	}
	
	public void startListening() {
		isListening = true;
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void stopListening() {
		isListening = false;
	}
	
	public boolean isListening() {
		return isListening;
	}
	
	
	public void addListener(OSCListener listener) {
		listeners.add(listener);
	}
	
	public void close() {
		socket.close();
	}
	
}

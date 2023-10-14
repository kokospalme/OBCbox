package javaosc;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class OSCPortOut extends OSCPort {

	protected InetAddress address;

	public OSCPortOut(InetAddress address, int port) {
		this.port = port;
		
		this.address = address;
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.err.println("Couldn't Create Outbound DatagramSocket");
			e.printStackTrace();
		}
	}
	
	public OSCPortOut(String address, int port) {
		this.port = port;
		
		InetAddress tentativeAddress = null;
		
		try { 
			tentativeAddress = InetAddress.getAllByName(address)[0];
		} catch (UnknownHostException e1) {
			System.err.println("Couldn't get requested host: "+address+". Falling back to localhost");
			e1.printStackTrace();
			try {
				tentativeAddress = InetAddress.getLocalHost();
			} catch (UnknownHostException e2) {
				System.err.println("Couldn't get localhost either");
				e2.printStackTrace();
			}
		}
		
		this.address = tentativeAddress;
	
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.err.println("Couldn't Create Outbound DatagramSocket");
			e.printStackTrace();
		}
	}
	
	public void send(OSCPacket packet) {
		byte[] byteArray = packet.getByteArray();
		try {
			DatagramPacket datapacket = new DatagramPacket(byteArray, byteArray.length, address, port);
			socket.send(datapacket);
		} catch (Exception e) {
			System.err.println("Couldn't send packet");
			e.printStackTrace();
		}		
	}
}

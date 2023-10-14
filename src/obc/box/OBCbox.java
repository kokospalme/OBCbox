package obc.box;
import javaosc.*;

public class OBCbox {
	public static OSCPortOut out;
	public static OSCPortIn in;
	public static String localhost = "localhost";
	public static int outPort = 17002;
	public static int inPort = 2014;
	
	public static void main(String[] args) {
		out = new OSCPortOut(localhost, outPort);	//open output port
		System.out.println("port for output established.");
		in = new OSCPortIn(inPort);	//open input port
		System.out.println("port for input established.");
		
		try {	//wait for a moment before sending a message
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object[] oscArgs = {"list", inPort};	//format a /master list Port message
		OSCMessage msg = new OSCMessage("/master", oscArgs);
		out.send(msg);	//send msg 
		
		System.out.println("msg sent");
		
		try {	//wait another while before stopping the 
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		in.close();
		System.out.println("socket closed");

	}

}

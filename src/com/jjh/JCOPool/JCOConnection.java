package com.jjh.JCOPool;

import java.util.Properties;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

public class JCOConnection {
	private static JCoDestination dest;
	private static Properties prop;
	
	public static void printJCOConnectionAttributes(JCoDestination dest) throws JCoException {
        System.out.println("Attributes:");
        System.out.println(dest.getAttributes());
        System.out.println();
	}
	
	public JCOConnection(String destinationName)  {
    	try {
			dest = JCoDestinationManager.getDestination(destinationName);
	    	printJCOConnectionAttributes(dest);
		} catch (JCoException e) {
			e.printStackTrace();
		}
	}
	
	public JCoDestination getJCOConnection() {
		return dest;
	}
}

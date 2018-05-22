package tutorial;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tam
 */
import org.apache.thrift.TException;
import tutorial.MultiplicationService;

public class MultiplicationHandler implements MultiplicationService.Iface {

	 public int multiply(int n1, int n2) throws TException {
             int result = n1*n2;    
             System.out.println("Multiply(" + n1 + "," + n2 + ")=" + result);
             return result;
	 }

	
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddTool;

import static AddTool.TServerDemo.count;
//import static AddTool.TThreadPoolServerDemo.begin;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author tam
 */
public class AddServiceImpl implements AddService.Iface{
    @Override
    public synchronized int Add(int n1, int n2) throws TException {
        count ++;
//        long thisTime = Calendar.getInstance().getTimeInMillis();
//      System.out.println("(request "+ count+"):"+n1 + " + " + n2);
//        if(count>15000)
//            System.out.println("Request >15.000");
//        if((thisTime-begin)>=1000){
//            begin = thisTime;
//           count = 0;
//        }
        return n1 + n2;
    }

    @Override
    public synchronized void Ping() throws TException {
        //count++;
        while(true){
            System.out.println("Ping");
        }
    }

    
    
}

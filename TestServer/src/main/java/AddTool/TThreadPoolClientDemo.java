/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddTool;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TFileTransport;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author tam
 */
public class TThreadPoolClientDemo {
    public static String SERVER_IP = "127.0.0.1";
    public static int SERVER_PORT = 9090;
    public static int TIMEOUT = 30000;
    public static int number = 0;
    public static int send = 0;
    public static int recv = 0;

    public void startClient(){
            for (int i = 0; i < 10; i++) {
                number ++;
                threadClient thread = new threadClient(number);
                thread.start();
//            while(true){                
//            int result = client.Add(2, 3);
//            System.out.println("2 + 3 = "+result); 
            //}               
            }                    
        }
        
    
    public static void main(String[] args) throws TTransportException, TException{
        TThreadPoolClientDemo client = new TThreadPoolClientDemo();
        client.startClient();
        
    }
    
    public class threadClient extends Thread{
        private int _number;
        
        public threadClient(int number){
            _number = number;
        }
        
        @Override
        public void run(){
            try {                
            TTransport transport = new TFastFramedTransport(new TSocket(SERVER_IP,SERVER_PORT,TIMEOUT));
            TProtocol tprotocol = new TBinaryProtocol(transport);
            AddService.Client client = new AddService.Client(tprotocol);
                transport.open();
                while(true){
                    int result = client.Add(_number, _number);
                    System.out.println(result);
                }                
            } catch (TException ex) {
                Logger.getLogger(TThreadPoolClientDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

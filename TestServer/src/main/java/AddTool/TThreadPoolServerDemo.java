/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddTool;

import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

/**
 *
 * @author tam
 */
public class TThreadPoolServerDemo {
    public static int SERVER_PORT = 9090;    
    public static int count = 0;
//    public static long begin = Calendar.getInstance().getTimeInMillis();
    public static int MIN_WORKER = 20;
//    public static int MAX_WORKER = 100000;
    
    public void startServer(){
        try {
            TProcessor tprocessor = new AddService.Processor<AddService.Iface>(new AddServiceImpl());
      
            TServerSocket tnbSocketTransport = new TServerSocket(new InetSocketAddress("localhost", count));         
            TThreadPoolServer.Args thsArgs = new TThreadPoolServer.Args(tnbSocketTransport);
            thsArgs.processor(tprocessor);
            thsArgs.transportFactory(new TFastFramedTransport.Factory());
            thsArgs.protocolFactory(new TBinaryProtocol.Factory());
            //thsArgs.selectorThreads(5);
            //thsArgs.workerThreads(20);
            thsArgs.minWorkerThreads(MIN_WORKER);
            //thsArgs.maxWorkerThreads(MAX_WORKER);
            TServer server = new TThreadPoolServer(thsArgs);
        
//            TProcessor tprocessor = new AddService.Processor<AddService.Iface>(new AddServiceImpl());
//
//            TServerSocket serverTransport = new TServerSocket(9090);
//            TThreadPoolServer.Args ttpsArgs = new TThreadPoolServer.Args(serverTransport);
//            //ttpsArgs.minWorkerThreads(10);
//            ttpsArgs.processor(tprocessor);
//            ttpsArgs.protocolFactory(new TCompactProtocol.Factory());
//            ttpsArgs.transportFactory(new TFastFramedTransport.Factory());
//                TServer server = new TThreadPoolServer(ttpsArgs);
//                
            Count countReq = new Count();
            countReq.start();
            System.out.println("Server is running....");
            server.serve();
        } catch (TTransportException ex){
            Logger.getLogger(TThreadPoolServerDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        TThreadPoolServerDemo server = new TThreadPoolServerDemo();
        server.startServer();
    }
    
    public class Count extends Thread{
        public Count(){
        }
        
        @Override
        public void run(){
            while(true){
                System.out.println(count);
                count = 0;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TThreadPoolServerDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}

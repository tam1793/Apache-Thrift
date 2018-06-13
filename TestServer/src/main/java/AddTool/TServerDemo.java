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
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

/**
 *
 * @author tam
 */
public class TServerDemo {
    enum typeServer{
        TThreadPoolServer,
        TNonblockingServer,
        THsHaServer,
        TThreadedSelectorServer        
    }
    public static int count = 0;
    private int SERVER_PORT = 9090;
    private int TIME_OUT = 30000;
    private int MIN_WORKER = 5;
    private typeServer type = typeServer.TThreadPoolServer;
    
    
    public void startServer(){
        try {
            TProcessor tprocessor = new AddService.Processor<AddService.Iface>(new AddServiceImpl());
            Count countReq = new Count();
            countReq.start();
            switch(type){
                case TThreadPoolServer://TThreadpoolServer
                    TServerTransport thpTransport = new TServerSocket(new InetSocketAddress("localhost", SERVER_PORT),TIME_OUT);         
                    TThreadPoolServer.Args thpArgs = new TThreadPoolServer.Args(thpTransport);
                    thpArgs.processor(tprocessor);
                    thpArgs.transportFactory(new TFramedTransport.Factory());
                    thpArgs.protocolFactory(new TBinaryProtocol.Factory());
                    thpArgs.minWorkerThreads = MIN_WORKER ;
                    TServer thpServer = new TThreadPoolServer(thpArgs);
                    System.out.println("TThreadPoolServer is running....");
                    thpServer.serve();
                    break;
                case TNonblockingServer://TNonblocingServer
                    TNonblockingServerTransport nbTransport = new TNonblockingServerSocket(new InetSocketAddress("localhost", SERVER_PORT),TIME_OUT);         
                    TNonblockingServer.Args nbArgs = new TNonblockingServer.Args(nbTransport);
                    nbArgs.processor(tprocessor);
                    nbArgs.transportFactory(new TFramedTransport.Factory());
                    nbArgs.protocolFactory(new TBinaryProtocol.Factory());
                    TServer nbServer = new TNonblockingServer(nbArgs);
                    System.out.println("TNonblockingServer is running....");
                    nbServer.serve();
                    break;
                case THsHaServer://THsHaServer
                    TNonblockingServerTransport hshaTransport = new TNonblockingServerSocket(SERVER_PORT,TIME_OUT);         
                    THsHaServer.Args hshaArgs = new THsHaServer.Args(hshaTransport);
                    hshaArgs.processor(tprocessor);
                    hshaArgs.transportFactory(new TFramedTransport.Factory());
                    hshaArgs.protocolFactory(new TBinaryProtocol.Factory());
                   hshaArgs.minWorkerThreads = MIN_WORKER;
                    hshaArgs.maxWorkerThreads = 16;
                    TServer hshaServer = new THsHaServer(hshaArgs);
                    System.out.println("THsHaServer is running....");
                    hshaServer.serve();
                    break;
                case TThreadedSelectorServer://TTHreadedSelectorServer
//                    TNonblockingServerSocket thsTransport = new TNonblockingServerSocket(new InetSocketAddress("localhost", SERVER_PORT),TIME_OUT);         
//                    TThreadedSelectorServer.Args thsArgs = new TThreadedSelectorServer.Args(thsTransport);
//                    thsArgs.processor(tprocessor);
//                    thsArgs.transportFactory(new TFramedTransport.Factory());
//                    thsArgs.protocolFactory(new TCompactProtocol.Factory());
//                    thsArgs.selectorThreads(8);
//                    thsArgs.workerThreads(16);
//                    TServer thsServer = new TThreadedSelectorServer(thsArgs);       
//                    System.out.println("TThreadedSelectorServerServer is running....");
//                    thsServer.serve();
                    TNonblockingServerTransport trans = new TNonblockingServerSocket(SERVER_PORT);
                    TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(trans);
                    args.transportFactory(new TFramedTransport.Factory());
                    args.protocolFactory(new TBinaryProtocol.Factory());
                    args.selectorThreads(8);
                    args.workerThreads(16);
                    args.processor(tprocessor);
                    args.maxReadBufferBytes = 16777216; // 16MB
                    TServer server = new TThreadedSelectorServer(args);
                    System.out.println("TThreadedSelectorServerServer is running....");
                    server.serve();

                    
            }
                          
        } catch (TTransportException ex){
            Logger.getLogger(TServerDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        TServerDemo server = new TServerDemo();
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
                    Logger.getLogger(TServerDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddTool;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author tam
 */
public class Server {
    public static int SERVER_PORT = 9090;
    public static int MIN_WORKER = 20;
    
    public static int count = 0;
    
    public void startServer() throws TTransportException{
        
        TProcessor tprocessor = new AddService.Processor<AddService.Iface>(new AddServiceImpl());
      
        TServerSocket tnbSocketTransport = new TServerSocket(SERVER_PORT);         
        TThreadPoolServer.Args thsArgs = new TThreadPoolServer.Args(tnbSocketTransport);
        thsArgs.processor(tprocessor);
        thsArgs.transportFactory(new TFastFramedTransport.Factory());
        thsArgs.protocolFactory(new TBinaryProtocol.Factory());
        thsArgs.minWorkerThreads(MIN_WORKER);
        TServer server = new TThreadPoolServer(thsArgs);
        
        System.out.println("TThreadPoolServer is running...");
        server.serve();
    }
    
    public static void main(String[] args) throws TTransportException{
        Server server = new Server();
        server.startServer();
    }
    
}

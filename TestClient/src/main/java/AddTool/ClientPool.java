/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddTool;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author tam
 */
public class ClientPool<T extends TServiceClient> implements AutoCloseable{

    private final GenericObjectPool<T> internalPool;
    
    public ClientPool(ClientFactory<T> clientFactory,
                      GenericObjectPool.Config poolConfig,String IP,int PORT){
        
        ThriftClientFactory factory = new ThriftClientFactory(clientFactory,new ProtocolFactory(IP, PORT));
        this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
    }
    
    
        class ThriftClientFactory extends BasePoolableObjectFactory{
            
            private ClientFactory<T> clientFactory;
            private ProtocolFactory protocolFactory;
            
        public ThriftClientFactory(ClientFactory<T> clientFactory,ProtocolFactory protocolFactory) {
            this.clientFactory = clientFactory;
            this.protocolFactory = protocolFactory;
        }

        @Override
        public T makeObject() throws Exception {
            // create client
            TProtocol protocol = protocolFactory.createProtocol();
            return clientFactory.createClient(protocol);
        }
        
        
        public void destroyObject(T obj){
            if (obj.getOutputProtocol().getTransport().isOpen()) {
                obj.getOutputProtocol().getTransport().close();
            }
            if (obj.getInputProtocol().getTransport().isOpen()) {
                obj.getInputProtocol().getTransport().close();
            }        
        }
    }
    
    @Override
    public void close() throws Exception {
        internalPool.close();
    }

    public static interface ClientFactory<T> {

        T createClient(TProtocol tProtocol);
    }

    private static class ProtocolFactory {

        private String IP;
        private int PORT;
    public ProtocolFactory(String IP,int PORT) {
        this.IP = IP;
        this.PORT = PORT;
        }
    public TProtocol createProtocol() throws TTransportException{
        TTransport transport = new TFastFramedTransport(new TSocket(IP, PORT));
        transport.open();
        return new TBinaryProtocol(transport);        
    }
    }
    
    public T getClient() throws Exception{
        return internalPool.borrowObject();
    }
    
    public void returnClient(T client) throws Exception{
        internalPool.returnObject(client);
    }
    
}

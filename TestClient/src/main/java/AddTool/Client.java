/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddTool;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import AddTool.ClientPool;
import AddTool.ClientPool.ClientFactory;
import AddTool.ClientPool.ThriftClientFactory;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.protocol.TProtocol;

/**
 *
 * @author tam
 */
public class Client {
    
    private static String HOST = "localhost";
    private static int PORT = 9090;
    private static int MAX_THREAD = 5;
    private static ClientPool<AddService.Client> pool;
    
    public static void main(String[] args){
        
        Config poolConfig = new Config();
        
        poolConfig.maxActive = 3;
       // poolConfig.minIdle = 5;
       // poolConfig.maxIdle = 10;
        poolConfig.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
        poolConfig.maxWait = 5000000;
        poolConfig.testOnBorrow = true;
        poolConfig.testOnReturn = true;
        
        pool = new ClientPool<AddService.Client>(
                new ClientFactory<AddService.Client>() {
            @Override
            public AddService.Client createClient(TProtocol tProtocol) {
                return new AddService.Client(tProtocol);
            }
        },poolConfig,HOST,PORT);
        
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREAD);
        
        for (int i = 0; i < 5; i++) {
            executor.submit(new ClientThread(i));
        }
            

    }

    static class ClientThread implements Runnable{
        
        int inThread;
        public ClientThread(int inThread){
            this.inThread = inThread;
        }
        @Override
        public void run() {
            try {
                while(true){
                    AddService.Client client = pool.getClient();
                
                    for (int i = 0; i < 10; i++) {
                        //client.Ping();
                        this.inThread = client.Add(inThread, 0);
                        System.out.println("Thread "+ inThread+" lan "+ i);
                        Thread.sleep(50);
                    }
                    pool.returnClient(client);
                }
                
                
              
            } catch (Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }

    
}

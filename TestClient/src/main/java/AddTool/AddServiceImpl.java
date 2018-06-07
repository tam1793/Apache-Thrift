/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddTool;

import org.apache.thrift.TException;
import static AddTool.Server.count;
/**
 *
 * @author tam
 */
public class AddServiceImpl implements AddService.Iface {

    public AddServiceImpl() {
    }

    @Override
    public int Add(int n1, int n2) throws TException {
        return n1 + n2 ;
    }

    @Override
    public void Ping() throws TException {
        count ++;
    }
    
}

package com.jjh.JCOPool;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;

public class PoolTest 
{
    static String ABAP_AS = "ABAP_AS_WITHOUT_POOL";  //�ƹ��� �ǹ� ���� �׳� ���ϴ� �̸�

    //����Ŭ ó�� ���� ���ڸ� �޾Ƽ� SQL�� ó�� �ϴ� �κ�
    //�Լ�(Procedure)����̹Ƿ� ���� �Լ��� �����ϰ� ��.
    //SAP�� �⺻������ �׻� �ִ� ���� �Լ� ��. (����Ŭ employer ���̺� ó�� �⺻������ �ִ� ������ ���̺�)
    /**
     * The following example executes a simple RFC function STFC_CONNECTION.
     * In contrast to JCo 2 you do not need to take care of repository management. 
     * JCo 3 manages the repository caches internally and shares the available
     * function metadata as much as possible. 
     * @throws JCoException
     */
    public static void SimpleCall(JCoDestination destination) throws JCoException
    {
        //JCoDestination is the logic address of an ABAP system and ...
//        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        // ... it always has a reference to a metadata repository
        JCoFunction function = destination.getRepository().getFunction("STFC_CONNECTION");
        if(function == null) {
            throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
        }

        //JCoFunction is container for function values. Each function contains separate
        //containers for import, export, changing and table parameters.
        //To set or get the parameters use the APIS setValue() and getXXX(). 
        function.getImportParameterList().setValue("REQUTEXT", "Hello SAP");    //<-- ���ڰ� ����
        
        try
        {
            //execute, i.e. send the function to the ABAP system addressed 
            //by the specified destination, which then returns the function result.
            //All necessary conversions between Java and ABAP data types
            //are done automatically.
            function.execute(destination);                            //<-- ����
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
        }
        
        System.out.println("STFC_CONNECTION finished:");
        System.out.println(" Echo: " + function.getExportParameterList().getString("ECHOTEXT"));       //<-- ����� ����
        System.out.println(" Response: " + function.getExportParameterList().getString("RESPTEXT"));
        System.out.println();
    }
    
	public static void main(String[] args) throws Exception {
		GenericObjectPool<JCOConnection> genericObjectPool = new GenericObjectPool<JCOConnection>(new JCOConnectionPoolFactory());
		
		for(int i = 0; i < 10; i++) { 
			JCOConnection obj = genericObjectPool.borrowObject();

			System.out.println(obj);
			JCoDestination dest = obj.getJCOConnection();
			System.out.println("i : " + i); 
			System.out.println(obj.getClass().getSimpleName()+ "connection : " );
			JCOConnection.printJCOConnectionAttributes(dest);
			
			SimpleCall(dest);
			genericObjectPool.returnObject(obj); 
		}
	}
}

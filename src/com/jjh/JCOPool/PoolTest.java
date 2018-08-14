package com.jjh.JCOPool;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;

public class PoolTest 
{
    static String ABAP_AS = "ABAP_AS_WITHOUT_POOL";  //아무런 의미 없음 그냥 정하는 이름

    //오라클 처럼 연결 인자를 받아서 SQL을 처리 하는 부분
    //함수(Procedure)방식이므로 먼저 함수를 선택하게 됨.
    //SAP에 기본적으로 항상 있는 예제 함수 임. (오라클 employer 테이블 처럼 기본적으로 있는 연습용 테이블)
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
        function.getImportParameterList().setValue("REQUTEXT", "Hello SAP");    //<-- 인자값 삽입
        
        try
        {
            //execute, i.e. send the function to the ABAP system addressed 
            //by the specified destination, which then returns the function result.
            //All necessary conversions between Java and ABAP data types
            //are done automatically.
            function.execute(destination);                            //<-- 실행
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
        }
        
        System.out.println("STFC_CONNECTION finished:");
        System.out.println(" Echo: " + function.getExportParameterList().getString("ECHOTEXT"));       //<-- 결과값 추출
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

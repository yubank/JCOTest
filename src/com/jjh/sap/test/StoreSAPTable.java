package com.jjh.sap.test;
/**
 * ���� ���� ��  
 *   1. config ������ �����Ͽ� �α��� ������ �ҽ��� ������ �ʰ� ó����. ���� ���� config ���� ���� �κи� ���� ����� �� �� ��.
 *   2. ���� RFC�� �ִ� �Լ��� ȣ�� �� �� ��� ��..  ���� �Ե� �˷�̴��� �ִ� ���̺��� ������ ��.
 * 
 */


import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.List;
import java.util.Map;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.*;


public class StoreSAPTable {
	final static String JCO_CFG_FILE_NAME = "ABAP_AS_WITHOUT_POOL";
	final static String JCO_CFG_EXTENTION = ".jcoDestination";
	static List<Map<String, Object>> list = null; // ��ȸ�� �����͸� ���� ����Ʈ
	static String ABAP_AS = "ABAP_AS_WITHOUT_POOL"; // sap �����(�������ϸ����� ����)
	
	public static File isConfigFileExist(String destinationName) {
		File destCfg = new File(destinationName + JCO_CFG_EXTENTION);
		if (!destCfg.exists()) {
			return destCfg;
		} else {
			return null;
		}
	}

	// sap �������� ����
	static void createDestinationDataFile(String destinationName, Properties connectProperties)
	{
		File destCfg = new File(destinationName + ".jcoDestination");

		if (!destCfg.exists()) {
			try
			{
				FileOutputStream fos = new FileOutputStream(destCfg, false);
				connectProperties.store(fos, "PDA USER JCO config");
				fos.close();
			}
			catch (Exception e)
			{
				throw new RuntimeException("Unable to create the destination files", e);
			}
		}
	}

	public static void getTableTest() throws JCoException
	{
		System.out.println("���̺������� ����");

		JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);

		// ��������Ȯ��.
		System.out.println("Attributes:");
		System.out.println(destination.getAttributes());
		System.out.println();

		// ����Ʈ ���(?) ��ư ��Ǹ����� ȣ��
		JCoFunction function = destination.getRepository().getFunction("ZSDA_008_SEND_WEB_INFOACCT");
        
		if (function == null) {
			throw new RuntimeException("SAP_DATA not found in SAP.");
		} else {
	        function.getImportParameterList().setValue("IV_KUNNR", "10074");			
		}
		
		try
		{
			function.execute(destination);
			System.out.println("����Ϸ�::!!");
		}
		catch (AbapException e)
		{
			System.out.println(e.toString());
			return;
		}

		// ��ǿ��� ���̺� ȣ��
		JCoTable codes = function.getTableParameterList().getTable("IT_MMSTAB");
//		list = new ArrayList<Map<String, Object>>();

		// �������鼭 ������ ��ȸ
		for (int i = 0; i < codes.getNumRows(); i++)
		{
			codes.setRow(i);

//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("NAME1", codes.getString("NAME1"));
			System.out.println(String.format("NAME1:%s", codes.getString("NAME1")));
/*			map.put("�÷�2", codes.getString("�÷�2"));
			map.put("�÷�3", codes.getString("�÷�3"));
*/			// ����Ʈ�� ��Ƽ� ���

//			list.add(map);
		}
	}

	public static void main(String[] args) throws JCoException {
		System.out.println("����");

		// ����������Ƽ ����
		Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "10.5.1.18"); // SAP ȣ��Ʈ ����
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "00");        // �ν��Ͻ���ȣ
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "900");       // SAP Ŭ���̾�Ʈ
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "PDAUSER");   // SAP������
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "init01");    // SAP �н�����
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "KO");        // ���
		// ������Ƽ�� �̿��Ͽ� ���������� ����.

		// ����ǰ� �ִ� ����ý��� ��ο� ������.
		createDestinationDataFile(ABAP_AS, connectProperties);
		getTableTest();
	}
}

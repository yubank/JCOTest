package com.jjh.sap.test;
/**
 * 실전 예제 임  
 *   1. config 파일을 설정하여 로그인 정보를 소스에 남기지 않게 처리함. 실제 사용시 config 파일 생성 부분만 따로 떼어야 할 듯 함.
 *   2. 실제 RFC에 있는 함수를 호출 해 본 결과 임..  실제 롯데 알루미늄에 있는 테이블을 가져와 봄.
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
	static List<Map<String, Object>> list = null; // 조회된 데이터를 담을 리스트
	static String ABAP_AS = "ABAP_AS_WITHOUT_POOL"; // sap 연결명(연결파일명으로 사용됨)
	
	public static File isConfigFileExist(String destinationName) {
		File destCfg = new File(destinationName + JCO_CFG_EXTENTION);
		if (!destCfg.exists()) {
			return destCfg;
		} else {
			return null;
		}
	}

	// sap 연결파일 생성
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
		System.out.println("테이블가져오기 실행");

		JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);

		// 연결정보확인.
		System.out.println("Attributes:");
		System.out.println(destination.getAttributes());
		System.out.println();

		// 리모트 펑션(?) 암튼 펑션명으로 호출
		JCoFunction function = destination.getRepository().getFunction("ZSDA_008_SEND_WEB_INFOACCT");
        
		if (function == null) {
			throw new RuntimeException("SAP_DATA not found in SAP.");
		} else {
	        function.getImportParameterList().setValue("IV_KUNNR", "10074");			
		}
		
		try
		{
			function.execute(destination);
			System.out.println("실행완료::!!");
		}
		catch (AbapException e)
		{
			System.out.println(e.toString());
			return;
		}

		// 펑션에서 테이블 호출
		JCoTable codes = function.getTableParameterList().getTable("IT_MMSTAB");
//		list = new ArrayList<Map<String, Object>>();

		// 루프돌면서 데이터 조회
		for (int i = 0; i < codes.getNumRows(); i++)
		{
			codes.setRow(i);

//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("NAME1", codes.getString("NAME1"));
			System.out.println(String.format("NAME1:%s", codes.getString("NAME1")));
/*			map.put("컬럼2", codes.getString("컬럼2"));
			map.put("컬럼3", codes.getString("컬럼3"));
*/			// 리스트에 담아서 사용

//			list.add(map);
		}
	}

	public static void main(String[] args) throws JCoException {
		System.out.println("시작");

		// 연결프로퍼티 생성
		Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "10.5.1.18"); // SAP 호스트 정보
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "00");        // 인스턴스번호
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "900");       // SAP 클라이언트
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "PDAUSER");   // SAP유저명
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "init01");    // SAP 패스워드
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "KO");        // 언어
		// 프로퍼티를 이용하여 연결파일을 생성.

		// 실행되고 있는 응용시스템 경로에 생성됨.
		createDestinationDataFile(ABAP_AS, connectProperties);
		getTableTest();
	}
}

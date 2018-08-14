package com.jjh.JCOPool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class JCOConnectionPoolFactory extends BasePooledObjectFactory<JCOConnection>
{
	@Override
	public PooledObject<JCOConnection> makeObject() {
		JCOConnection jcc = new JCOConnection("ABAP_AS_WITHOUT_POOL");
		return new DefaultPooledObject<JCOConnection>(jcc); 
	}
	
	@Override
	public JCOConnection create() throws Exception {
		// TODO Auto-generated method stub
		return new JCOConnection("ABAP_AS_WITHOUT_POOL");
	}

	@Override
	public PooledObject<JCOConnection> wrap(JCOConnection jcc) {
		return new DefaultPooledObject<JCOConnection>(jcc);
	}
}

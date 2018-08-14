package com.jjh.JCOPool;

import java.io.PrintWriter;
import java.util.Deque;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectState;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.sap.conn.jco.JCoException;

public class JCOConnectionPool implements PooledObject<JCOConnection> {

	@Override
	public boolean allocate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int compareTo(PooledObject<JCOConnection> arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deallocate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean endEvictionTest(Deque<PooledObject<JCOConnection>> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getActiveTimeMillis() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCreateTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getIdleTimeMillis() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLastBorrowTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLastReturnTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLastUsedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JCOConnection getObject(){
		// TODO Auto-generated method stub
		return new JCOConnection("ABAP_AS_WITHOUT_POOL");
	}

	@Override
	public PooledObjectState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markAbandoned() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markReturning() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printStackTrace(PrintWriter arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLogAbandoned(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean startEvictionTest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void use() {
		// TODO Auto-generated method stub
		
	}
}

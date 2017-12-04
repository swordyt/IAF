package com.swordyt.datadriver;

import java.lang.reflect.Method;

import com.swordyt.core.DataDriver;

public class XmlDriver extends DataDriver {
	private int i=0;
	public XmlDriver(Method method) {
	}

	public boolean hasNext() {
		
		return i++<20;
	}

	public Object[] next() {
		// TODO Auto-generated method stub
		return new String[]{i+""};
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void remove() {
		// TODO Auto-generated method stub
		
	}

}

package com.yinting.core.datadriver;

public enum ExcelHeader implements ExcelLayout{
	Description(0),Run(1),Url(2),Method(3),MerchantId(4),Source(5),DeviceId(6),UserId(7),TokenId(8);
	private final int ordinal;
	private final String name;
	ExcelHeader(int size) {
		this.ordinal = size;
		this.name=this.name();
	}
	public int getOrdinal() {
		return ordinal;
	}

	public String getName() {
		return this.name();
	}
}

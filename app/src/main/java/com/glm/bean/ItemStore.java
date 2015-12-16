package com.glm.bean;

import java.util.Date;
/**
 * identifica una gara virtuale
 * */
public class ItemStore {
	private String sMsKu="";
	private int iVirtualRaceID;
	private String sName="";
	private String sDesc="";
	private String sDesc1="";
	private String fPrice="";
	private int iType=0;
	private int iDifficult=0;
	private Date dStart;
	/**
	 * @return the iVirtualRaceID
	 */
	public synchronized int getiVirtualRaceID() {
		return iVirtualRaceID;
	}
	/**
	 * @param iVirtualRaceID the iVirtualRaceID to set
	 */
	public synchronized void setiVirtualRaceID(int iVirtualRaceID) {
		this.iVirtualRaceID = iVirtualRaceID;
	}
	/**
	 * @return the sName
	 */
	public synchronized String getsName() {
		return sName;
	}
	/**
	 * @param sName the sName to set
	 */
	public synchronized void setsName(String sName) {
		this.sName = sName;
	}
	/**
	 * @return the iType
	 */
	public synchronized int getiType() {
		return iType;
	}
	/**
	 * @param iType the iType to set
	 */
	public synchronized void setiType(int iType) {
		this.iType = iType;
	}
	/**
	 * @return the iDifficult
	 */
	public synchronized int getiDifficult() {
		return iDifficult;
	}
	/**
	 * @param iDifficult the iDifficult to set
	 */
	public synchronized void setiDifficult(int iDifficult) {
		this.iDifficult = iDifficult;
	}
	/**
	 * @return the dStart
	 */
	public synchronized Date getdStart() {
		return dStart;
	}
	/**
	 * @param dStart the dStart to set
	 */
	public synchronized void setdStart(Date dStart) {
		this.dStart = dStart;
	}
	/**
	 * @return the fPrice
	 */
	public synchronized String getfPrice() {
		return fPrice;
	}
	/**
	 * @param fPrice the fPrice to set
	 */
	public synchronized void setfPrice(String fPrice) {
		this.fPrice = fPrice;
	}
	/**
	 * @return the sDesc
	 */
	public synchronized String getsDesc() {
		return sDesc;
	}
	/**
	 * @param sDesc the sDesc to set
	 */
	public synchronized void setsDesc(String sDesc) {
		this.sDesc = sDesc;
	}
	/**
	 * @return the sDesc1
	 */
	public synchronized String getsDesc1() {
		return sDesc1;
	}
	/**
	 * @param sDesc1 the sDesc1 to set
	 */
	public synchronized void setsDesc1(String sDesc1) {
		this.sDesc1 = sDesc1;
	}
	/**
	 * @return the sMsKu
	 */
	public synchronized String getsMsKu() {
		return sMsKu;
	}
	/**
	 * @param sMsKu the sMsKu to set
	 */
	public synchronized void setsMsKu(String sMsKu) {
		this.sMsKu = sMsKu;
	}
}

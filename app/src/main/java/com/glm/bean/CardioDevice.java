package com.glm.bean;

import android.bluetooth.BluetoothDevice;

public class CardioDevice {
	private String sDeviceName;
	private String sDeviceAddress;
	private BluetoothDevice oDeviceBluetooth;
	public synchronized String getsDeviceName() {
		return sDeviceName;
	}
	public synchronized void setsDeviceName(String sDeviceName) {
		this.sDeviceName = sDeviceName;
	}
	public synchronized String getsDeviceAddress() {
		return sDeviceAddress;
	}
	public synchronized void setsDeviceAddress(String sDeviceAddress) {
		this.sDeviceAddress = sDeviceAddress;
	}
	public synchronized BluetoothDevice getoDeviceBluetooth() {
		return oDeviceBluetooth;
	}
	public synchronized void setoDeviceBluetooth(BluetoothDevice oDeviceBluetooth) {
		this.oDeviceBluetooth = oDeviceBluetooth;
	}
	
}

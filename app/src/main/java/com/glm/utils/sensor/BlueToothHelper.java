package com.glm.utils.sensor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.glm.bean.CardioDevice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BlueToothHelper {
	private double lastHeartRate = 0;
	private double heartRate = 0;
	private BluetoothDevice oDevice;
	private ArrayList<CardioDevice> aCardioDevice;
	private boolean bConnect=false;
	private BluetoothAdapter oBluetoothAdapter;
	

	//private Handler oHandler;
	private ConnectThread oConnectThread;
	private ConnectedThread oConnectedThread;
	private boolean bReadCardio=false;
	
	 // Message types sent from the BluetoothSenorService Handler
	 public static final int MESSAGE_STATE_CHANGE = 1;
	 public static final int MESSAGE_READ = 2;
	 public static final int MESSAGE_WRITE = 3;
	 public static final int MESSAGE_DEVICE_NAME = 4;
	
	private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	/**
	 * Costruttore che inizializza gli oggetti BlueTooth
	 * 
	 * */
	public BlueToothHelper(){
		oBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		//oHandler		  = new Handler();
		aCardioDevice	  = new ArrayList<CardioDevice>();
	}
	/**
	 * Ritorna se il dispositivo supporta il bluetooth
	 * 
	 * 
	 * */
	public boolean isBluetoothAvail(){
		
		if (oBluetoothAdapter == null) {
			//Log.v(this.getClass().getCanonicalName(),"BlueTooth not supported");
		    return false;
		}	
		return true;
	}
	/**
	 * Risorna lo stato del BlueTooth 
	 * 
	 * */
	public boolean isBlueToothEnabled(){
		if(oBluetoothAdapter!=null){
			if (!oBluetoothAdapter.isEnabled()) {
				//Log.v(this.getClass().getCanonicalName(),"BlueTooth not enabled");
			    /*Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);*/
				return false;
			}
			return true;
		}
		return false;
	}
	/**
	 * Cerca per dispositivi associati al CELL.
	 * 
	 * 
	 * */
	public void searchPairedDevice(){
		Set<BluetoothDevice> pairedDevices = oBluetoothAdapter.getBondedDevices();
		Log.v("oBTHelper","pairedDevices: "+pairedDevices.size());
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		    	if(device!=null){
		    		if(device.getBondState()!=BluetoothDevice.BOND_NONE){
		    			CardioDevice oCardio = new CardioDevice();
			    		oCardio.setsDeviceName(device.getName());
			    		oCardio.setsDeviceAddress(device.getAddress());
			    		oCardio.setoDeviceBluetooth(device);
			    		aCardioDevice.add(oCardio);
			    		oCardio=null;
		    		}		    		
		    	}
		    	
		    }
		}
	}
	/**Ritorna i nomi dispositivi associati*/
	public synchronized ArrayList<CardioDevice> getaCardioName() {
		return aCardioDevice;
	}
	
	
	/**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect() {
    	if(oDevice!=null){
	        Log.d("oBTHelper", "connect to: " + oDevice.getAddress());
	        bReadCardio=true;
	        // Cancel any thread attempting to make a connection
	        //if (mState == STATE_CONNECTING) {
	        //    if (oConnectThread != null) {oConnectThread.cancel(); oConnectThread = null;}
	        //}
	
	        // Cancel any thread currently running a connection
	        if (oConnectedThread != null) {oConnectedThread.cancel(); oConnectedThread = null;}
	        
	        // Start the thread to connect with the given device
	        oConnectThread = new ConnectThread(oBluetoothAdapter.getRemoteDevice(oDevice.getAddress()));
	        oConnectThread.start();
    	}
    }

    public synchronized int getHeartRate(){
    	if(heartRate==0) return (int) lastHeartRate;
    	else return (int) heartRate;
    }
    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device) {
        Log.d("oBTHelper", "connected, Socket connected");

        // Cancel the thread that completed the connection
        if (oConnectThread != null) {oConnectThread.cancel(); oConnectThread = null;}

        // Cancel any thread currently running a connection
        if (oConnectedThread != null) {oConnectedThread.cancel(); oConnectedThread = null;}

        // Start the thread to manage the connection and perform transmissions
        oConnectedThread = new ConnectedThread(socket);
        oConnectedThread.start();     
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        Log.d("oBTHelper", "stop");

        if (oConnectThread != null) {
            oConnectThread.cancel();
            oConnectThread.interrupt();
            oConnectThread = null;
        }

        if (oConnectedThread != null) {
            oConnectedThread.cancel();
            oConnectedThread.interrupt();
            oConnectedThread = null;
        }     
    }	
	
	/**
	 * 
	 * THREAD DI CONNESSIONE BLUETOOTH 
	 * 
	 * 
	 * **/
	private class ConnectThread extends Thread {
	    private BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	 
	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
	            
	        } catch (IOException e) { 
	        	 Log.e(this.getClass().getCanonicalName(), "ConnectThread Failed");	 
	        	 e.printStackTrace();
	        	 //Reconnect to socket
	        	 //oConnectThread = new ConnectThread( oBluetoothAdapter.getRemoteDevice(device.getAddress()));
	             //oConnectThread.start();
	        }		
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	        
	        try {
	        	Log.d(this.getClass().getCanonicalName(), "oBluetoothAdapter.isDiscovering() "+oBluetoothAdapter.isDiscovering());	
	        	oBluetoothAdapter.cancelDiscovery();
	        	while(oBluetoothAdapter.isDiscovering()){
	        		try {
	        			Log.v(this.getClass().getCanonicalName(),"Sleep for end discovery sleeping bt");
						Thread.sleep(3000);					
					} catch (InterruptedException e) {
						Log.e(this.getClass().getCanonicalName(),"Error sleeping bt");
					}
	        	}
	        	// Cancel discovery because it will slow down the connection
		        /*if(oBluetoothAdapter.isDiscovering()){
		        	oBluetoothAdapter.cancelDiscovery();
		        	//oBluetoothAdapter.startDiscovery();		        	
		        }else{
		        	oBluetoothAdapter.startDiscovery();
		        }*/
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	        	
	            if(mmSocket!=null) mmSocket.connect();
	            Log.v(this.getClass().getCanonicalName(),"Connect to Cardio Succes!");
	        } catch (IOException connectException) {
	        	try {
	        		Log.e(this.getClass().getCanonicalName(), "ConnectThread->thread->run Failed");	
	        		Log.e(this.getClass().getCanonicalName(), "oBluetoothAdapter.isDiscovering() "+oBluetoothAdapter.isDiscovering());
	        		//(connectException.printStackTrace();	            
	                if(mmSocket!=null) mmSocket.close();  	              
	                if(oBluetoothAdapter!=null) oBluetoothAdapter.cancelDiscovery();		                
	            } catch (IOException closeException) { 
	            	Log.e(this.getClass().getCanonicalName(), "ConnectThread->thread->run Failed to close socket");
	            	//closeException.printStackTrace();
	            }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        manageConnectedSocket(mmSocket);
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	            mmSocket=null;
	        } catch (IOException e) { 
	        	Log.e(this.getClass().getCanonicalName(), "ConnectThread->thread->cancel Failed to close socket");	
	        }
	    }
	}
	
	private class ConnectedThread extends Thread {
	    private BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	   
	    
	    public ConnectedThread(BluetoothSocket socket) {
	        mmSocket = socket;
	        InputStream tmpIn = null;
	      
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            if(socket!=null) tmpIn = socket.getInputStream();	        
	        } catch (IOException e) { 
	        	Log.e(this.getClass().getCanonicalName(), "Error get Stream!");
	        } catch (NullPointerException e) {
	        	Log.e(this.getClass().getCanonicalName(), "Null Error get Stream!");
			}
	 
	        mmInStream = tmpIn;	       
	    }
	 
	    public void run() {
	        byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes; // bytes returned from read()
	 
	        // Keep listening to the InputStream until an exception occurs
	        while (bReadCardio) {
	            try {
	            	bConnect=true;
	                // Read from the InputStream
	                if(mmInStream!=null) bytes = mmInStream.read(buffer);
	                else bReadCardio=false;
	                boolean heartrateValid = false; 
	                
	                // Minimum length Polar packets is 8, so stop search 8 bytes before buffer ends.
	                for (int i = 0; i < buffer.length - 8; i++) {
	                  heartrateValid = packetValid(buffer,i); 
	                  if (heartrateValid)  {
	                    heartRate = buffer[i + 5] & 0xFF;
	                    break; 
	                  }
	                }
	                //Log.d(this.getClass().getCanonicalName(), "heartrateValid "+heartrateValid);
	                // If our buffer is corrupted, use decaying last good value.
	                if(!heartrateValid) {
	                  heartRate = (int) (lastHeartRate * 0.9);  
	                  //Log.d(this.getClass().getCanonicalName(), "lastHeartRate "+lastHeartRate+" HeartRate: "+heartRate);
	                  if(heartRate < 40)
	                    heartRate = 0;
	                }
	                if(heartRate>0){
	                	lastHeartRate = heartRate;
	                }
	                	                
	                //Log.d(this.getClass().getCanonicalName(), "Read HearRate "+heartRate);
	                //Log.i(this.getClass().getCanonicalName(),"Heart Rate:"+heartRate);
	                // Send the obtained bytes to the UI Activity
	                //oHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
	                //        .sendToTarget();
	                //Bind al servizio per impstare l'heart rate
	            } catch (IOException e) {
	            	Log.e("oBTHelper", "IOException ConnectedThread");	            	   	            	
	                break;
	            }
	        }
	    }
	 
	    /* Call this from the main Activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	            mmSocket=null;
	            //Try to Reconnect
	            //connect(oDevice);
	        } catch (IOException e) { 
	        	Log.e("oBTHelper", "IOException ConnectedThread cancel");	
	        }
	    }
	}

	private boolean packetValid (byte[] buffer, int i) {
	    boolean headerValid = (buffer[i] & 0xFF) == 0xFE;
	    boolean checkbyteValid = (buffer[i + 2] & 0xFF) == (0xFF - (buffer[i + 1] & 0xFF));
	    boolean sequenceValid = (buffer[i + 3] & 0xFF) < 16;
	    
	    return headerValid && checkbyteValid && sequenceValid;
	 }
	
	/**
	 * qua gestisce la connessione al socket bluetooth aperta
	 * 
	 * */
	public void manageConnectedSocket(BluetoothSocket mmSocket) {
		oConnectedThread = new ConnectedThread(mmSocket);
		oConnectedThread.run();
	}
	/**
	 * Chiude la sessione BlueTooth
	 * */
	public void disconect() {
		if(oConnectedThread!=null){
			bReadCardio=false;
			oConnectedThread.cancel();
			oBluetoothAdapter.cancelDiscovery();
		}
	}
	/**Imposto il device Bluetooth*/
	public void setDevice(BluetoothDevice oDeviceBluetooth) {
		oDevice=oDeviceBluetooth;
	}
	public synchronized boolean isbConnect() {
		return bConnect;
	}
}

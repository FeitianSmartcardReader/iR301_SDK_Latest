package com.example.ftusbdemo;

import android.annotation.SuppressLint;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;

import com.feitian.reader.devicecontrol.Card;

public class ft_reader {
	private boolean isPowerOn = false;
	private Card inner_card;

	@SuppressLint({ "NewApi", "NewApi" })
	public ft_reader(UsbManager mUsbManager, UsbDevice mDevice) {
		// public Card(UsbManager mManager, UsbDevice mDev,Handler mHandler)
		inner_card = new Card(mUsbManager, mDevice, null);
		// TODO Auto-generated constructor stub
	}

	public String getReaderName() {
		return inner_card.getReaderName();
	}

	public String getManufacturerName() {
		return inner_card.getManufacturerName();
	}

	public int open() throws FtBlueReadException {
		int ret = inner_card.open();
		if (ret != STATUS.RETURN_SUCCESS) {
			if (ret == STATUS.READER_NOT_SUPPORT) {
				throw new FtBlueReadException(
						"sorry we just support FeiTian reader");
			}
			throw new FtBlueReadException("open device error");
		}
		return ret;
	}

	public boolean isPowerOn() {
		return isPowerOn;
	}

	public int PowerOn() throws FtBlueReadException {
		// if(inner_card.getcardStatus() == STATUS.CARD_ABSENT){
		// throw new FtBlueReadException("card is absent");
		// }
		int ret = inner_card.PowerOn();
		if (ret != STATUS.RETURN_SUCCESS) {
			throw new FtBlueReadException("Power On Failed");
		}
		isPowerOn = true;
		return ret;
	}

	public int PowerOff() throws FtBlueReadException {
		int ret = inner_card.PowerOff();
		if (ret != STATUS.RETURN_SUCCESS) {
			throw new FtBlueReadException("Power On Failed");
		}
		isPowerOn = false;
		return ret;
	}
	public String getDkVersion(){
		return inner_card.GetDkVersion();
	}
	public int getVersion(byte []recvBuf,int []recvBufLen){
		return inner_card.getVersion(recvBuf, recvBufLen);
	}
	/*==--==*/
	public int getSerialNum(byte[] serial,int serialLen[]){
		return inner_card.FtGetSerialNum(serial, serialLen);
	}
	public int readFlash(byte[] buf,int offset,int len){
		return inner_card.FtReadFlash(buf, offset, len);
	}
	public int writeFlash(byte[] buf,int offset,int len){
		return inner_card.FtWriteFlash(buf, offset, len);
	}
	public int getProtocol() throws FtBlueReadException {
		if (isPowerOn == false) {
			throw new FtBlueReadException("Power Off already");
		}
		return inner_card.getProtocol();
	}

	public byte[] getAtr() throws FtBlueReadException {
		if (isPowerOn == false) {
			throw new FtBlueReadException("Power Off already");
		}
		return inner_card.getAtr();
	}

	public int getCardStatus() {
		return inner_card.getcardStatus();
	}

	public void startCardStatusMonitoring(Handler Handler)
			throws FtBlueReadException {
		if (inner_card.registerCardStatusMonitoring(Handler) != 0) {
			throw new FtBlueReadException("not support cardStatusMonitoring");
		}
	}

	public int transApdu(int tx_length, final byte tx_buffer[],
			int rx_length[], final byte rx_buffer[]) throws FtBlueReadException {
		
		int ret = 0;
		if (isPowerOn == false) {
			throw new FtBlueReadException("Power Off already");
		}

		ret = inner_card.transApdu(tx_length, tx_buffer, rx_length,
				rx_buffer);

		if (ret == STATUS.BUFFER_NOT_ENOUGH) {
			throw new FtBlueReadException("receive buffer not enough");
		} else if (ret == STATUS.TRANS_RETURN_ERROR) {
			throw new FtBlueReadException("trans apdu error");
		}
		
		return ret;

	}
	
	private void delay(int ms){  
        try {  
            Thread.currentThread();  
            Thread.sleep(ms);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }   
     }    
	
	public int close() {
		// TODO Auto-generated method stub
		return inner_card.close();
	}
	/* for dukpt test */
}

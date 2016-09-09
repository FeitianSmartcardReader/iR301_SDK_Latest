/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ftusbdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.feitian.readerdk.Tool.DK;

@SuppressLint({ "NewApi", "NewApi", "NewApi" })
@TargetApi(12)
public class FtUsbActivity extends Activity implements View.OnClickListener,
		Runnable {

	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	private static final int MAX_LINES = 25;
	private Button mList;
	private Button mOpen;
	private Button mClose;
	private Button mTransmit;
	private Button mExit;
	private Button mPower;
	private Button mPowerOff;
	private Button mClear;
	private Button mGetAttr;
	private Button mGetStatus;
	private Button mGetProtocol;
	private Button mWriteFlash;
	private Button mReadFlash;
	private Button mGetSerialNum;
	
	private EditText mEditFlash;
	private CheckBox mCheckBoxT0;
	private CheckBox mCheckBoxT1;
	private EditText mDataSend;
	private TextView mResponseTextView;
	private Spinner mSpinner;
	private ArrayAdapter<String> mAdapter;
	private List<String> list;

	private UsbManager mUsbManager;
	private UsbDevice mDevice;

	private PendingIntent mPermissionIntent;
	private ft_reader mCard;
	public UsbEndpoint mIntPointIn;


	// 
	@Override
	public void onCreate(Bundle savedInstanceState) { // init
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ft_usb);

		mList = (Button) findViewById(R.id.BList);
		mList.setOnClickListener(this);

		mOpen = (Button) findViewById(R.id.BOpen);
		mOpen.setOnClickListener(this);

		mClose = (Button) findViewById(R.id.BClose);
		mClose.setOnClickListener(this);

		mTransmit = (Button) findViewById(R.id.BTransmit);
		mTransmit.setOnClickListener(this);

		mExit = (Button) findViewById(R.id.BExit);
		mExit.setOnClickListener(this);

		mPower = (Button) findViewById(R.id.BPower);
		mPower.setOnClickListener(this);

		mGetAttr = (Button) findViewById(R.id.BGetAtr);
		mGetAttr.setOnClickListener(this);

		mClear = (Button) findViewById(R.id.BClear);
		mClear.setOnClickListener(this);

		mGetStatus = (Button) findViewById(R.id.BGetStatus);
		mGetStatus.setOnClickListener(this);

		mGetProtocol = (Button) findViewById(R.id.BGetProtocol);
		mGetProtocol.setOnClickListener(this);

		mPowerOff = (Button) findViewById(R.id.BPowerOff);
		mPowerOff.setOnClickListener(this);
		
		mWriteFlash = (Button) findViewById(R.id.BWriteFlash);
		mWriteFlash.setOnClickListener(this);
		
		mReadFlash = (Button) findViewById(R.id.BReadFlash);
		mReadFlash.setOnClickListener(this);
		
		mGetSerialNum = (Button)findViewById(R.id.BGetSerialNum);
		mGetSerialNum.setOnClickListener(this);
		
		mEditFlash = (EditText) findViewById(R.id.EWriteFlash);
		mEditFlash.setText("hello world !!");

		mCheckBoxT0 = (CheckBox) findViewById(R.id.BCheckT0);
		mCheckBoxT1 = (CheckBox) findViewById(R.id.BCheckT1);

		mResponseTextView = (TextView) findViewById(R.id.textView1);
		mResponseTextView.setMovementMethod(new ScrollingMovementMethod());
		mResponseTextView.setMaxLines(MAX_LINES);
		mResponseTextView.setText("");

		mDataSend = (EditText) findViewById(R.id.EApdu);
		mDataSend.setText("0084000008");
		// (EditText)findViewById(R.id.ETimeOut);
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		list = new ArrayList<String>();
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(mAdapter);

		mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				logMsg("please Select a Item!");
			}
		});

		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);// start service process
		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter();
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

		registerReceiver(mUsbReceiver, filter);

		// init();
		mOpen.setEnabled(false);
		mCheckBoxT0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCheckBoxT0.setChecked(!mCheckBoxT0.isChecked());

			}
		});
		mCheckBoxT1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCheckBoxT1.setChecked(!mCheckBoxT1.isChecked());

			}
		});
		stat_close() ;
	}

	private void stat_close() {
		mList.setFocusable(true);
		mClose.setEnabled(false);
		mTransmit.setEnabled(false);
		mPower.setEnabled(false);
		mGetAttr.setEnabled(false);
		mGetStatus.setEnabled(false);
		mPowerOff.setEnabled(false);
		mGetStatus.setEnabled(false);
		mGetProtocol.setEnabled(false);
		mWriteFlash.setEnabled(false);
		mReadFlash.setEnabled(false);
		mGetSerialNum.setEnabled(false);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	// get notification of plug in/out
	@SuppressLint("NewApi")
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		// 
		@TargetApi(12)
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
				UsbDevice device = (UsbDevice) intent
						.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				if (device != null) {
				}
				logMsg("Add:  DeviceName:  " + device.getDeviceName()
						+ "  DeviceProtocol: " + device.getDeviceProtocol()
						+ "\n");

			} else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				UsbDevice device = intent
						.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				logMsg("Del: DeviceName:  " + device.getDeviceName()
						+ "  DeviceProtocol: " + device.getDeviceProtocol()
						+ "\n");
				if (null != mCard) {
					/* off our */
					// finish();
				}
				stat_close();
			}
		}
	};

	@Override
	public void onResume() {

		super.onResume();
	}

	@Override
	public void onDestroy() { // 
		// m_thread.stop();
		super.onDestroy();
	}

	public static String byte2HexStr(byte[] b, int len) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < len; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
			sb.append(" ");
		}
		return sb.toString().toUpperCase().trim();
	}

	public void onClick(View v) {
		if (v == mList) {

			mAdapter.clear();
			HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
			logMsg("======List the device======");
			int nIndex = 1;
			while (deviceIterator.hasNext()) {
				UsbDevice device = deviceIterator.next();
				logMsg(String.valueOf(nIndex++) + ": ==> "
						+ device.getDeviceName());
				mAdapter.add(device.getDeviceName());
				mOpen.setEnabled(true);
				mClose.setEnabled(false);
			}

		} else if (v == mOpen) {
			String deviceName = (String) mSpinner.getSelectedItem();
			if (deviceName != null) {
				for (UsbDevice device : mUsbManager.getDeviceList().values()) {
					// If device name is found
					if (deviceName.equals(device.getDeviceName())) {
						if (mCard != null) {
							mCard.close();
						}
						mDevice = device;
						if (!mUsbManager.hasPermission(mDevice)) {
							mUsbManager.requestPermission(mDevice,
									mPermissionIntent);
						}
						if (!mUsbManager.hasPermission(mDevice)) {
							return;
						}
						mCard = new ft_reader(mUsbManager, mDevice);
						break;
					} else {
					}
				}
			}
			try {
				int ret = mCard.open();
				mClose.setEnabled(true);
				mOpen.setEnabled(false);
				mPower.setEnabled(true);
				mPowerOff.setEnabled(true);
				mGetStatus.setEnabled(true);
				mWriteFlash.setEnabled(true);
				mReadFlash.setEnabled(true);
				mGetSerialNum.setEnabled(true);
				logMsg("open success ");
				logMsg("ManufacturerName: " + mCard.getManufacturerName());
				logMsg("Reader: " + mCard.getReaderName());
				logMsg("DK Version:" + mCard.getDkVersion());
				byte ReaderVersion[] = new byte[512];
				int []len = new int[1];
				mCard.getVersion(ReaderVersion, len);
				logMsg("Reader Version:"+ReaderVersion[0]+"."+ReaderVersion[1]);
				/**/
				mCard.startCardStatusMonitoring(mHandler);
			} catch (Exception e) {
				logMsg("Exception: => " + e.toString());
			}
		} else if (v == mClose) {
			try {
				mCard.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logMsg("Exception: => " + e.toString());
			}
			stat_close();
			mOpen.setEnabled(true);
		} else if (v == mGetStatus) {
			int ret = mCard.getCardStatus();
			if (STATUS.CCID_ICC_PRESENT == ret ) {
				if (mCard.isPowerOn())
					logMsg("Card is present and is activated");
				else
					logMsg("Card is present and is not activated");
			} else if (STATUS.CCID_ICC_ABSENT == ret) {
				logMsg("Card is absent");
			} else {
				logMsg("Card is UNKNOWN");
			}

		}else if (v == mWriteFlash){
			String str = mEditFlash.getText().toString();
			if(str.length() == 0){
				logMsg("WriteFlash-->text null");
			}else{
				if(DK.RETURN_SUCCESS == mCard.writeFlash(str.getBytes(), 0, str.length())){
					logMsg("WriteFlash-->success");
				}else{
					logMsg("writeFlash-->error");
				}
			}
		}else if (v == mReadFlash) {
			byte buf[] = new byte[512];
			if(DK.RETURN_SUCCESS == mCard.readFlash(buf, 0, 255)){
				logMsg("readFlash-->"+new String(buf));
			}else{
				logMsg("readFlash-->error");
			}
		}else if(v == mGetSerialNum){
			byte serialNum[] = new byte[128];
			int serialLen[] = new int[1];
			mCard.getSerialNum(serialNum, serialLen);
			serialNum[serialLen[0]] = '\0';
			String str = new  String(serialNum);
			logMsg("GetSerialNum-->"+str);
		}else if (v == mGetAttr) {
			try {
				byte[] Atr = mCard.getAtr();
				if (null != Atr) {
					logMsg("Atr: " + byte2HexStr(Atr, Atr.length));
				} else {
					logMsg("getAtr error!");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logMsg("Exception: => " + e.toString());
			}
		} else if (v == mGetProtocol) {
			int Protocol = 0;
			try {
				Protocol = mCard.getProtocol();
				if (STATUS.CARD_PROTOCOL_T0 == Protocol) {
					mCheckBoxT0.setChecked(true);
					mCheckBoxT1.setChecked(false);
				} else if (STATUS.CARD_PROTOCOL_T1 == Protocol) {
					mCheckBoxT0.setChecked(false);
					mCheckBoxT1.setChecked(true);
				} else {
					mCheckBoxT0.setChecked(false);
					mCheckBoxT1.setChecked(false);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				logMsg("Exception: => " + e.toString());
			}
		}

		else if (v == mTransmit) {
			String dataSendStr = mDataSend.getText().toString();
			if (!isLegal(dataSendStr)) {
				new AlertDialog.Builder(FtUsbActivity.this)
						.setTitle("prompt")
						.setMessage("prease input data as '0~9' 'a~f' 'A~F'")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										mDataSend.setText("");
									}
								}).show();
			} else {
				byte[] arrayOfByte = hexStringToBytes(dataSendStr);
				byte[] array = new byte[1024];
				if (null == arrayOfByte) {
					new AlertDialog.Builder(FtUsbActivity.this)
							.setTitle("prompt")
							.setMessage("prease input data!")
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											mDataSend.setText("");
										}
									}).show();
				} else {
					try {
						int[] receiveln = new int[2];
						int ret = mCard.transApdu(arrayOfByte.length,
								arrayOfByte, receiveln, array);
						if (ret == STATUS.RETURN_SUCCESS)
							logMsg(" receive: "
									+ byte2HexStr(array, receiveln[0]));

						else {
							logMsg(" receive error: ");
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logMsg("Exception: => " + e.toString());
					}
				}
			}
		} else if (v == mPower) {
			try {
				int ret = mCard.PowerOn();
				if (STATUS.RETURN_SUCCESS == ret) {
					mTransmit.setEnabled(true);
					mGetAttr.setEnabled(true);
					mPowerOff.setEnabled(true);
					mGetProtocol.setEnabled(true);
					logMsg("powerOn success !");
				} else {
					logMsg("powerOn error !");
				}
			} catch (Exception e) {
				logMsg("Exception: => " + e.toString());
			}
		} else if (v == mPowerOff) {
			mCheckBoxT0.setChecked(false);
			mCheckBoxT1.setChecked(false);
			try {
				mCard.PowerOff();
				logMsg("PowerOff success !");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logMsg("Exception: => " + e.toString());
			}
		} else if (v == mClear) {
			mResponseTextView.setText("");
			mResponseTextView.scrollTo(0, mResponseTextView.getLineHeight());
		} else if (v == mExit) {
			new AlertDialog.Builder(FtUsbActivity.this)
					.setTitle("prompt")
					.setMessage("Do you want to leave?")
					.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
								}
							})
					.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// do nothing
								}

							}).show();
			// this.finish();
		} 
	}

	private boolean isLegal(String dataSendStr) {
		// TODO Auto-generated method stub
		for (int i = 0; i < dataSendStr.length(); i++) {
			if (!(((dataSendStr.charAt(i) >= '0') && (dataSendStr.charAt(i) <= '9'))
					|| ((dataSendStr.charAt(i) >= 'a') && (dataSendStr
							.charAt(i) <= 'f')) || ((dataSendStr.charAt(i) >= 'A') && (dataSendStr
					.charAt(i) <= 'F')))) {
				return false;
			}
		}
		return true;
	}

	public synchronized void logMsg(String msg) {
		String oldMsg = mResponseTextView.getText().toString();

		mResponseTextView.setText(oldMsg + "\n" + msg);

		if (mResponseTextView.getLineCount() > MAX_LINES) {
			mResponseTextView.scrollTo(0,
					(mResponseTextView.getLineCount() - MAX_LINES)
							* mResponseTextView.getLineHeight());
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DK.CARD_STATUS:
				switch (msg.arg1) {
				case DK.CARD_ABSENT:
					logMsg("IFD card absent");
					break;
				case DK.CARD_PRESENT:
					logMsg("IFD card persent");
					try {
						mCard.PowerOff();
						mCard.PowerOn();
					} catch (FtBlueReadException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				case DK.CARD_UNKNOWN:
					logMsg("IFD card unknown");
					break;
				case DK.IFD_COMMUNICATION_ERROR:
					logMsg("IFD IFD error");
					break;
				}
			default:
				break;
			}
		}
	};
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		if (hexString.length() % 2 != 0) {
			hexString = hexString.substring(0, hexString.length() - 1)
					+ "0"
					+ hexString.substring(hexString.length() - 1,
							hexString.length());
		}

		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	private void WarnMsg(String msg) {
		new AlertDialog.Builder(FtUsbActivity.this).setTitle("prompt")
				.setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mDataSend.setText("");
					}
				}).show();
	}
}

package com.example.ftusbdemo;

public class STATUS {
	public final static int RETURN_SUCCESS =    0;
	public final static int RETURN_ERROR   = -1;
	public final static int ERROR_RECEIVE_LRC = 0xf0;
	public static final int PROTO_NOT_SUPPORT = 1;
	public static final int READER_NOT_SUPPORT = 2;
	public static final int IFD_COMMUNICATION_ERROR	     = 612; 
	public static final int IFD_NOT_SUPPORTED            = 614;	
	
	public static final int TRANS_RETURN_ERROR = 0xF001;
	public static final int BUFFER_NOT_ENOUGH  = 0xF002;
	
	public static final int CCID_ICC_PRESENT	     = 1;
	public static final int CCID_ICC_UNKNOWN 	      = 2;
	public static final int CCID_ICC_ABSENT        = 3;
	
	public static final int READER_PRESENT       = 0;
	public static final int READER_ABSENT        = 2;
	
	public static final int CARD_PROTOCOL_T0  = 0;
	public static final int CARD_PROTOCOL_T1  = 1;
}

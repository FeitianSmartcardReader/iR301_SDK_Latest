------------- iR301-U Software Developer's Kit -------------

------------------ Folder Content List ------------------

iOS SDK:
	https://github.com/FeitianSmartcardReader/bR301_iOS_SDK_RELEASE

iOS SDK source code:
	Under GPL 2.0 standard, we publish our source code for iOS platform, if you are interesting using source code, youc can download it from github:
	https://github.com/FeitianSmartcardReader/bR301_iOS



------------------------ Modify history ------------------------

2016/02/16
	Upload iOS SDK into github which also can work for bR301.
2014/12/03
	To get the latest bR301 SDK, you also can access github to get whole source code of bR301, the link is below:https://github.com/FeitianSmartcardReader
2014/10/08
	Add non-PCSC api
	Add comments(readerInterfaceDidChange/cardInterfaceDidDetach) of readerInterfaceDidChange in ReaderInterface.h
2014/8/15
	Add bR301, iR301, iPad casing reader support
	Modify source code to improve speed of get card/reader status of bR301
	Fixed bug when application access open/close session at same time, add mutex in source code
	Fixed minor bugs in arm64
	Fixed minor bugs in SCardListReader API
	Fixed minor bugs when running application in background
2014/6/4
	Removed DUKPT folder
	Updated demo application, the new demo support background mode and support get lib/firmware version
2014/04/01
	Fixed bugs for iPad case reader support
	Fixed bugs when application enter to background on iOS 7
	Support armv7s/armv7/arm64
	Add get library version API in SDK, now you can through API(FtGetLibVersion) to get library version
	Support application running in iOS background (FtDidEnterBackground)
	Fixed bugs in DUKPT encryption
2014/01/14
	Fixed bugs in iOS SDK
		1. Add get library version in SDK 
		2. Resolved AES_decryp API conflict
2013/11/06
	The bugs has fixed in Aug, 2013 which is related Multi-thread in SDK
	Add DUKPT sample code
2013/3/24
	Add DUKPT function to SDK(More information, please follow developer manual)
2013/02/20:
	Add multi-thread support to get reader status
	Fixed bugs and supported iOS 6.1
	Supported armv6/armv7/armv7s
2012/08/23:
	 Add SCardGetStatusChange funciton
	 Add 256bytes use space and provide API for call(FtWriteFlash/FtReadFlash)
	 Add to get serial number function (FtGetSerialNum)


------------------------ Release ------------------------

[Version Information]
    Model        : iR301-U
    SDK          : 1.0
    Release Date : 2012-6-13
    Language     : English


[Product Information]

    1.  Product Model: 
        iR301-U

	   
[General Introduction]
	
    Feitian iR301 is a CCID compliant smart card reader with USB 2.0 full speed support. It offers a plug-and-play solution saving the effort on driver installation and system compliance checking.

    Feitian iR301 can be adopted in smart card-based applications, suck as e-Banking, e-Government, e-Payment, access control, network security and more.
    
    More CCID information, please follow the below website:
http://msdn.microsoft.com/en-us/windows/hardware/gg487509.aspx
	

	   	   
[Product Features]


    *USB 2.0 Full Speed Device
    *Compliant with PC/SC, CCID Standards
    *Supports ISO-7816-1/2/3 T=0 and T=1 Protocol
    *Supports ISO-7816 Class A,B and C Cards
    *Supports Protocol and Parameters Selection (PPS)
    *Compliant with EMV Level 1
    *Short Circuit Protection
	

[Supported Platforms]

    PC Platform:
	Windows 2000/XP/2003/2008/Vista/Windows7+ Linux, Mac OS X, UNIX 
    Mobile Platform:
	Android(support OTG)/iOS 3.13+


 
 
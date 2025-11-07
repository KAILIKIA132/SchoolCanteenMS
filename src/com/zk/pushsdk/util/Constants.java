package com.zk.pushsdk.util;

/**
 * Define all the constant value.
 * @author seiya
 *
 */

public final class Constants
{
	/**
	 * the label of file config.xml
	 */
	/**Configuration file name*/
	public static final String CONFIG_FILE_NAME = "config.xml";
	/**the URL of database connection*/
	public static final String DATABASE_URL = "databaseconnect.url";
	/**user name for database*/
    public static final String DATABASE_USER = "databaseconnect.user";
    /**password for database*/
    public static final String DATABASE_PWD = "databaseconnect.password";
    /**Driver for database*/
    public static final String DATABASE_DRIVER = "databaseconnect.driverclass";
    /**None*/
    @Deprecated
    public static final String OPTION_VER = "option.ver";
    /**the record count of a single page*/
    public static final String OPTION_PAGE_SIZE = "option.pagesize";
    /**the size of monitor data for a single page*/
    public static final String OPTION_MONIGOR_SIZE = "option.monitorsize";
    
    /**
     * the Server command and the constant value
     */
    /**Command header*/
    public static final String DEV_CMD_TITLE = "C:";
    /**SHELL command*/
    public static final String DEV_CMD_SHELL = "SHELL {0}";
    /**CHECK*/
    public static final String DEV_CMD_CHECK = "CHECK";
    /**CLEAR ATTENDANCE RECORD*/
    public static final String DEV_CMD_CLEAR_LOG = "CLEAR LOG";
    /**CLEAR ATTENDANCE PHOTO*/
    public static final String DEV_CMD_CLEAR_PHOTO = "CLEAR PHOTO";
    /**CLEAR ALL DATA*/
    public static final String DEV_CMD_CLEAR_DATA = "CLEAR DATA";
    /**SEND DEVICE INFO TO SERVER*/
    public static final String DEV_CMD_INFO = "INFO";
    /**SET DEVICE OPTION*/
    public static final String DEV_CMD_SET_OPTION = "SET OPTION {0}";
    /**REBOOT DEVICE*/
    public static final String DEV_CMD_REBOOT = "REBOOT";
    /**UPDATE USER INFO*/
    public static final String DEV_CMD_DATA_UPDATE_USERINFO = "DATA UPDATE USERINFO PIN={0}\tName={1}\tPri={2}\tPasswd={3}\tCard={4}\tGrp={5}\tTZ={6}\tCategory={7}";
    /**UPDATE FP TEMPLATE*/
    public static final String DEV_CMD_DATA_UPDATE_FINGER = "DATA UPDATE FINGERTMP PIN={0}\tFID={1}\tSize={2}\tValid={3}\tTMP={4}";
    /**UPFATE FACE TEMPLATE*/
    public static final String DEV_CMD_DATA_UPDATE_FACE = "DATA UPDATE FACE PIN={0}\tFID={1}\tSize={2}\tValid={3}\tTMP={4}";
    /**UPDATE USER PHOTO*/
    public static final String DEV_CMD_DATA_UPDATE_USERPIC = "DATA UPDATE USERPIC PIN={0}\tSize={1}\tContent={2}";
    /**UPDATE BIOPHOTO*/
    public static final String DEV_CMD_DATA_UPDATE_BIOPHOTO = "DATA UPDATE BIOPHOTO PIN={0}\tType={1}\tSize={2}\tContent={3}";
    /**UPDATE BIODATA abhishek*///DATA UPDATE BIODATA Pin=70059\tNo=0\tIndex=0\tValid=1\tDuress=0\tType=9\tMajorVer=58\tMinorVer=7\tFormat=0\tTmp
    public static final String DEV_CMD_DATA_UPDATE_BIODATA = "DATA UPDATE BIODATA Pin={0}\tNo={1}\tIndex={2}\tValid={3}\tDuress={4}\tType={5}\tMajorVer={6}\tMinorVer={7}\tFormat={8}\tTmp={9}";
    /**UPDATE SMS*/
    public static final String DEV_CMD_DATA_UPDATE_SMS = "DATA UPDATE SMS MSG={0}\tTAG={1}\tUID={2}\tMIN={3}\tStartTime={4}";
    /**UPDATE USER SMS*/
    public static final String DEV_CMD_DATA_UPDATE_USER_SMS = "DATA UPDATE USER_SMS PIN={0}\tUID={1}";
    /**DELETE USER INFO*/
    public static final String DEV_CMD_DATA_DELETE_USERINFO = "DATA DELETE USERINFO PIN={0}";
    /**DELETE FP TEMPLATE*/
    public static final String DEV_CMD_DATA_DELETE_FINGER = "DATA DELETE FINGERTMP PIN={0}\tFID={1}";
    /**DELETE FACE TEMPLATE*/
    public static final String DEV_CMD_DATA_DELETE_FACE = "DATA DELETE FACE PIN={0}\tFID={1}";
    /**DELETE USER PHOTO*/
    public static final String DEV_CMD_DATA_DELETE_USERPIC = "DATA DELETE USERPIC PIN={0}";
    /**CLEAR USER*/
    public static final String DEV_CMD_DATA_CLEAR_USERINFO = "CLEAR ALL USERINFO";
    /**DELETE SMS*/
    public static final String DEV_CMD_DATA_DELETE_SMS = "DATA DELETE SMS UID={0}";
    /**QUERY ATTENDANCE RECORD*/
    public static final String DEV_CMD_DATA_QUERY_ATTLOG = "DATA QUERY ATTLOG StartTime={0}\tEndTime={1}";
    /**QUERY ATTENDANCE PHOTO*/
    public static final String DEV_CMD_DATA_QUERY_ATTPHOTO = "DATA QUERY ATTPHOTO StartTime={0}\tEndTime={1}";
    /**QUERY USER INFO*/
    public static final String DEV_CMD_DATA_QUERY_USERINFO = "DATA QUERY USERINFO PIN={0}";
    /**QUERY FP BY USER AND FINGER INDEX*/
    public static final String DEV_CMD_DATA_QUERY_FINGERTMP = "DATA QUERY FINGERTMP PIN={0}\tFID={1}";
    /**QUERY FP BY USER ID*/
    public static final String DEV_CMD_DATA_QUERY_FINGERTMP_ALL = "DATA QUERY FINGERTMP PIN={0}";
    /**ONLINE ENROLL USER FP*/
    public static final String DEV_CMD_ENROLL_FP = "ENROLL_FP PIN={0}\tFID={1}\tRETRY={2}\tOVERWRITE={3}";
    /**CHECK AND SEND LOG*/
    public static final String DEV_CMD_LOG = "LOG";
    /**UNLOCK THE DOOR*/
    public static final String DEV_CMD_AC_UNLOCK = "AC_UNLOCK";
    /**CLOSE THE ALARM*/
    public static final String DEV_CMD_AC_UNALARM = "AC_UNALARM";
    /**GET FILE*/
    public static final String DEV_CMD_GET_FILE = "GetFile {0}";
    /**SEND FILE*/
    public static final String DEV_CMD_PUT_FILE = "PutFile {0}\t{1}";
    /**RELOAD DEVICE OPTION*/
    public static final String DEV_CMD_RELOAD_OPTIONS = "RELOAD OPTIONS";
    /**AUTO PROOFREAD ATTENDANCE RECORD*/
    public static final String DEV_CMD_VERIFY_SUM_ATTLOG = "VERIFY SUM ATTLOG StartTime={0}\tEndTime={1}";    
    /**UPDATE MEET INFO*/
    public static final String DEV_CMD_DATA_UPDATE_MEETINFO = "DATA UPDATE MEETINFO MetName={0}\tMetStarSignTm={1}\tMetLatSignTm={2}\tEarRetTm={3}\tLatRetTm={4}\tCode={5}\tMetStrTm={6}\tMetEndTm={7}";
    /**DELETE MEET INFO*/
    public static final String DEV_CMD_DATA_DELETE_MEETINFO = "DATA DELETE MEETINFO Code={0}";
    /**UPDATE PERS MEET*/
    public static final String DEV_CMD_DATA_UPDATE_PERSMEET = "UPDATE PERSMEET Code={0}\tPin={1}";
    /**PutAdvFile*/
    public static final String DEV_CMD_DATA_UPDATE_ADV = "PutAdvFile Type={0}\tFileName={1}\tUrl=downloadFile?SN={2}&path={3}";
    /**DelAdvFile*/
    public static final String DEV_CMD_DATA_DELETE_ADV = "DelAdvFile Type={0}\tFileName={1}";
    /**CLEAR ADV FILE*/
    public static final String DEV_CMD_DATA_CLEAR_ADV = "DelAdvFile Type={0}";
    /**CLEAR MEET INFO*/
    public static final String DEV_CMD_DATA_CLEAR_MEET = "CLEAR MEETINFO";
    /**CLEAR PERSMEET INFO*/
    public static final String DEV_CMD_DATA_CLEAR_PERSMEET = "CLEAR PERSMEET";
    
    
    public static final String DEV_TABLE_ATTLOG = "ATTLOG";
    public static final String DEV_TABLE_OPLOG = "OPERLOG";
    public static final String DEV_TABLE_ATTPHOTO = "ATTPHOTO";
    public static final String DEV_TABLE_SMS = "SMS";
    public static final String DEV_TABLE_USER_SMS = "USER_SMS";
    public static final String DEV_TABLE_USERINFO = "USERINFO";
    public static final String DEV_TABLE_FINGER_TMP = "FINGERTMP";
    public static final String DEV_TABLE_FACE = "FACE";
    public static final String DEV_TABLE_PLAM = "PLAM";
    public static final String DEV_TABLE_USERPIC = "USERPIC";
    
    public static final String system_dev_timeZone="The Time Zone";
    public static final String system_utc_12="(UTC-12)International Date Change Line West";
    public static final String system_utc_11="(UTC-11)Coordinated Universal Time-11";
    public static final String system_utc_10="(UTC-10)Hawaii";
    public static final String system_utc_9="(UTC-9)Alaska";
    public static final String system_utc_8="(UTC-8)Pacific time (American and Canada)  Baja California";
    public static final String system_utc_7="(UTC-7)La Paz, Massa Rand, The mountain time (American and Canada), Arizona";
    public static final String system_utc_6="(UTC-6)Saskatchewan, Central time, Central America";
    public static final String system_utc_5="(UTC-5)Bogota, Lima, Quito, Leo Branco, Eastern time, Indiana(East)";
    public static final String system_utc_430="(UTC-4:30)Caracas";
    public static final String system_utc_4="(UTC-4)Atlantic time, Cuiaba, Georgetown, La Paz, Santiago";
    public static final String system_utc_330="(UTC-3:30)Newfoundland";
    public static final String system_utc_3="(UTC-3)Brasilia, Buenos Aires, Greenland, Cayenne";
    public static final String system_utc_2="(UTC-2)The International Date Line West-02";
    public static final String system_utc_1="(UTC-1)Cape Verde Islands, Azores";
    public static final String system_utc_0="(UTC)Dublin, Edinburgh, Lisbon, London, The International Date Line West";
    public static final String system_utc1="(UTC+1)Amsterdam, Brussels, Sarajevo";
    public static final String system_utc2="(UTC+2)Beirut, Damascus, Eastern Europe, Cairo,Athens, Jerusalem";
    public static final String system_utc3="(UTC+3)Baghdad, Kuwait, Moscow, St Petersburg,Nairobi";
    public static final String system_utc330="(UTC+3:30)Teheran or Tehran";
    public static final String system_utc4="(UTC+4)Abu Zabi, Yerevan, Baku, Port Louis, Samarra";
    public static final String system_utc430="(UTC+4:30)Kabul";
    public static final String system_utc5="(UTC+5)Ashkhabad, Islamabad, Karachi";
    public static final	String system_utc530="(UTC+5:30)Chennai, Calcutta Mumbai, New Delhi";
    public static final	String system_utc545="(UTC+5:45)Katmandu";
    public static final String system_utc6="(UTC+6)Astana, Dhaka, Novosibirsk";
    public static final	String system_utc630="(UTC+6:30)Yangon";
    public static final	String system_utc7="(UTC+7)Bangkok, Hanoi, Jakarta";
    public static final	String system_utc8="(UTC+8)Beijing, Chinese Taipei, Irkutsk, Ulan Bator";
    public static final	String system_utc9="(UTC+9)Osaka, Tokyo, Seoul, Yakutsk";
    public static final	String system_utc930="(UTC+9:30)Adelaide, Darwin";
    public static final	String system_utc10="(UTC+10)Brisbane, Vladivostok, Guam, Canberra";
    public static final	String system_utc11="(UTC+11)Jo Kul Dah, Solomon Islands, New Caledonia";
    public static final	String system_utc12="(UTC+12)Anadyr, Oakland, Wellington, Fiji";
    public static final	String system_utc13="(UTC+13)Nukualofa, The Samoa Islands";
    public static final	String system_utc14="(UTC+14)Christmas Island";

    
    /**
     *Biometric template type
     */
    public static final int BIO_TYPE_GM = 0;//generic
    public static final int BIO_TYPE_FP = 1;//finger print
    public static final int BIO_TYPE_FACE = 2;//FACE
    public static final int BIO_TYPE_VOICE = 3;//VOICE
    public static final int BIO_TYPE_IRIS = 4;//IRIS
    public static final int BIO_TYPE_RETINA = 5;//RETINA
    public static final int BIO_TYPE_PP = 6;//palm print
    public static final int BIO_TYPE_FV = 7;//finger-vein
    public static final int BIO_TYPE_PALM = 8;//PALM
    public static final int BIO_TYPE_VF = 9;//visible face
    
    
    /**
     * Biometric algorithm version
     */
    public static final String BIO_VERSION_FP_9 = "9.0";//Finger print version: 9.0
    public static final String BIO_VERSION_FP_10 = "10.0";//Finger print version: 10.0
    public static final String BIO_VERSION_FP_12 = "12.0";//Finger print version: 10.0
    public static final String BIO_VERSION_FACE_5 = "5.0";//Face version: 5.0
    public static final String BIO_VERSION_FACE_7 = "7.0";//Face version: 7.0
    public static final String BIO_VERSION_FV_3 = "3.0";
    
    /**
     * Biometric data format
     */
    public static final int BIO_DATA_FMT_ZK = 0;//ZK format
    public static final int BIO_DATA_FMT_ISO = 1;//ISO format
    public static final int BIO_DATA_FMT_ANSI = 2;//ANSI format
    
    /**
     * Language Encoding
     */
    public static final String DEV_LANG_ZH_CN = "83";//chinese
    
    public static final String DEV_ATTR_CMD_SIZE = "CMD_SIZE";
	
	public static final String DEV_CMD_DATA_DELETE_PALM = "DATA DELETE PALM PIN={0}";
	public static final String DEV_CMD_DATA_UPDATE_PALM = "DATA UPDATE BIODATA PIN={0}\tNo={2}\tIndex={3}\tValid={4}\tType={5}\tMajorVer={6}\tMinorVer={7}\tFormat={8}\tTemp={9}";
	
    /**
     * support feature
     * @author seiya
     *
     */
    public static enum DEV_FUNS {
    	FP,
    	FACE,
    	PLAM,
    	USERPIC,
    	BIOPHOTO,
    	BIODATA
    };
    
    
}

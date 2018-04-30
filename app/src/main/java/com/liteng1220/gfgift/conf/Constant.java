package com.liteng1220.gfgift.conf;

public final class Constant {

    public static final String COUNTRY_CODE = "+86";
    public static final String SDCARD_FOLDER_NAME = "YDD";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String OS = "Android";
    public static final String FILE_EXT_JPG = "jpg";

    public static final byte DEFAULT_YES = 1;
    public static final byte DEFAULT_NO = 0;

    public static class RequestCode {
        public static final int REQUEST_CODE_CREATE_PRODUCT = 0x0001;
        public static final int REQUEST_CODE_PICK_IMAGE = 0x0002;
        public static final int REQUEST_CODE_SELECT_BACKUP_FILE = 0x0004;
    }

    public static class Bundle {
        public static final String BUNDLE_KEY_IMAGE_PATH = "sd81206430150197"; // IMAGE_PATH
        public static final String BUNDLE_KEY_FILE_PATH = "sd5811430150197"; // FILE_PATH
        public static final String BUNDLE_KEY_TITLE = "sd19819114"; // TITLE
        public static final String BUNDLE_KEY_CONTENT = "sd214131941319"; // CONTENT
        public static final String BUNDLE_KEY_TIME = "sd198124"; // TIME
        public static final String BUNDLE_KEY_BUTTON_NAME = "sd1201919141330130124"; // BUTTON_NAME
        public static final String BUNDLE_KEY_ID = "sd83"; // ID
        public static final String BUNDLE_KEY_TYPE = "sd51564736"; // TYPE
        public static final String BUNDLE_KEY_BARCODE = "sd101721434"; // BARCODE
        public static final String BUNDLE_KEY_STATUS = "sd18190192018"; // STATUS
        public static final String BUNDLE_KEY_DATA = "sd30190"; // DATA
        public static final String BUNDLE_KEY_NAME = "sd130124"; // NAME
    }

    public static class SharedPreference {
        public static final String SP_KEY_LAUNCH_TIME = "sd11020132730198124"; // LAUNCH_TIME
        public static final String SP_KEY_UID = "sd2083"; // UID
        public static final String SP_KEY_USERNAME = "sd2018417130124"; // USERNAME
    }

    public static class ActionType {
        public static final String ACTION_TYPE_SEARCH = "sd0219814133019241543018401727"; // ACTION_TYPE_SEARCH
        public static final String ACTION_TYPE_START_DOWNLOAD = "sd02198141330192415430181901719303142213111403"; // ACTION_TYPE_START_DOWNLOAD
        public static final String ACTION_TYPE_STOP_DOWNLOAD = "sd0219814133019241543018191415303142213111403"; // ACTION_TYPE_STOP_DOWNLOAD
    }

}

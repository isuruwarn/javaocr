package com.ocr.util;



/**
 * 
 * @author isuru
 *
 */
public class GlobalConstants {
	
	
	public static final String ENGLISH = "English";
	public static final String SINHALA = "Sinhala";
	
	// TODO: read from file
	public static final String ENG_MAP_FILE = "files/eng15.map";
	public static final String SIN_MAP_FILE = "files/sin15.map";
	
	
	// icon image file
	public static final String CHAR_SAVED_ICO_FILE = "img/mapping_saved_ico.png";
	
	
	// main application frame
	public static final int MAIN_FRAME_W = 700;
	public static final int MAIN_FRAME_H = 880;

	
	// panel dimensions
	public static final int ENG_MAPPINGS_PANEL_W = 600;
	public static final int ENG_MAPPINGS_PANEL_H = 630;
	
	public static final int SIN_MAPPINGS_PANEL_W = 600;
	public static final int SIN_MAPPINGS_PANEL_H = 750;
	
	public static final int MAPPINGS_IMG_PANEL_W = 400;
	public static final int MAPPINGS_IMG_PANEL_H = 150;
	
	public static final int NUM_BTNS_PANEL_W = 600;
	public static final int NUM_BTNS_PANEL_H = 50;
	
	public static final int SP_CHAR_BTNS_PANEL_W = 600;
	public static final int SP_CHAR_BTNS_PANEL_H = 140;
	
	public static final int ENG_CHAR_BTNS_PANEL_W = 600;
	public static final int ENG_CHAR_BTNS_PANEL_H = 210;
	
	public static final int SIN_CHAR_BTNS_PANEL_W = 600;
	public static final int SIN_CHAR_BTNS_PANEL_H = 380;
	
	
	// button dimensions
	public static final int REG_CHAR_BUTTON_W = 45;
	public static final int REG_CHAR_BUTTON_H = 35;
	
	public static final int SIN_CHAR_BUTTON_W = 50;
	public static final int SIN_CHAR_BUTTON_H = 40;

	
	// label dimensions
	public static final int STAT_LBL_W = 600;
	public static final int STAT_LBL_H = 25;
	
	public static final int CHAR_IDX_LBL_W = 20;
	public static final int CHAR_IDX_LBL_H = 25;
	
	public static final int CHAR_IMG_LBL_W = 100;
	public static final int CHAR_IMG_LBL_H = 100;
	
	public static final int CHAR_SAVED_IMG_LBL_W = 50;
	public static final int CHAR_SAVED_IMG_LBL_H = 100;
	

	// text box dimensions
	public static final int MAIN_TXT_AREA_W = 650;
	public static final int MAIN_TXT_AREA_H = 650;
	
	public static final int INPUT_IMG_PATH_TXT_W = 300;
	public static final int INPUT_IMG_PATH_TXT_H = 25;
	
	public static final int CHAR_MAP_TXT_W = 100;
	public static final int CHAR_MAP_TXT_H = 100;
	
	public static final int CHAR_INFO_TXT_W = 360;
	public static final int CHAR_INFO_TXT_H = 25;
	
	
	// action commands
	public static final String CHOOSE_FILE_ACTION = "Choose File";
	public static final String COMBOBOX_CHANGED_ACTION = "comboBoxChanged";
	public static final String SCAN_ACTION = "Scan";
	public static final String COPY_ACTION = "Copy";
	public static final String CLEAR_ACTION = "Clear";
	public static final String RESOLVE_ACTION = "Resolve";
	public static final String PREV_MAPPING_ACTION = "Prev";
	public static final String NEXT_MAPPING_ACTION = "Next";
	public static final String SAVE_MAPPING_ACTION = "Save";
	public static final String SAVEALL_MAPPING_ACTION = "Save All";
	public static final String CLEAR_MAPPING_ACTION = "Clear";
	public static final String CLEARALL_MAPPING_ACTION = "Clear All";

	
	// fonts
	public static final String SANSSERIF_FONT_TYPE = "SansSerif";
	public static final String VERDANA_FONT_TYPE = "Verdana";
	public static final String ISKOOLA_POTA_FONT_TYPE = "Iskoola Pota";
	
	
	// font sizes
	public static final int MAIN_TEXT_ENG_FONT_SIZE = 12;
	public static final int MAIN_TEXT_SIN_FONT_SIZE = 20;
	public static final int SIN_CHAR_BUTTON_FONT_SIZE = 15;
	public static final int REG_CHAR_BUTTON_FONT_SIZE = 12;
	public static final int CHAR_MAPPINGS_ENG_FONT_SIZE = 35;
	public static final int CHAR_MAPPINGS_SIN_FONT_SIZE = 35;
	
	
	// text strings
	public static final String TITLE = "Java OCR";
	public static final String RESOLVE_TITLE = "Resolve unrecognized characters";
	public static final String STAT_LBL_TXT_STR = "Lines: %d     Characters (with spaces): %d     Unrecognized Characters: %d";
	public static final String SAVE_MAPPING_TITLE = "Save mapping";
	public static final String SAVE_MULTIPLE_MAPPPINGS_TITLE = "Save multiple mappings";
	public static final Object MAPPING_EXISTS_MSG = "Mapping already exists";
	public static final Object SAVE_MAPPPING_SUCCESS_MSG = "Mapping successfully saved";
	public static final Object SAVE_MAPPPING_ERROR_MSG1 = "Error while saving mapping. Please try again.";
	public static final Object SAVE_MAPPPING_ERROR_MSG2 = "Please enter a value";
	public static final String SAVE_MULTIPLE_MAPPPINGS_SUCCESS_MSG = "%d mappings were successfully saved";
	public static final Object SAVE_MULTIPLE_MAPPPINGS_ERROR_MSG = "Error while saving mulitple mappings. Please try again.";
	public static final String CLEAR_ALL_MAPPINGS_CONF_MSG = "Are you sure you want to clear all unsaved mappings in current session?";
	
	// for debugging
	public static final String SAMPLE_IMG_FILENAME = "sampleImgs/sampleImg1.png";
	public static final String SAMPLE_OUTPUT_FILENAME = "output/output1.txt";
	
	
}

package edu.columbia.threescompany.graphics;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

public class GuiConstants {

	public static final String	VERSION = "0.1";
	
	public static final int		BOARD_LENGTH = 600;
	
	public static final int		GUI_WIDTH = 840;
	public static final int		GUI_HEIGHT = 680;
	
	public static final int		PREGAME_GUI_WIDTH = 320;
	public static final int		PREGAME_GUI_HEIGHT = 200;
	
	public static final int		PLAYER_GUI_WIDTH = 320;
	public static final int		PLAYER_GUI_HEIGHT = 200;
	
	public static final Font	CHAT_FONT = new Font("Tahoma", Font.PLAIN, 9);
	public static final Font	BUTTON_DISABLED_FONT = new Font("Tahoma", Font.PLAIN, 10);
	public static final Font	BUTTON_ENABLED_FONT = new Font("Tahoma", Font.BOLD, 10);
	public static final Font	AP_LABEL_FONT = new Font("Tahoma", Font.BOLD, 9);
	
	public static final Color	BG_COLOR = Color.WHITE;
	
	public static final String	IMAGES_DIR = "images" + File.separator;
	public static final String	IMAGES_MENU_DIR = "images" + File.separator + "menu" + File.separator;
	
    /** String for About box. */
    public static final String	HELP_ABOUT =	"<html><b>Blobs v" + VERSION + "</b>\n" +
												"2007, Columbia University, CS4995\n" +
												"Daniel Benamy\n" +
												"John Morales\n" +
												"Eugene Kozhukalo\n" +
    											"Zach van Schouwen";
    
    public static final String PUSH_BLOB_IMAGE = "bgblu02.gif";
    public static final String PULL_BLOB_IMAGE = "bgred02.gif";
    public static final String EXPLODING_BLOB_IMAGE = "bgyel01.gif";
    public static final String SLIPPERY_BLOB_IMAGE = "bggrn01.gif";
    public static final String DEATHRAY_BLOB_IMAGE = "bgbrn07.gif";
    public static final String ANCHOR_POINT_IMAGE = "Anchor0b.gif";
    public static final String APCI_POINT_IMAGE = "bgblu01.gif";
    public static final String HOLE_IMAGE = "hole.PNG";

}
												
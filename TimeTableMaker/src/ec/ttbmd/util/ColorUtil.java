package ec.ttbmd.util;

import java.util.Arrays;

public class ColorUtil {
    
    private static final String[] COLOR_NAMES = new String[] {
        
        "purple",
        "darkblue",
	"lightblue",
	"darkgreen", 
	"softgreen",
	"lightgreen",
	"yellow", 
	"lightorange",
	"darkorange",
	"red",
        "pink"
    
    };
    
    private static final String[] COLOR_HEX = new String[] {
        
	"rgba(74, 20, 140, 1)",        
    	"rgba(13, 71, 161, 1)",
	"rgba(2, 136, 209, 1)",
	"rgba(0, 77, 64, 1)", 
	"rgba(46, 125, 50, 1)",
	"rgba(0, 230, 118, 1)",
	"rgba(255, 179, 0, 1)", 
	"rgba(245, 124, 0, 1)",
	"rgba(255, 61, 0, 1)",
	"rgba(183, 28, 28, 1)",
        "rgba(216, 27, 96, 1)"
    
    };
         
    public static String getHex(String color_name) {
        
        return COLOR_HEX[Arrays.asList(COLOR_NAMES).indexOf(color_name)];
        
    }
        
}

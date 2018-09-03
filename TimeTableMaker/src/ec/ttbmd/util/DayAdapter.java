package ec.ttbmd.util;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DayAdapter extends XmlAdapter<String, DayOfWeek> {

    public static String[] DAYS_OF_WEEK = new String[]{
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"            
    };
          
    @Override
        public DayOfWeek unmarshal(String v) throws Exception {      
        return DayOfWeek.of(Arrays.asList(DAYS_OF_WEEK).indexOf(v)+1);
    }

    @Override
        public String marshal(DayOfWeek v) throws Exception {
        return v.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("en-US"));
    }
    
}

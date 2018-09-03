package ec.ttbmd.util;

import static java.lang.Integer.parseInt;
import java.time.LocalTime;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    @Override
    public LocalTime unmarshal(String v) throws Exception {      
        return LocalTime.of(parseInt(v.substring(0, 2)), parseInt(v.substring(3)));
    }

    @Override
    public String marshal(LocalTime v) throws Exception {
        return TimeUtil.format(v);
    }
    
}

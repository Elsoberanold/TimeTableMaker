package ec.ttbmd.model;

import ec.ttbmd.util.DayAdapter;
import ec.ttbmd.util.LocalTimeAdapter;
import java.time.DayOfWeek;
import java.time.LocalTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Event {
    
    private final ObjectProperty<LocalTime> START;
    private final ObjectProperty<LocalTime> END;   
    private final ObjectProperty<DayOfWeek> DAY;
    
    private final StringProperty NAME;
    private final StringProperty SHORTNAME;
    private final StringProperty TYPE;
    private final StringProperty ROOM;
    private final StringProperty COLOR;
    
    public Event() {
        this(null, null, null, null);
    }
    /* The xml file should look like this
    <EVENT>
        <START>08H00</START>
        <END>09H00</END>
        <DAY>Monday</DAY>
        <NAME>Math</NAME>
        <SHORTNAME>M I</SHORTNAME> //same as Tag
        <TYPE>TP</TYPE>
        <ROOM>FMDUPA129</ROOM>
        <COLOR>lightorange</COLOR>
    </EVENT>    
    */
    public Event(String EvN, String EvTa, String EvC, String type) {
        
        this.START = new SimpleObjectProperty<LocalTime>(LocalTime.of(8, 0)); //* time picker
        this.END = new SimpleObjectProperty<LocalTime>(LocalTime.of(9, 0)); //* time picker       
        this.DAY = new SimpleObjectProperty<DayOfWeek>(DayOfWeek.MONDAY); //* dropdown menu
        
        this.TYPE = new SimpleStringProperty(type); //* dropdown menu
        
        /*
        T-Theoretical
        TP-Theoretical-Practical
        P-Practical
        */
        
        this.COLOR = new SimpleStringProperty(EvC); //color picker ou dropdown menu        
        
        this.NAME = new SimpleStringProperty(EvN); //*textfield
        this.SHORTNAME = new SimpleStringProperty(EvTa); //*textfield
        this.ROOM = new SimpleStringProperty("ROOMA129"); //* textfield

        
    }
    
    public String getNAME() {
        return NAME.get();
    }

    public void setNAME(String Tag) {
        this.NAME.set(Tag);
    }

    public StringProperty NAMEProperty() {
        return NAME;
    }
    
    public String getSHORTNAME() {
        return SHORTNAME.get();
    }

    public void setSHORTNAME(String Tag) {
        this.SHORTNAME.set(Tag);
    }

    public StringProperty SHORTNAMEProperty() {
        return SHORTNAME;
    }  
    
    public String getROOM() {
        return ROOM.get();
    }

    public void setROOM(String Room) {
        this.ROOM.set(Room);
    }

    public StringProperty ROOMProperty() {
        return ROOM;
    }
    
    public String getTYPE() {
        return TYPE.get();
    }

    public void setTYPE(String Type) {
        this.TYPE.set(Type);
    }

    public StringProperty TYPEProperty() {
        return TYPE;
    }
    
    public String getCOLOR() {
        return COLOR.get();
    }

    public void setCOLOR(String Color) {
        this.COLOR.set(Color);
    }

    public StringProperty COLORProperty() {
        return COLOR;
    }    
        
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public LocalTime getSTART() {
        return START.get();
    }

    public void setSTART(LocalTime Start) {
        this.START.set(Start);
    }

    public ObjectProperty<LocalTime> STARTProperty() {
        return START;
    }

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public LocalTime getEND() {
        return END.get();
    }

    public void setEND(LocalTime End) {
        this.END.set(End);
    }

    public ObjectProperty<LocalTime> ENDProperty() {
        return END;
    }
    
    @XmlJavaTypeAdapter(DayAdapter.class)
    public DayOfWeek getDAY() {
        return DAY.get();
    }

    public void setDAY(DayOfWeek Day) {
        this.DAY.set(Day);
    }

    public ObjectProperty<DayOfWeek> DAYProperty() {
        return DAY;
    }
    
}

package ec.ttbmd.util;

import ec.ttbmd.model.Event;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class CanvasUtil {
    
    private static int wkDays(boolean showWeekEnd) {
        if (showWeekEnd) {
            return DayAdapter.DAYS_OF_WEEK.length;
        } else {
            return DayAdapter.DAYS_OF_WEEK.length - 2;
        }
    }

    public static void drawBaseTable(GraphicsContext c, int nLines, double H, double W, boolean hhour, boolean weekends) {

        c.beginPath();

        int cont = 0;

        for (int i = 0; i < nLines; i++) {

            if (i == 0) {

                c.moveTo(W / (2 * (wkDays(weekends) + 1) - 1), 0);
                c.lineTo(W, 0);

            } else if (i == 1) {

            } else if (i == 2 || i == nLines - 1) {
                c.moveTo(0, i * (H / (nLines - 1)));
                c.lineTo(W, i * (H / (nLines - 1)));

            } else if (isOdd(i)) {
                c.moveTo(W / (2 * (wkDays(weekends) + 1) - 1), i * (H / (nLines - 1)));
                c.lineTo(W, i * (H / (nLines - 1)));

                int hora = 8 + cont;
                drawTextCentered(c, W / (2 * 2 * (wkDays(weekends) + 1) - 2), i * H / (nLines - 1), pad(hora) + ":00", 14);
                cont++;

            } else {

                if (hhour) {
                    c.moveTo(W / (2 * (wkDays(weekends) + 1) - 1), i * (H / (nLines - 1)));
                    c.lineTo(W, i * (H / (nLines - 1)));
                }

            }

        }

        for (int i = 0; i < 2 * (wkDays(weekends) + 1); i++) {

            if (i == 0) {

                c.moveTo(0, 2 * (H / (nLines - 1)));
                c.lineTo(0, H);

            }
            if (i == 1) {

                c.moveTo(W / (2 * (wkDays(weekends) + 1) - 1), 0);
                c.lineTo(W / (2 * (wkDays(weekends) + 1) - 1), H);

            } else if (isOdd(i)) {
                c.moveTo(i * W / (2 * (wkDays(weekends) + 1) - 1), 2 * (H / (nLines - 1)));
                c.lineTo(i * W / (2 * (wkDays(weekends) + 1) - 1), H);
            }

        }

        c.setStroke(Color.valueOf("rgba(200, 200, 200, 1)"));
        c.setLineWidth(1);
        c.stroke();

        for (int i = 0; i < wkDays(weekends); i++) {
            drawTextCentered(c, (i + 1) * 2 * W / (2 * (wkDays(weekends) + 1) - 1), H / (nLines - 1), DayAdapter.DAYS_OF_WEEK[i], 14);
        }

    }

    private static void PackEvents(ArrayList<ObservableList<Event>> columns, GraphicsContext ctx, double Hdist, double Wdist, int nTableLines, boolean weekends) {
        int n = columns.size();
        for (int i = 0; i < n; i++) {
            ObservableList<Event> col = columns.get(i);
            for (int j = 0; j < col.size(); j++) {

                Event bubble = col.get(j);
                drawEvent(ctx, bubble, Hdist, Wdist, nTableLines, 2 * (wkDays(weekends) + 1), n, i);

            }
        }
    }

    private static String pad(Integer n) {
        return (n < 10) ? '0' + n.toString() : n.toString();
    }

    private static boolean isOdd(int num) {
        return (num % 2) == 1;
    }

    private static boolean collidesWith(Event a, Event b) {

        return TimeUtil.toMinutes(a.getEND()) > TimeUtil.toMinutes(b.getSTART()) && TimeUtil.toMinutes(a.getSTART()) < TimeUtil.toMinutes(b.getEND());

    }

    private static void drawEvent(
            GraphicsContext c, Event event,
            double H, double W, int nLines, int nColumns, int nEvSTime, int xCordInc
    ) {

        double xEvent = W / (nColumns - 1) + (event.getDAY().getValue() - 1) * 2 * W / (nColumns - 1);//xEvent = W / (nColumns - 1) + wkDays.indexOf(day) * 2 * W / (nColumns - 1);

        double yEvent = (2 * (TimeUtil.timeDiff(LocalTime.of(8, 0), event.getSTART()) / 60) + 3) * (H / (nLines - 1));

        double wEvent = 2 * W / (nColumns - 1);

        double hEvent = 2 * (TimeUtil.timeDiff(event.getSTART(), event.getEND()) / 60) * (H / (nLines - 1));

        String roomName;

        wEvent = wEvent / nEvSTime;

        xEvent = xEvent + xCordInc * wEvent;

        c.beginPath();

        drawRCRect(c, xEvent, yEvent, wEvent - 3, hEvent - 3, 4, ColorUtil.getHex(event.getCOLOR()));

        c.setFont(Font.font("Segoe UI", FontWeight.BOLD, FontPosture.REGULAR, 14));
        c.setTextBaseline(VPos.TOP);
        c.setTextAlign(TextAlignment.LEFT);
        c.setFill(Color.valueOf("rgba(255, 255, 255, 1)"));

        if (nEvSTime != 1) {
            c.fillText(event.getSHORTNAME() + " – " + event.getTYPE(), xEvent + 5, yEvent + 2, wEvent - 16);
            roomName = event.getROOM().substring(5);
        } else {
            c.fillText(event.getNAME() + " – " + event.getTYPE(), xEvent + 5, yEvent + 2, wEvent - 16);
            roomName = event.getROOM();
        }

        c.setFont(Font.font("Segoe UI", FontWeight.BOLD, FontPosture.REGULAR, 12));
        c.setFill(Color.valueOf("rgba(255, 255, 255, 0.75)"));
        c.fillText(roomName, xEvent + 5, yEvent + 18, wEvent - 16);

    }

    private static void drawRCRect(GraphicsContext c, double x, double y, double w, double h, double r, String color) {

        c.beginPath();

        c.moveTo(x + r, y);
        c.lineTo(x + w - r, y);
        c.arcTo(x + w, y, x + w, y + r, r);
        c.lineTo(x + w, y + h - r);
        c.arcTo(x + w, y + h, x + w - r, y + h, r);
        c.lineTo(x + r, y + h);
        c.arcTo(x, y + h, x, y + h - r, r);
        c.lineTo(x, y + r);
        c.arcTo(x, y, x + r, y, r);

        c.setFill(Color.valueOf(color));

        c.fill();

    }

    public static void drawAllEvents(GraphicsContext ctx, ObservableList<Event> events, double Hdist, double Wdist, int nTableLines, boolean weekends) {

        ArrayList<ObservableList<Event>> columns = new ArrayList<>();

        Event lastEventEnding = null;

        for (Event e : events) {

            if (lastEventEnding != null && (TimeUtil.toMinutes(e.getSTART()) >= TimeUtil.toMinutes(lastEventEnding.getEND()) || e.getDAY() != lastEventEnding.getDAY())) {
                PackEvents(columns, ctx, Hdist, Wdist, nTableLines, weekends);
                columns.clear();
                lastEventEnding = null;
            }

            boolean placed = false;

            for (int i = 0; i < columns.size(); i++) {

                ObservableList<Event> col = columns.get(i);

                if (!collidesWith(col.get(col.size() - 1), e)) {
                    col.add(e);
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                ObservableList<Event> xxx = FXCollections.observableArrayList(e);
                columns.add(xxx);
            }

            if (lastEventEnding == null || TimeUtil.toMinutes(e.getEND()) > TimeUtil.toMinutes(lastEventEnding.getEND()) || e.getDAY() != lastEventEnding.getDAY()) {
                lastEventEnding = e;
            }

        }

        if (columns.size() > 0) {
            PackEvents(columns, ctx, Hdist, Wdist, nTableLines, weekends);
        }
    }

    private static void drawTextCentered(GraphicsContext c, double x, double y, String texto, int Sz) {
        c.setFill(Color.BLACK);
        c.setFont(Font.font("Roboto", FontWeight.LIGHT, FontPosture.REGULAR, Sz));
        c.setTextAlign(TextAlignment.CENTER);
        c.setTextBaseline(VPos.CENTER);
        c.fillText(texto, x, y);
        c.setFont(Font.font(texto, FontWeight.MEDIUM, FontPosture.REGULAR, Sz));
    }
    
    public static void updateEventsList(ObservableList<Event> events) {
        FXCollections.sort(events, (Event e1, Event e2) -> {
            if (e1.getDAY().getValue() < e2.getDAY().getValue()) {
                return -1;
            }
            if (e1.getDAY().getValue() > e2.getDAY().getValue()) {
                return 1;
            }
            if (TimeUtil.toMinutes(e1.getSTART()) < TimeUtil.toMinutes(e2.getSTART())) {
                return -1;
            }
            if (TimeUtil.toMinutes(e1.getSTART()) > TimeUtil.toMinutes(e2.getSTART())) {
                return 1;
            }
            if (TimeUtil.toMinutes(e1.getEND()) < TimeUtil.toMinutes(e2.getEND())) {
                return -1;
            }
            if (TimeUtil.toMinutes(e1.getEND()) > TimeUtil.toMinutes(e2.getEND())) {
                return 1;
            }
            return 0;
        });
    }

}

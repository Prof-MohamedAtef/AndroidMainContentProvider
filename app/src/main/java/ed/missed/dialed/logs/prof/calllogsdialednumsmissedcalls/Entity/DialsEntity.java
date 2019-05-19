package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Entity;

/**
 * Created by Prof-Mohamed Atef on 18/05/2019.
 */

public class DialsEntity {
    String Number, Type;

    public DialsEntity(String number, String type) {
        Number = number;
        Type = type;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}

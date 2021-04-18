package nextstep.subway.path.domain.enums;

public enum DistanceFarePolicy {

    TEN_KM(5, 10, 50), //40 = 5*8 800 40/5
    FIFTY_KM(8, 50, Integer.MAX_VALUE); //

    final private int chargeEveryNKm;
    final private int distanceFrom;
    final private int distanceTo;

    DistanceFarePolicy(int chargeEveryNKm, int distanceFrom, int distanceTo) {
        this.chargeEveryNKm = chargeEveryNKm;
        this.distanceFrom = distanceFrom;
        this.distanceTo = distanceTo;
    }

    public int getChargeEveryNKm() {
        return chargeEveryNKm;
    }

    public int getDistanceFrom() {
        return distanceFrom;
    }

    public int getDistanceTo() {
        return distanceTo;
    }

    public int getMaximumFare(){
        if(getDistanceTo()==Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return ((getDistanceTo()-getDistanceFrom())/getChargeEveryNKm())*100;
    }

}

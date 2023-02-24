package nextstep.subway.utils;

public enum PathServiceType {
    DISTANCE;

    public static boolean isDistance(String type){
        return DISTANCE.name().equals(type);
    }
}

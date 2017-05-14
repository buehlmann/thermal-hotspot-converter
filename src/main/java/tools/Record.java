package tools;

public class Record {

    private double latitude;
    private double longitude;
    private double height;
    private double probability;
    private double rank;

    public Record(double latitude, double longitude, double height, double probability, double rank) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.probability = probability;
        this.rank = rank;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getHeight() {
        return height;
    }

    public double getProbability() {
        return probability;
    }

    public double getRank() {
        return rank;
    }
}

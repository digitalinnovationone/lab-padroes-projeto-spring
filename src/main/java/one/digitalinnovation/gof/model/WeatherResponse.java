package one.digitalinnovation.gof.model;

public class WeatherResponse {
    private String time;
    private double temperature;
    private double windspeed;
    private double latitude;
    private double longitude;

    public WeatherResponse(String time, double temperature, double windspeed, double latitude, double longitude) {
        this.time = time;
        this.temperature = temperature;
        this.windspeed = windspeed;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
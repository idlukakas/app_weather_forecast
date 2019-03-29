package br.com.bossini.fatec_ipi_noite_weather_forecast;

public class City {
    private String name;

    public City() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

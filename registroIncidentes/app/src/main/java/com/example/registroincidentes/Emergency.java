package com.example.registroincidentes;

public class Emergency {

    private Integer idEmergency;
    private String Name;
    private String Location;
    private String RegisterDate;
    private Double Latitude;
    private Double Longitude;
    private Double Altitude;
    private String EmergencyType;

    private String EmergencyType2;
    private Boolean IsClosed;
    private String ClosedDate;
    private String EmergencyCol;

    public Integer getIdEmergency() {
        return idEmergency;
    }
    public void setIdEmergency(Integer idEmergency) {
        this.idEmergency = idEmergency;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }
    public void setLocation(String location) {
        Location = location;
    }

    public String getRegisterDate() {
        return RegisterDate;
    }
    public void setRegisterDate(String registerDate) {
        RegisterDate = registerDate;
    }

    public Double getLatitude() {
        return Latitude;
    }
    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }
    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getAltitude() {
        return Altitude;
    }
    public void setAltitude(Double altitude) {
        Altitude = altitude;
    }

    public String getEmergencyType() {
        return EmergencyType;
    }
    public void setEmergencyType(String emergencyType) {
        EmergencyType = emergencyType;
    }

    public String getEmergencyType2() {
        return EmergencyType2;
    }
    public void setEmergencyType2(String emergencyType2) {
        EmergencyType2 = emergencyType2;
    }

    public Boolean getClosed() {
        return IsClosed;
    }
    public void setClosed(Boolean closed) {
        IsClosed = closed;
    }

    public String getClosedDate() {
        return ClosedDate;
    }
    public void setClosedDate(String closedDate) {
        ClosedDate = closedDate;
    }

    public String getEmergencyCol() {
        return EmergencyCol;
    }
    public void setEmergencyCol(String emergencyCol) {
        EmergencyCol = emergencyCol;
    }
}

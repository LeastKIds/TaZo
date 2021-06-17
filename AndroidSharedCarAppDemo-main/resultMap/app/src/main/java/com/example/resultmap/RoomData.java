package com.example.resultmap;


public class RoomData {


    private String id;
    private String name;
    private String userLimit;
    private String gender;
    private String startAt;
    private String originLat;
    private String originLng;
    private String destinationLat;
    private String destinationLng;


    public RoomData(String id, String name, String userLimit, String gender,
                    String startAt, String originLat, String originLng,
                    String destinationLat, String destinationLng) {
        this.name = name;
        this.userLimit = userLimit;
        this.gender = gender;
        this.startAt = startAt;
        this.originLat = originLat;
        this.originLng = originLng;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(String userLimit) {
        this.userLimit = userLimit;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getOriginLat() {
        return originLat;
    }

    public void setOriginLat(String originLat) {
        this.originLat = originLat;
    }

    public String getOriginLng() {
        return originLng;
    }

    public void setOriginLng(String originLng) {
        this.originLng = originLng;
    }

    public String getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(String destinationLat) {
        this.destinationLat = destinationLat;
    }

    public String getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(String destinationLng) {
        this.destinationLng = destinationLng;
    }

    public String getPlace() {
        if(originLng != null)
            return originLat + " / " + originLng;
        else
            return destinationLat + " / " + destinationLng;
    }

    public void setId(String id)
    {
        this.id=id;
    }
    public String getId()
    {
        return id;
    }
}


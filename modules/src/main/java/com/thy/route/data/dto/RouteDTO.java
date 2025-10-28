package com.thy.route.data.dto;

import java.util.List;

public class RouteDTO {
    private List<TransportationDTO> transportations;
    private String totalDuration;
    private Double totalCost;

    public RouteDTO() {
    }

    public RouteDTO(List<TransportationDTO> transportations) {
        this.transportations = transportations;
    }

    public List<TransportationDTO> getTransportations() {
        return transportations;
    }

    public void setTransportations(List<TransportationDTO> transportations) {
        this.transportations = transportations;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}
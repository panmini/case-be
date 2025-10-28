package com.thy.route.exception;

import com.thy.exception.BaseException;

public class RouteNotFoundException extends BaseException {
    public RouteNotFoundException(String origin, String destination) {
        super("ROUTE_NOT_FOUND",
                "No valid routes found from " + origin + " to " + destination);
    }
}
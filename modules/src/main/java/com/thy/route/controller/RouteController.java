package com.thy.route.controller;

import com.thy.route.data.dto.RouteDTO;
import com.thy.route.service.impl.RouteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);

    @Autowired
    private RouteService routeService;

    @GetMapping
    public List<RouteDTO> findRoutes(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        return routeService.findValidRoutes(origin, destination, date);
    }
}


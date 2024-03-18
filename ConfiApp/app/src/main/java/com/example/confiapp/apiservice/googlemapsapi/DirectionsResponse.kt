package com.example.confiapp.apiservice.googlemapsapi

data class DirectionsResponse(
    val routes: List<Route>
)

data class Route(
    val overviewPolyline: Polyline
)

data class Polyline(
    val encodedPath: String
)

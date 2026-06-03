package com.parkinglot;

import com.parkinglot.spot.ParkingSpot;
import com.parkinglot.vehicle.Vehicle;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

public final class SpotFinder {
    private SpotFinder() {
    }

    public static ParkingSpot findNearestSpot(Collection<ParkingSpot> spots, Vehicle vehicle) {
        PriorityQueue<ParkingSpot> available = new PriorityQueue<ParkingSpot>(
                Comparator.comparingInt(ParkingSpot::getDistanceToEntrance)
        );

        for (ParkingSpot spot : spots) {
            if (spot.canFit(vehicle)) {
                available.add(spot);
            }
        }

        return available.isEmpty() ? null : available.poll();
    }
}

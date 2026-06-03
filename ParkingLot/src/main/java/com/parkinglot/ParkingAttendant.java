package com.parkinglot;

import com.parkinglot.pricing.PricingStrategy;
import com.parkinglot.ticket.Ticket;
import com.parkinglot.vehicle.Vehicle;

public class ParkingAttendant {
    private final ParkingLot parkingLot;

    public ParkingAttendant(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public Ticket park(Vehicle vehicle) {
        return parkingLot.parkVehicle(vehicle);
    }

    public double collectPayment(Ticket ticket, PricingStrategy strategy) {
        return parkingLot.exitVehicle(ticket, strategy);
    }
}

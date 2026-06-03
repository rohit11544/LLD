package com.parkinglot;

import com.parkinglot.display.DisplayBoardObserver;
import com.parkinglot.display.ParkingLotStatus;
import com.parkinglot.floor.Floor;
import com.parkinglot.pricing.PricingStrategy;
import com.parkinglot.spot.ParkingSpot;
import com.parkinglot.ticket.Ticket;
import com.parkinglot.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private static ParkingLot instance;

    private final List<Floor> floors = new ArrayList<Floor>();
    private final List<DisplayBoardObserver> observers = new ArrayList<DisplayBoardObserver>();
    private PricingStrategy defaultPricingStrategy;
    private int ticketCounter;

    public static synchronized ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot();
        }
        return instance;
    }

    private ParkingLot() {
        this.ticketCounter = 0;
    }

    public void addFloor(Floor floor) {
        floors.add(floor);
    }

    public void setDefaultPricingStrategy(PricingStrategy pricingStrategy) {
        this.defaultPricingStrategy = pricingStrategy;
    }

    public void subscribe(DisplayBoardObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        ParkingLotStatus status = new ParkingLotStatus(getAvailableSpotCount(), getTotalSpotCount());
        for (DisplayBoardObserver observer : observers) {
            observer.update(status);
        }
    }

    public Ticket parkVehicle(Vehicle vehicle) {
        return parkVehicle(vehicle, LocalDateTime.now());
    }

    public Ticket parkVehicle(Vehicle vehicle, LocalDateTime entryTime) {
        ParkingSpot spot = findAvailableSpot(vehicle);
        if (spot == null) {
            if (getAvailableSpotCount() == 0) {
                System.out.println("Parking lot FULL — no empty spots");
            } else {
                System.out.println("No compatible spot for " + vehicle.getType()
                        + " (" + vehicle.getLicensePlate()
                        + "). Empty spots exist but wrong size/type.");
            }
            notifyObservers();
            return null;
        }

        if (!spot.park(vehicle)) {
            return null;
        }

        Ticket ticket = new Ticket(generateTicketId(), spot, vehicle, entryTime);
        System.out.println("Ticket issued: " + ticket.getTicketId()
                + " | spot: " + spot.getId()
                + " | vehicle: " + vehicle.getLicensePlate());
        notifyObservers();
        return ticket;
    }

    public double exitVehicle(Ticket ticket) {
        return exitVehicle(ticket, defaultPricingStrategy);
    }

    public double exitVehicle(Ticket ticket, PricingStrategy strategy) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        if (strategy == null) {
            throw new IllegalArgumentException("Pricing strategy cannot be null");
        }

        LocalDateTime exitTime = ticket.getExitTime() != null
                ? ticket.getExitTime()
                : LocalDateTime.now();
        if (ticket.getExitTime() == null) {
            ticket.markExit(exitTime);
        }
        ticket.calculateFee(strategy);

        ParkingSpot spot = ticket.getSpot();
        spot.unpark();

        ticket.markPaid();
        System.out.println("Exit fee for ticket " + ticket.getTicketId() + ": $" + ticket.getFee());
        notifyObservers();
        return ticket.getFee();
    }

    public ParkingSpot findAvailableSpot(Vehicle vehicle) {
        for (Floor floor : floors) {
            ParkingSpot spot = floor.findAvailableSpot(vehicle);
            if (spot != null) {
                return spot;
            }
        }
        return null;
    }

    public int getAvailableSpotCount() {
        int count = 0;
        for (Floor floor : floors) {
            count += floor.getEmptySpotCount();
        }
        return count;
    }

    public int getTotalSpotCount() {
        int count = 0;
        for (Floor floor : floors) {
            count += floor.getTotalSpots();
        }
        return count;
    }

    private Floor findFloorForSpot(ParkingSpot spot) {
        for (Floor floor : floors) {
            if (floor.getSpot(spot.getId()) != null) {
                return floor;
            }
        }
        return null;
    }

    private String generateTicketId() {
        ticketCounter++;
        return "T" + String.format("%04d", ticketCounter);
    }

    public static synchronized void resetForTesting() {
        instance = null;
    }
}

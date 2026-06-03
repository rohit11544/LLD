package com.parkinglot;

import com.parkinglot.display.EntranceDisplayBoard;
import com.parkinglot.factory.ParkingSpotFactory;
import com.parkinglot.floor.Floor;
import com.parkinglot.pricing.FlatRatePricing;
import com.parkinglot.pricing.HourlyPricing;
import com.parkinglot.pricing.WeekendPricing;
import com.parkinglot.spot.SpotType;
import com.parkinglot.ticket.Ticket;
import com.parkinglot.vehicle.Vehicle;
import com.parkinglot.vehicle.VehicleType;

import java.time.LocalDateTime;

public class ParkingLotDemo {
    public static void main(String[] args) {
        ParkingLot lot = ParkingLot.getInstance();
        lot.subscribe(new EntranceDisplayBoard());
        lot.setDefaultPricingStrategy(new HourlyPricing(2.0, 30.0));

        Floor floor1 = new Floor("F1");
        floor1.addSpot(ParkingSpotFactory.createSpot(SpotType.MOTORCYCLE, "M1", 5));
        floor1.addSpot(ParkingSpotFactory.createSpot(SpotType.COMPACT, "C1", 10));
        floor1.addSpot(ParkingSpotFactory.createSpot(SpotType.HANDICAPPED, "H1", 8));
        floor1.addSpot(ParkingSpotFactory.createSpot(SpotType.LARGE, "L1", 15));

        Floor floor2 = new Floor("F2");
        floor2.addSpot(ParkingSpotFactory.createSpot(SpotType.COMPACT, "C2", 12));
        floor2.addSpot(ParkingSpotFactory.createSpot(SpotType.LARGE, "L2", 20));

        lot.addFloor(floor1);
        lot.addFloor(floor2);

        ParkingAttendant attendant = new ParkingAttendant(lot);

        System.out.println("\n--- Park car (compact) ---");
        LocalDateTime carEntry = LocalDateTime.of(2026, 5, 23, 10, 0);
        Ticket carTicket = lot.parkVehicle(new Vehicle("ABC123", VehicleType.CAR), carEntry);

        System.out.println("\n--- Park truck (large) ---");
        Ticket truckTicket = attendant.park(new Vehicle("TRK999", VehicleType.TRUCK));

        System.out.println("\n--- Park motorcycle ---");
        Ticket bikeTicket = attendant.park(new Vehicle("BIKE01", VehicleType.MOTORCYCLE));

        System.out.println("\n--- Exit car with hourly pricing (2.5 hrs @ $2/hr) ---");
        carTicket.markExit(LocalDateTime.of(2026, 5, 23, 12, 30));
        lot.exitVehicle(carTicket, new HourlyPricing(2.0, 30.0));

        System.out.println("\n--- Exit truck with flat rate ---");
        lot.exitVehicle(truckTicket, new FlatRatePricing(10.0));

        System.out.println("\n--- Weekend pricing example (Saturday, 2 hrs @ 1.5x) ---");
        Vehicle weekendCar = new Vehicle("WKD001", VehicleType.CAR);
        LocalDateTime saturdayEntry = LocalDateTime.of(2026, 5, 23, 10, 0);
        Ticket weekendTicket = lot.parkVehicle(weekendCar, saturdayEntry);
        if (weekendTicket != null) {
            WeekendPricing weekendPricing = new WeekendPricing(2.0, 1.5);
            weekendTicket.markExit(LocalDateTime.of(2026, 5, 23, 12, 0));
            lot.exitVehicle(weekendTicket, weekendPricing);
        }

        System.out.println("\n--- Fill lot to test FULL display (cars only) ---");
        lot.exitVehicle(bikeTicket, new FlatRatePricing(5.0));
        System.out.println("Motorcycle exited — all car-sized spots free except none occupied");

        while (lot.findAvailableSpot(new Vehicle("X", VehicleType.CAR)) != null) {
            int before = lot.getAvailableSpotCount();
            Ticket t = lot.parkVehicle(new Vehicle("FILL" + before, VehicleType.CAR));
            if (t == null) {
                break;
            }
        }
        System.out.println("Car spots exhausted. Remaining empty: "
                + lot.getAvailableSpotCount() + " (large spots — trucks only)");
    }
}

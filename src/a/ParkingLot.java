package a;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

enum Commands {
    create_parking_lot, park, leave, status, registration_numbers_for_cars_with_colour, slot_numbers_for_cars_with_colour, slot_number_for_registration_number
}

class Vehicle {
    private String regNumber;
    private String color;

    Vehicle(String regNumber, String color) {
        this.regNumber = regNumber;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getRegNumber() {
        return regNumber;
    }
}

/**
 * @author ashesh.pandey
 */
public class ParkingLot {

    Map<Integer, Vehicle> vehicleHashMap = new HashMap<>();
    int totalNumberOfSlots = 0;
    public static final String INVALID_COMMANDS = "INVALID COMMANDS";
    public static final String SPLIT_REGEX = " ";

    private int getFreeSlot(ParkingLot parkingLot) {
        int freeSlot = 0;
        for (int i = 1; i <= parkingLot.totalNumberOfSlots; i++) {
            if (!parkingLot.vehicleHashMap.containsKey(i)) {
                freeSlot = i;
                break;
            }
        }
        return freeSlot;
    }

    private static boolean enumContainsValue(String value)
    {
        for (Commands cmd : Commands.values())
        {
            if (cmd.name().equals(value))
            {
                return true;
            }
        }
        return false;
    }

    private void createParkingLot(int numberOfSlots) {
        totalNumberOfSlots += numberOfSlots;
    }

    public int allotSlotNumber(Vehicle v, ParkingLot parkingLot) {
        int freeSlot = getFreeSlot(parkingLot);
        if (freeSlot > totalNumberOfSlots || freeSlot == 0) {
            return -1;
        } else {
            vehicleHashMap.put(freeSlot, v);
        }
        return freeSlot;
    }

    private void freeSlot(int slotNumber) {
        vehicleHashMap.remove(slotNumber);
    }

    private List<String> registrationNumbersWithColor(String color) {
        List<String> regNumbers = new ArrayList<>();
        for(Map.Entry<Integer, Vehicle> entry : vehicleHashMap.entrySet()) {
            Vehicle value = entry.getValue();
            if (value.getColor().equals(color)) {
                regNumbers.add(value.getRegNumber());
            }
        }
        return regNumbers;
    }

    private int getSlotNumberWithRegNumber(String regNumber) {
        for(Map.Entry<Integer, Vehicle> entry : vehicleHashMap.entrySet()) {
            int key = entry.getKey();
            Vehicle value = entry.getValue();
            if (value.getRegNumber().equals(regNumber)) {
                return key;
            }
        }
        return -1;
    }

    private List<Integer> getSlotNumberWithColor(String color) {
        List<Integer> colors = new ArrayList<>();
        for(Map.Entry<Integer, Vehicle> entry : vehicleHashMap.entrySet()) {
            int key = entry.getKey();
            Vehicle value = entry.getValue();
            if (value.getColor().equals(color)) {
                colors.add(key);
            }
        }
        return colors;
    }

    private void getStatusOfParkingLot() {
        System.out.println("Slot No." + " " + "Registration No" + " " + "Colour");
        for(Map.Entry<Integer, Vehicle> entry : vehicleHashMap.entrySet()) {
            int key = entry.getKey();
            Vehicle value = entry.getValue();
            System.out.println(key + " " + value.getRegNumber() + " " + value.getColor());
        }
    }

    public static  void main(String[] args) throws FileNotFoundException {

        Scanner sc = new Scanner(System.in);
        String cmd = "";
        List<String> commandList;
        ParkingLot parkingLot = new ParkingLot();

        do {
            try {
                cmd = sc.nextLine();
            } catch (Exception e) {
                cmd = "exit";
                System.out.println(e.getMessage());
            }
            commandList = Arrays.asList(cmd.split(SPLIT_REGEX));

            if (cmd.contains(".txt")) {
                List<String> commandLines = getCommands(cmd);
                for (String commandLine : commandLines) {
                    List<String> commandArgs = Arrays.asList(commandLine.split(SPLIT_REGEX));
                    processCommands(commandArgs, parkingLot);
                }
            } else if (cmd.contains("exit")) {
                break;
            } else
            {
                processCommands(commandList, parkingLot);
            }
        } while (true);
    }

    private static void processCommands(List<String> commands, ParkingLot parkingLot) {
        String command = commands.get(0);
        if (enumContainsValue(command)) {
            Commands cmd = Commands.valueOf(command);
            switch (cmd) {
                case create_parking_lot:
                    parkingLot.createParkingLot(Integer.parseInt(commands.get(1)));
                    System.out.println("Created a parking slot with " + commands.get(1) + " slots");
                    break;
                case park:
                    String regNumber = commands.get(1);
                    String color = commands.get(2);
                    Vehicle v = new Vehicle(regNumber, color);
                    int allottedSlot = parkingLot.allotSlotNumber(v, parkingLot);
                    if (allottedSlot != -1) {
                        System.out.println("Allocated slot number: " + allottedSlot);
                    } else {
                        System.out.println("Sorry, parking lot is full");
                    }
                    break;
                case leave:
                    parkingLot.freeSlot(Integer.parseInt(commands.get(1)));
                    System.out.println("Slot number " + commands.get(1) + " is free");
                    break;
                case status:
                    parkingLot.getStatusOfParkingLot();
                    break;
                case registration_numbers_for_cars_with_colour:
                    String numbersList = parkingLot.registrationNumbersWithColor(commands.get(1)).toString();
                    System.out.println(numbersList.substring(1, numbersList.length()-1));
                    break;
                case slot_numbers_for_cars_with_colour:
                    if(!parkingLot.getSlotNumberWithColor(commands.get(1)).isEmpty()) {
                        String numberList = parkingLot.getSlotNumberWithColor(commands.get(1)).toString();
                        System.out.println(numberList.substring(1, numberList.length() - 1));
                    } else {
                        System.out.println("Not found");
                    }
                    break;
                case slot_number_for_registration_number:
                    if (parkingLot.getSlotNumberWithRegNumber(commands.get(1)) != -1) {
                        System.out.println(parkingLot.getSlotNumberWithRegNumber(commands.get(1)));
                    } else {
                        System.out.println("Not found");
                    }
                    break;
                default:
                    System.out.println(INVALID_COMMANDS);
            }
        } else {
            System.out.println(INVALID_COMMANDS);
        }
    }

    private static List<String> getCommands(String fileName) throws FileNotFoundException {
        if(fileName == null) return new ArrayList<>();
        File file = new File(fileName);
        if(! (file.exists() && file.canRead())) {
            System.err.println("File not found");
            return new ArrayList<>();
        }

        List<String> commandLines = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            commandLines.add(scanner.nextLine());
        }

        scanner.close();

        return commandLines;
    }
}

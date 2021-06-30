package a;

import javafx.application.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;


/**
 * @author ashesh.pandey
 */
public class ParkingLotTest {

    @Before
    public void bef() {
//        ParkingLot.totalNumberOfSlots = 6;
    }

    @Test
    public void testAllotSlotNumber() {
        ParkingLot pl = new ParkingLot();
        pl.totalNumberOfSlots = 6;
        Vehicle v = new Vehicle("abc", "xyz");
        assertEquals(1, pl.allotSlotNumber(v, pl));
    }

    @Test
    public void testMainMethod() throws FileNotFoundException {
        String userInput = "inputs.txt";
        ByteArrayInputStream is = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(is);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(os));
        ParkingLot.main(null);
        System.setOut(oldOut);
        String output = os.toString();
        assert(output.contains("KA01HH9999"));
    }

    @Test
    public void testRegistrationNumbersWithColor() {
        ParkingLot parkingLot = new ParkingLot();
        Vehicle v1 = new Vehicle("ABC", "green");
        Vehicle v2 = new Vehicle("XYZ", "white");
        parkingLot.vehicleHashMap.put(1, v1);
        parkingLot.vehicleHashMap.put(2, v2);
        assertEquals(1, parkingLot.registrationNumbersWithColor("white").size());
    }

    @Test
    public void testGetSlotNumberWithRegNumber() {
        ParkingLot parkingLot = new ParkingLot();
        Vehicle v1 = new Vehicle("ABC", "green");
        Vehicle v2 = new Vehicle("XYZ", "white");
        parkingLot.vehicleHashMap.put(1, v1);
        parkingLot.vehicleHashMap.put(2, v2);
        assertEquals(1, parkingLot.getSlotNumberWithRegNumber("ABC"));
    }

//    @After
//    public void aft() throws FileNotFoundException {
//        String exitInput = "exit";
//        ByteArrayInputStream bais1 = new ByteArrayInputStream(exitInput.getBytes());
//        System.setIn(bais1);
//        ParkingLot.main(null);
//    }
}

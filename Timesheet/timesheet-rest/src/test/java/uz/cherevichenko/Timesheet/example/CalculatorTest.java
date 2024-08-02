package uz.cherevichenko.Timesheet.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    @Test
    void testSum(){
        Calculator calculator = new Calculator();
        int actual = calculator.sum(5, 10);
        int expected = 15;
        assertEquals(expected, actual);
    }
}

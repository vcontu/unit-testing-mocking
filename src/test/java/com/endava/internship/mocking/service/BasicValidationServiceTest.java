package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BasicValidationServiceTest {

    ValidationService testService;

    @BeforeEach
    void setUp(){
        testService = new BasicValidationService();
    }

    @Test
    void whenValidateNullAmount_thenThrowIllegalArgumentException(){
        Exception actualException = assertThrows(IllegalArgumentException.class,
                                                            () -> testService.validateAmount(null));
        assertEquals("Amount must not be null", actualException.getMessage());
    }

    @Test
    void whenValidateNegativeOrEqualZeroAmount_thenThrowIllegalArgumentException(){
        Exception actualException = assertThrows(IllegalArgumentException.class,
                () -> testService.validateAmount(-2.0));
        assertEquals("Amount must be greater than 0", actualException.getMessage());
    }

    @Test
    void whenValidateNullPaymentId_thenThrowIllegalArgumentException(){
        Exception actualException = assertThrows(IllegalArgumentException.class,
                () -> testService.validatePaymentId(null));
        assertEquals("Payment id must not be null", actualException.getMessage());
    }

    @Test
    void whenValidateNullUserId_thenThrowIllegalArgumentException(){
        Exception actualException = assertThrows(IllegalArgumentException.class,
                () -> testService.validateUserId(null));
        assertEquals("User id must not be null", actualException.getMessage());
    }

    @Test
    void whenValidateInactiveUser_thenThrowIllegalArgumentException(){
        User invalidUser = new User(1, "test user", Status.INACTIVE);
        Exception actualException = assertThrows(IllegalArgumentException.class,
                () -> testService.validateUser(invalidUser));
        assertEquals("User with id 1 not in ACTIVE status", actualException.getMessage());
    }

    @Test
    void whenValidateNullMessage_thenThrowIllegalArgumentException(){
        Exception actualException = assertThrows(IllegalArgumentException.class,
                () -> testService.validateMessage(null));
        assertEquals("Payment message must not be null", actualException.getMessage());
    }

    @Test
    void whenValidateCorrectAmount_thenNoExceptionIsThrown(){
        assertDoesNotThrow(() -> testService.validateAmount(1.0));
    }

    @Test
    void whenValidateCorrectPaymentId_thenNoExceptionIsThrown(){
        assertDoesNotThrow(() -> testService.validatePaymentId(UUID.randomUUID()));
    }

    @Test
    void whenValidateCorrectUserId_thenNoExceptionIsThrown(){
        assertDoesNotThrow(() -> testService.validateUserId(1));
    }

    @Test
    void whenValidateCorrectUser_thenNoExceptionIsThrown(){
        User validUser = new User(1, "Test user", Status.ACTIVE);
        assertDoesNotThrow(() -> testService.validateUser(validUser));
    }
    @Test
    void whenValidateCorrectMessage_thenNoExceptionIsThrown(){
        assertDoesNotThrow(() -> testService.validateMessage("Test message"));
    }


}

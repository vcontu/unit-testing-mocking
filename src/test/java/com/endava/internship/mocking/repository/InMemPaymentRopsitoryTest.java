package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InMemPaymentRopsitoryTest {

    PaymentRepository testPaymentRepository;
    Payment testPayment1;
    Payment testPayment2;

    @BeforeEach
    void setUp(){
        testPaymentRepository = new InMemPaymentRepository();
        testPayment1 = new Payment(1, 1.0, "message");
        testPayment2 = new Payment(2, 2.0, "message");
    }

    @Test
    void whenSaveNullUser_thenThrowIllegalArgumentException(){
        Exception actualException = assertThrows(IllegalArgumentException.class,
                        () -> testPaymentRepository.save(null));
        assertEquals("Payment must not be null", actualException.getMessage());
    }

    @Test
    void whenSaveExistentUser_thenThrowIllegalArgumentException(){
        testPaymentRepository.save(testPayment1);

        Exception actualException = assertThrows(IllegalArgumentException.class,
                () -> testPaymentRepository.save(testPayment1));

        assertEquals("Payment with id " + testPayment1.getPaymentId() + "already saved", actualException.getMessage());
    }

    @Test
    void whenSaveNewPayment_thenReturnSamePaymentAndFindItById(){
        assertEquals(testPayment1,
                testPaymentRepository.save(testPayment1));
        assertEquals(testPayment1,
                testPaymentRepository.findById(testPayment1.getPaymentId()).get());
    }

    @Test
    void whenFindByNullId_thenThrowIllegalArgumentException(){
        Exception exception = assertThrows(IllegalArgumentException.class,
                                () -> testPaymentRepository.findById(null));
        assertEquals("Payment id must not be null", exception.getMessage());
    }

    @Test
    void whenFindByExistentId_thenReturnCopyOfPayment(){
        testPaymentRepository.save(testPayment1);
        testPaymentRepository.save(testPayment2);

        Optional<Payment> foundPayment = testPaymentRepository.
                                            findById(testPayment1.getPaymentId());

        assertTrue(foundPayment.isPresent());
        assertEquals(testPayment1, foundPayment.get());
    }

    @Test
    void whenFindAll_thenReturnAllPaymentsAdded(){
        testPaymentRepository.save(testPayment1);
        testPaymentRepository.save(testPayment2);
        List<Payment> expectedResult = List.of(testPayment1, testPayment2);

        List<Payment> foundPayments = testPaymentRepository.findAll();

        assertThat(foundPayments).hasSize(2);
        assertThat(foundPayments).containsOnlyElementsOf(expectedResult);
    }

    @Test
    void whenEditNullMessage_thenThrowIllegalArgumentException(){

        final UUID testId = UUID.randomUUID();
        Exception actualException = assertThrows(NoSuchElementException.class,
                () -> testPaymentRepository.editMessage(testId, "Test message"));
        assertEquals("Payment with id " + testId + " not found", actualException.getMessage());
    }

    @Test
    void whenEditMessage_thenPaymentMessageChanges(){
        Payment testPayment = Mockito.mock(Payment.class);
        when(testPayment.getPaymentId()).thenReturn(UUID.randomUUID());
        String testMessage = "New test";
        testPaymentRepository.save(testPayment);
        testPaymentRepository.editMessage(testPayment.getPaymentId(), testMessage);

        verify(testPayment).setMessage(testMessage);
    }

}

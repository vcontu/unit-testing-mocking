package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Payment;
import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import com.endava.internship.mocking.repository.InMemPaymentRepository;
import com.endava.internship.mocking.repository.InMemUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Captor
    ArgumentCaptor<Payment> paymentCaptor;

    @Mock
    private InMemUserRepository userRepositoryMock;

    @Mock
    private InMemPaymentRepository paymentRepositoryMock;

    @Mock
    private ValidationService validationServiceMock;

    @InjectMocks
    private PaymentService testPaymentService;

    Double testAmount = 1.0;
    Integer testUserId = 1;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createPayment() {

        when(userRepositoryMock.findById(testUserId)).
                thenReturn(Optional.of(new User(testUserId, "John", Status.ACTIVE)));
        testPaymentService.createPayment(testUserId, testAmount);

        verify(paymentRepositoryMock).save(paymentCaptor.capture());
        Payment savedPayment = paymentCaptor.getValue();

        verify(validationServiceMock).validateUserId(testUserId);
        verify(validationServiceMock).validateAmount(testAmount);
        verify(userRepositoryMock).findById(testUserId);
        assertThat(savedPayment.getAmount()).isEqualTo(testAmount);
        assertThat(savedPayment.getUserId()).isEqualTo(testUserId);
    }

    @Test
    void editMessage() {
        when(userRepositoryMock.findById(testUserId)).
                thenReturn(Optional.of(new User(testUserId, "John", Status.ACTIVE)));
        testPaymentService.createPayment(testUserId, testAmount);
        verify(paymentRepositoryMock).save(paymentCaptor.capture());
        Payment savedPayment = paymentCaptor.getValue();
        String testNewMessage = "New message";

        testPaymentService.editPaymentMessage(savedPayment.getPaymentId(),
                testNewMessage);

        verify(validationServiceMock).validatePaymentId(savedPayment.getPaymentId());
        verify(validationServiceMock).validateMessage(testNewMessage);
        verify(paymentRepositoryMock).editMessage(savedPayment.getPaymentId(), testNewMessage);
    }

    @Test
    void getAllByAmountExceeding() {
        Double greaterAmount = 3.0;
        Integer greaterUserId = 2;
        Payment greaterPayment = new Payment(greaterUserId, greaterAmount, "A message");
        when(paymentRepositoryMock.findAll())
                .thenReturn(List.of(new Payment(testUserId, testAmount, "Sera was here"),
                                    greaterPayment));

        List<Payment> returnedPayments = testPaymentService.getAllByAmountExceeding(2.0);

        assertThat(returnedPayments).containsOnly(greaterPayment);
    }
}

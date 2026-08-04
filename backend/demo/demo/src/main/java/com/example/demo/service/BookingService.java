package com.example.demo.service;



import com.example.demo.dto.booking.BookingHistoryResponse;
import com.example.demo.dto.booking.BookingRequest;
import com.example.demo.dto.booking.BookingResponse;
import java.util.List;

public interface BookingService {


    BookingResponse createBooking(
            BookingRequest request
    );
    List<BookingHistoryResponse> getBookingHistory(String passengerNic);

}
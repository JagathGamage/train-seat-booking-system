package com.example.demo.controler;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.booking.BookingHistoryResponse;
import com.example.demo.dto.booking.BookingRequest;
import com.example.demo.dto.booking.BookingResponse;
import com.example.demo.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {


    private final BookingService bookingService;



    @PostMapping
    public BookingResponse createBooking(
            @RequestBody BookingRequest request
    ){

        return bookingService.createBooking(request);

    }

    @GetMapping("/history")
    public List<BookingHistoryResponse> getBookingHistory(

            @RequestParam String nic

    ) {

        return bookingService.getBookingHistory(nic);

    }



}

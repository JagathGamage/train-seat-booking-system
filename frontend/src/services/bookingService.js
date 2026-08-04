import api from "../api/axios";

export const createBooking = (booking) => {

    return api.post("/bookings", booking);

};

export const getBookingHistory = (nic) => {

    return api.get(`/bookings/history?nic=${nic}`);

};
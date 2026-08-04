import {
    Box,
    Card,
    CardContent,
    Typography,
    Grid,
    TextField,
    Button,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Alert,
    Snackbar,
    Tabs,
    Tab,
    CircularProgress
} from "@mui/material";

import EventSeatIcon from "@mui/icons-material/EventSeat";
import TrainIcon from "@mui/icons-material/Train";

import { useEffect, useState, useMemo } from "react";
import { useLocation, useNavigate } from "react-router-dom";

import { getAvailableSeats } from "../services/seatService";
import { createBooking } from "../services/bookingService";

import "./BookingPage.css";

function BookingPage() {
    const location = useLocation();
    const navigate = useNavigate();

    const {
        scheduleId,
        origin,
        destination,
        travelDate
    } = location.state || {};

    const [seats, setSeats] = useState([]);
    const [selectedSeat, setSelectedSeat] = useState(null);
    const [passengerName, setPassengerName] = useState("");
    const [passengerNic, setPassengerNic] = useState("");
    const [loading, setLoading] = useState(false);
    const [successBooking, setSuccessBooking] = useState(null);
    const [error, setError] = useState("");

    // Active coach tab state
    const [selectedCoach, setSelectedCoach] = useState("");

    useEffect(() => {
        if (scheduleId && origin && destination) {
            loadSeats();
        }
    }, []);

    async function loadSeats() {
        try {
            setLoading(true);
            const response = await getAvailableSeats(
                scheduleId,
                origin,
                destination
            );
            const loadedSeats = response.data || [];
            setSeats(loadedSeats);

            // Automatically select the first available coach tab
            if (loadedSeats.length > 0) {
                const firstCoach = loadedSeats[0]?.coachName || "";
                setSelectedCoach(firstCoach);
            }
        } catch (err) {
            setError("Unable to load available seats");
        } finally {
            setLoading(false);
        }
    }

    // Group seats dynamically by coachName
    const seatsByCoach = useMemo(() => {
        return seats.reduce((acc, seat) => {
            const coach = seat.coachName || "General";
            if (!acc[coach]) {
                acc[coach] = [];
            }
            acc[coach].push(seat);
            return acc;
        }, {});
    }, [seats]);

    // Unique coach list for tab labels
    const coachNames = Object.keys(seatsByCoach);

    function selectSeat(seat) {
        setSelectedSeat(seat);
    }

    const handleTabChange = (event, newCoach) => {
        setSelectedCoach(newCoach);
    };

    async function handleBooking() {
        if (!selectedSeat) {
            setError("Please select a seat");
            return;
        }

        if (!passengerName || !passengerNic) {
            setError("Please enter passenger details");
            return;
        }

        const bookingRequest = {
            scheduleId: scheduleId,
            seatId: selectedSeat.seatId,
            originStationId: origin,
            destinationStationId: destination,
            passengerName: passengerName,
            passengerNic: passengerNic
        };

        try {
            const response = await createBooking(bookingRequest);
            setSuccessBooking(response.data);
        } catch (err) {
            setError("Seat booking failed. Please try again.");
        }
    }

    return (
        <Box className="bookingPage">
            <Card className="bookingCard">
                <CardContent>
                    <Typography variant="h4" className="title">
                        <TrainIcon sx={{ mr: 1, verticalAlign: "middle" }} />
                        Confirm Your Journey
                    </Typography>

                    <Box className="journeyBox">
                        <Typography>Date : {travelDate}</Typography>
                        <Typography>From : {origin}</Typography>
                        <Typography>To : {destination}</Typography>
                    </Box>

                    <Typography variant="h5" sx={{ mt: 4, mb: 2 }}>
                        Select Seat
                    </Typography>

                    {loading ? (
                        <Box sx={{ textAlign: "center", my: 4 }}>
                            <CircularProgress />
                        </Box>
                    ) : coachNames.length > 0 ? (
                        <>
                            {/* Coach Tabs Navigation */}
                            <Box sx={{ borderBottom: 1, borderColor: "divider", mb: 3 }}>
                                <Tabs
                                    value={selectedCoach}
                                    onChange={handleTabChange}
                                    variant="scrollable"
                                    scrollButtons="auto"
                                    aria-label="Coach Tabs"
                                >
                                    {coachNames.map((coach) => (
                                        <Tab
                                            key={coach}
                                            label={`Coach ${coach}`}
                                            value={coach}
                                            sx={{ fontWeight: "bold", fontSize: "16px" }}
                                        />
                                    ))}
                                </Tabs>
                            </Box>

                            {/* Seats Grid for Selected Coach */}
                            <Grid container spacing={2}>
                                {(seatsByCoach[selectedCoach] || []).map((seat) => (
                                    <Grid
                                        item
                                        xs={4}
                                        sm={3}
                                        md={2}
                                        key={seat.seatId}
                                    >
                                        <Card
                                            className={
                                                selectedSeat?.seatId === seat.seatId
                                                    ? "seat selected"
                                                    : "seat"
                                            }
                                            onClick={() => selectSeat(seat)}
                                        >
                                            <EventSeatIcon />
                                            <Typography sx={{ fontWeight: "bold" }}>
                                                {seat.seatNumber}
                                            </Typography>
                                            <Typography variant="caption">
                                                {seat.coachName}
                                            </Typography>
                                        </Card>
                                    </Grid>
                                ))}
                            </Grid>
                        </>
                    ) : (
                        <Alert severity="info">No available seats found for this journey.</Alert>
                    )}

                    <Typography variant="h5" sx={{ mt: 5, mb: 2 }}>
                        Passenger Details
                    </Typography>

                    <Grid container spacing={3}>
                        <Grid item xs={12} md={6}>
                            <Typography className="fieldLabel">Passenger Name</Typography>
                            <TextField
                                fullWidth
                                placeholder="Enter full name"
                                value={passengerName}
                                onChange={(e) => setPassengerName(e.target.value)}
                                className="customInputField"
                            />
                        </Grid>

                        <Grid item xs={12} md={6}>
                            <Typography className="fieldLabel">NIC</Typography>
                            <TextField
                                fullWidth
                                placeholder="Enter NIC number"
                                value={passengerNic}
                                onChange={(e) => setPassengerNic(e.target.value)}
                                className="customInputField"
                            />
                        </Grid>
                    </Grid>

                    <Box textAlign="center" mt={5}>
                        <Button
                            variant="contained"
                            size="large"
                            onClick={handleBooking}
                            disabled={!selectedSeat || !passengerName || !passengerNic}
                        >
                            Confirm Booking
                        </Button>
                    </Box>
                </CardContent>
            </Card>

            <Snackbar
                open={Boolean(error)}
                autoHideDuration={3000}
                onClose={() => setError("")}
            >
                <Alert severity="error">{error}</Alert>
            </Snackbar>

            <Dialog open={Boolean(successBooking)}>
                <DialogTitle>🎉 Booking Successful</DialogTitle>
                <DialogContent>
                    <Typography>Booking ID : {successBooking?.bookingId}</Typography>
                    <Typography>Coach : {successBooking?.coachName}</Typography>
                    <Typography>Seat : {successBooking?.seatNumber}</Typography>
                    <Typography>Fare : Rs. {successBooking?.fare}</Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => navigate("/")}>Done</Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
}

export default BookingPage;
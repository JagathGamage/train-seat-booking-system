import {
    Box,
    Card,
    CardContent,
    Typography,
    TextField,
    Button,
    Grid,
    Chip,
    CircularProgress,
    Alert
} from "@mui/material";


import TrainIcon from "@mui/icons-material/Train";
import EventSeatIcon from "@mui/icons-material/EventSeat";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import PaymentsIcon from "@mui/icons-material/Payments";


import { useState } from "react";

import { getBookingHistory } from "../services/bookingService";

import "./BookingHistory.css";



function BookingHistory(){


    const [nic,setNic] = useState("");

    const [bookings,setBookings] = useState([]);

    const [loading,setLoading] = useState(false);

    const [error,setError] = useState("");




    async function searchBookings(){


        if(!nic){

            setError(
                "Please enter NIC"
            );

            return;

        }


        try{


            setLoading(true);

            setError("");


            const response =
                await getBookingHistory(nic);


            setBookings(
                response.data
            );


        }
        catch(err){


            setError(
                "No bookings found"
            );


        }
        finally{

            setLoading(false);

        }


    }





    return (


        <Box className="historyPage">


            <Card className="historyContainer">


                <CardContent>


                    <Typography
                        variant="h4"
                        className="historyTitle"
                    >

                        <TrainIcon/>

                        My Train Bookings

                    </Typography>





                    <Box className="searchBox">


                        <TextField

                            fullWidth

                            label="Enter Passenger NIC"

                            value={nic}

                            onChange={(e)=>
                                setNic(e.target.value)
                            }

                        />


                        <Button

                            variant="contained"

                            onClick={searchBookings}

                        >

                            Search

                        </Button>


                    </Box>





                    {
                        loading &&

                        <Box
                            textAlign="center"
                            mt={4}
                        >

                            <CircularProgress/>

                        </Box>

                    }





                    {
                        error &&

                        <Alert
                            severity="error"
                            sx={{
                                mt:3
                            }}
                        >

                            {error}

                        </Alert>

                    }







                    <Grid
                        container
                        spacing={3}
                        mt={2}
                    >


                    {
                        bookings.map((booking)=>(


                            <Grid
                                item
                                xs={12}
                                key={booking.bookingId}
                            >


                            <Card
                                className="ticketCard"
                            >


                            <CardContent>



                                <Box className="ticketHeader">


                                    <Typography
                                        variant="h6"
                                    >

                                        🚆
                                        {" "}
                                        {booking.trainName}

                                    </Typography>



                                    <Chip

                                        label={
                                            booking.status
                                        }

                                        color="success"

                                    />


                                </Box>






                                <Typography
                                    className="date"
                                >

                                    Travel Date :
                                    {" "}
                                    {booking.travelDate}

                                </Typography>





                                <Box className="route">


                                    <Box>

                                    <LocationOnIcon/>

                                    <Typography>

                                        {booking.origin}

                                    </Typography>


                                    </Box>




                                    <Typography
                                        className="arrow"
                                    >

                                        ➜

                                    </Typography>




                                    <Box>


                                    <LocationOnIcon/>

                                    <Typography>

                                        {booking.destination}

                                    </Typography>


                                    </Box>



                                </Box>







                                <Grid
                                    container
                                    spacing={2}
                                    mt={2}
                                >



                                <Grid
                                    item
                                    xs={12}
                                    md={4}
                                >

                                <Box className="infoBox">


                                    <EventSeatIcon/>


                                    <Typography>

                                        Coach
                                        {" "}
                                        {booking.coachName}

                                        <br/>

                                        Seat
                                        {" "}
                                        {booking.seatNumber}

                                    </Typography>


                                </Box>


                                </Grid>







                                <Grid
                                    item
                                    xs={12}
                                    md={4}
                                >

                                <Box className="infoBox">


                                    <PaymentsIcon/>


                                    <Typography>

                                        Fare

                                        <br/>

                                        Rs.
                                        {" "}
                                        {booking.fare}

                                    </Typography>


                                </Box>


                                </Grid>



                                </Grid>



                            </CardContent>


                            </Card>



                            </Grid>



                        ))
                    }



                    </Grid>




                </CardContent>


            </Card>


        </Box>


    );

}


export default BookingHistory;
import { Button, Card, CardContent, Container, Typography, Box } from "@mui/material";
import DirectionsRailwayFilledIcon from "@mui/icons-material/DirectionsRailwayFilled";
import EventSeatIcon from "@mui/icons-material/EventSeat";
import HistoryIcon from "@mui/icons-material/History";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import { useNavigate } from "react-router-dom";
import "./Home.css";

function Home() {
  const navigate = useNavigate();

  return (
    <Box className="home">

      <Container maxWidth="lg">

        <Box className="hero">

          <Typography
            variant="h2"
            className="title"
          >
            Train Seat Booking System
          </Typography>

          <Typography
            variant="h6"
            className="subtitle"
          >
            Colombo Fort – Badulla Reserved Seat Booking
          </Typography>

          <Typography
            className="description"
          >
            Book reserved train seats for any journey segment.
            A seat becomes available again after passengers leave,
            maximizing seat utilization and improving passenger convenience.
          </Typography>

          <Button
            variant="contained"
            size="large"
            endIcon={<ArrowForwardIcon />}
            className="startButton"
            onClick={() => navigate("/search")}
          >
            Start Booking
          </Button>

        </Box>


        <Box className="cardContainer">

          <Card
            className="featureCard"
            onClick={() => navigate("/search")}
          >

            <CardContent>

              <DirectionsRailwayFilledIcon className="cardIcon" />

              <Typography variant="h5">
                Search Journey
              </Typography>

              <Typography className="cardText">
                Select travel date, origin and destination,
                then find available reserved seats instantly.
              </Typography>

            </CardContent>

          </Card>


          <Card
            className="featureCard"
            onClick={() => navigate("/")}
          >

            <CardContent>

              <EventSeatIcon className="cardIcon" />

              <Typography variant="h5">
                Book Seat
              </Typography>

              <Typography className="cardText">
                Reserve your preferred seat for your selected journey segment.
              </Typography>

            </CardContent>

          </Card>


          <Card
            className="featureCard"
            onClick={() => navigate("/history")}
          >

            <CardContent>

              <HistoryIcon className="cardIcon" />

              <Typography variant="h5">
                Booking History
              </Typography>

              <Typography className="cardText">
                View all your previous reservations using your NIC.
              </Typography>

            </CardContent>

          </Card>

        </Box>

      </Container>

    </Box>
  );
}

export default Home;
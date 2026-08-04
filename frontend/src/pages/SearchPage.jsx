import {
  Box,
  Card,
  CardContent,
  Typography,
  Grid,
  TextField,
  Button,
  MenuItem,
  CircularProgress,
  Alert,
  Select,
  FormControl
} from "@mui/material";

import SearchIcon from "@mui/icons-material/Search";
import TrainIcon from "@mui/icons-material/Train";

import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getStations } from "../services/stationService";
import { getSchedules } from "../services/scheduleService";

import "./SearchPage.css";

function SearchPage() {
  const navigate = useNavigate();

  const [stations, setStations] = useState([]);
  const [travelDate, setTravelDate] = useState("");
  const [origin, setOrigin] = useState("");
  const [destination, setDestination] = useState("");
  const [scheduleId, setScheduleId] = useState("");
  const [schedules, setSchedules] = useState([]);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    loadStations();
  }, []);

  async function loadStations() {
    try {
      const response = await getStations();
      setStations(response.data || []);
    } catch (err) {
      console.error(err);
    }
  }

  async function loadSchedules(date) {
    if (!date) return;

    try {
      setLoading(true);
      const response = await getSchedules(date);
      setSchedules(response.data || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }

  function searchSeats() {
    if (!travelDate || !origin || !destination || !scheduleId) {
      setError("Please select all journey details");
      return;
    }
    setError("");
    navigate("/booking", {
      state: {
        travelDate,
        origin,
        destination,
        scheduleId
      }
    });
  }

  return (
    <Box className="searchPage">
      <Card className="searchCard">
        <CardContent>
          <Typography variant="h4" className="heading">
            <TrainIcon sx={{ mr: 1 }} />
            Journey Search
          </Typography>

          <Grid container spacing={3}>
            {/* Travel Date */}
            <Grid item xs={12} md={6}>
              <Typography className="fieldLabel">Travel Date</Typography>
              <TextField
                fullWidth
                type="date"
                value={travelDate}
                onChange={(e) => {
                  setError("");
                  setTravelDate(e.target.value);
                  loadSchedules(e.target.value);
                }}
                className="customInputField"
              />
            </Grid>

            {/* Origin Station */}
            <Grid item xs={12} md={6}>
              <Typography className="fieldLabel">Origin Station</Typography>
              <FormControl fullWidth className="customInputField">
                <Select
                  displayEmpty
                  value={origin}
                  onChange={(e) => {
                    setError("");
                    setOrigin(e.target.value);
                  }}
                  renderValue={(selected) => {
                    if (!selected) {
                      return <span className="placeholderText">Select Origin Station</span>;
                    }
                    const found = stations.find((s) => s.id === selected);
                    return found ? found.stationName : selected;
                  }}
                >
                  <MenuItem value="" disabled>
                    Select Origin Station
                  </MenuItem>
                  {stations.map((station) => (
                    <MenuItem key={station.id} value={station.id}>
                      {station.stationName}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            {/* Destination Station */}
            <Grid item xs={12} md={6}>
              <Typography className="fieldLabel">Destination Station</Typography>
              <FormControl fullWidth className="customInputField">
                <Select
                  displayEmpty
                  value={destination}
                  onChange={(e) => {
                    setError("");
                    setDestination(e.target.value);
                  }}
                  renderValue={(selected) => {
                    if (!selected) {
                      return <span className="placeholderText">Select Destination Station</span>;
                    }
                    const found = stations.find((s) => s.id === selected);
                    return found ? found.stationName : selected;
                  }}
                >
                  <MenuItem value="" disabled>
                    Select Destination Station
                  </MenuItem>
                  {stations.map((station) => (
                    <MenuItem key={station.id} value={station.id}>
                      {station.stationName}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            {/* Train Schedule */}
            <Grid item xs={12} md={6}>
              <Typography className="fieldLabel">Train Schedule</Typography>
              <FormControl fullWidth className="customInputField">
                <Select
                  displayEmpty
                  value={scheduleId}
                  onChange={(e) => {
                    setError("");
                    setScheduleId(e.target.value);
                  }}
                  renderValue={(selected) => {
                    if (!selected) {
                      return <span className="placeholderText">Select Train Schedule</span>;
                    }
                    const found = schedules.find((s) => s.scheduleId === selected);
                    return found
                      ? `${found.trainName} (${found.departureTime})`
                      : selected;
                  }}
                >
                  <MenuItem value="" disabled>
                    Select Train Schedule
                  </MenuItem>
                  {schedules.map((schedule) => (
                    <MenuItem key={schedule.scheduleId} value={schedule.scheduleId}>
                      {schedule.trainName} ({schedule.departureTime})
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
          </Grid>

          <Box sx={{ textAlign: "center", mt: 4 }}>
            <Button
              variant="contained"
              className="searchButton"
              startIcon={<SearchIcon />}
              disabled={!travelDate || !origin || !destination || !scheduleId}
              onClick={searchSeats}
            >
              Find Available Seats
            </Button>
          </Box>

          {loading && (
            <Box sx={{ textAlign: "center", mt: 4 }}>
              <CircularProgress />
            </Box>
          )}

          {error && (
            <Alert severity="warning" sx={{ mt: 3 }}>
              {error}
            </Alert>
          )}
        </CardContent>
      </Card>
    </Box>
  );
}

export default SearchPage;
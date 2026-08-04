import api from "../api/axios";

export const getStations = () => {
    return api.get("/stations");
};
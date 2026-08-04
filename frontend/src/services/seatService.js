import api from "../api/axios";

export const getAvailableSeats = (
    scheduleId,
    originId,
    destinationId
) => {

    return api.get("/seats/available", {
        params: {
            scheduleId,
            originStationId: originId,
            destinationStationId: destinationId
        }
    });

};
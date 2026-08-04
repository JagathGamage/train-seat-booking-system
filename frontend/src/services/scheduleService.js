import api from "../api/axios";

export const getSchedules = (date) => {
    return api.get(`/schedules?date=${date}`);
};
const API = "http://localhost:8080/concert_hall";
export const GET_SHOWS = API + "/shows";
export const GET_SEAT_INFO = (showId) => API + "/seats/?showID=" + showId;
export const POST_TICKETS = (showId, front, middle, back) => API + "/tickets/?showID=" + showId +
    "&front=" + front +
    "&middle=" + middle +
    "&back=" + back;
export const GET_BALANCE = API + "/balance";

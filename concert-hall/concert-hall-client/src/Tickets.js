import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {GET_SEAT_INFO, POST_TICKETS} from "./api";
import {Card, Container, Input, Paper} from "@material-ui/core";
import './App.css';
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";


const Tickets = () => {
    const {showId} = useParams();
    const [seatInfo, setSeatInfo] = useState({});
    const [backTickets, setBackTickets] = useState(0);
    const [middleTickets, setMiddleTickets] = useState(0);
    const [frontTickets, setFrontTickets] = useState(0);

    useEffect(() => {
        fetch(GET_SEAT_INFO(showId))
            .then(response => response.json())
            .then((data) => {
                console.log(data);
                setSeatInfo(data);
            });
    }, [showId]);

    const renderTicketMenu = () => {

        return seatInfo.Back ?
            <div className="selector">
                <Paper className="priceMenu">
                    <Typography>Front Seats</Typography>
                    <Typography>{seatInfo.Back.first} RON</Typography>
                    <Input
                        value={backTickets}
                        onChange={(event) => setBackTickets(event.target.value)}
                        inputProps={{
                            step: 1,
                            min: 0,
                            max: seatInfo.Back.second,
                            type: 'number',
                        }}
                    />
                </Paper>
                <Paper className="priceMenu">
                    <Typography>Middle Seats</Typography>
                    <Typography>{seatInfo.Middle.first} RON</Typography>
                    <Input
                        value={middleTickets}
                        onChange={(event) => setMiddleTickets(event.target.value)}
                        inputProps={{
                            step: 1,
                            min: 0,
                            max: seatInfo.Middle.second,
                            type: 'number',
                        }}
                    />
                </Paper>
                <Paper className="priceMenu">
                    <Typography>Front Seats</Typography>
                    <Typography>{seatInfo.Front.first} RON</Typography>
                    <Input
                        value={frontTickets}
                        onChange={(event) => setFrontTickets(event.target.value)}
                        inputProps={{
                            step: 1,
                            min: 0,
                            max: seatInfo.Front.second,
                            type: 'number',
                        }}
                    />
                </Paper>
            </div> : <></>;
    };

    const getTotalPrice = () => {
        if (backTickets === 0 && middleTickets === 0 && frontTickets === 0)
            return 0.0;
        else
            return backTickets * seatInfo.Back.first +
                middleTickets * seatInfo.Middle.first +
                frontTickets * seatInfo.Front.first;
    };

    const buyTickets = () => {
        fetch(POST_TICKETS(showId,frontTickets,middleTickets,backTickets), {method: 'POST'})
            .then(() => alert("Reserved " + backTickets + " back seats, " +
                middleTickets + " middle seats and " +
                frontTickets + " front seats successfully !"))
            .catch(() => alert("Not enough seats, please try again"))
            .finally(() => {
                window.location.reload();
            });
    };

    return (
        <Container className="container">
            {renderTicketMenu()}
            <Card className="buyCard">
                <Typography>Total Price: {getTotalPrice().toFixed(2)}</Typography>
                <Button color="primary"
                        disabled={getTotalPrice() === 0.00} onClick={buyTickets}
                        variant="contained">Buy</Button>
            </Card>
        </Container>);
};

export default Tickets;

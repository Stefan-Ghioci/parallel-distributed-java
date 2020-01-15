import React, {useEffect, useState} from 'react';
import './App.css';
import {Card, Container} from "@material-ui/core";
import Typography from "@material-ui/core/Typography";
import {GET_BALANCE, POST_TICKETS} from "./api";

function getRandomInt(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

const Test = () => {
        const [values, setValues] = useState({balance: 0, calculated: 0});
        const [logger, setLogger] = useState([]);


        useEffect(() => {
            setTimeout(() => {
                // const frontTickets = getRandomInt(0, 10);
                // const middleTickets = getRandomInt(0, 10);
                // const backTickets = getRandomInt(0, 10);
                // const showId = getRandomInt(0, 2);
                //
                // fetch(GET_BALANCE)
                //     .then(response => response.json())
                //     .then((data) => {
                //         setValues({
                //             balance: data[0],
                //             calculated: data[1]
                //         });
                //     })
                //     .finally(() => {
                //             let message = fetch(POST_TICKETS(showId, frontTickets, middleTickets, backTickets), {method: 'POST'})
                //                 .then(() => message = "Reserved " + backTickets + " back seats, " +
                //                     middleTickets + " middle seats and " +
                //                     frontTickets + " front seats for show with ID=" + showId)
                //                 .catch(() => "Not enough seats, please try again");
                //             setLogger([...logger, message]);
                //         }
                //     );

                const frontTickets = getRandomInt(0, 10);
                const middleTickets = getRandomInt(0, 10);
                const backTickets = getRandomInt(0, 10);
                const showId = getRandomInt(0, 2);

                const balancePromise = fetch(GET_BALANCE)
                    .then(response => response.json());
                const messagePromise = fetch(POST_TICKETS(showId, frontTickets, middleTickets, backTickets), {method: 'POST'})
                    .then((response) => {
                        console.log(response);
                        return response.ok
                            ? "Reserved " + backTickets + " back seats, " +
                            middleTickets + " middle seats and " +
                            frontTickets + " front seats for show with ID=" + showId
                            : "Not enough seats, please try again";
                    });

                Promise.all([messagePromise, balancePromise])
                    .then(data => {
                        console.log(data);
                        setLogger([...logger, data[0]]);
                        setValues({balance: data[1][0], calculated: data[1][1]});
                    });
            }, 5000)
        }, [logger]);


        const showLogger = () => logger.map(message => <Typography>{message}</Typography>);

        return (
            <Container className="container">
                <Card className="priceMenu">
                    <Typography>
                        Balance: {values.balance.toFixed(2)}
                    </Typography>
                    <Typography>
                        Calculated: {values.calculated.toFixed(2)}
                    </Typography>
                </Card>
                <Card className="buyCard">
                    {showLogger()}
                </Card>
            </Container>
        );
    }
;

export default Test;

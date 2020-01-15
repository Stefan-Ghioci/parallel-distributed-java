import React, {useEffect, useState} from 'react';
import './App.css';
import Container from "@material-ui/core/Container";
import {Paper} from "@material-ui/core";
import Typography from "@material-ui/core/Typography";
import Link from "@material-ui/core/Link";
import {GET_SHOWS} from "./api";

const Home = () => {
    const [shows, setShows] = useState([]);

    useEffect(() => {
        fetch(GET_SHOWS)
            .then(response => response.json())
            .then((data) => {
                console.log(data);
                setShows(data);
            });
    }, []);

    const renderShows = () => shows.map(show =>
        <Link href={'/shows/' + show.id} underline="none">
            <Paper className={"showListItem"}>
                <Typography style={{padding: 10}} variant="h5">
                    {show.title} ({new Date(show.date).toLocaleDateString("en-GB")})
                </Typography>
                <Typography style={{padding: 10}} variant="body1">
                    {show.desc}
                </Typography>
            </Paper>
        </Link>)
    ;

    return (
        <Container className="container" maxWidth="md">
            <Typography variant="h3">Select a show:</Typography>
            {shows === [] ? <p>Select a show</p> : renderShows()}
        </Container>
    );
};

export default Home;

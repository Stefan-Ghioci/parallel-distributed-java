import React from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import Tickets from "./Tickets";
import Home from "./Home";

const App = () => {
    return (
        <Router>
            <Switch>
                <Route path="/shows/:showId">
                    <Tickets/>
                </Route>
                <Route path="/">
                    <Home/>
                </Route>
            </Switch>
        </Router>
    );
};

export default App;

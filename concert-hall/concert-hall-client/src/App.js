import React from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import Tickets from "./Tickets";
import Home from "./Home";
import Test from "./Test";

const App = () => {
    return (
        <Router>
            <Switch>
                <Route path="/shows/:showId">
                    <Tickets/>
                </Route>
                <Route path="/test">
                    <Test/>
                </Route>
                <Route path="/">
                    <Home/>
                </Route>
            </Switch>
        </Router>
    );
};

export default App;

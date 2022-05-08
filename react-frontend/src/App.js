import React, {Component} from "react";
import {connect} from "react-redux";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import Login from "./components/login.component";
import {clearMessage} from "./actions/message";
import {logout} from "./actions/auth";
import {history} from './helpers/history';
import Profile from "./components/profile.component";
import Register from "./components/register.component";
import Home from "./components/home.component";
import BoardUser from "./components/board-user.component";
import Navbar from "./components/navbar.component";


class App extends Component {
    constructor(props) {
        super(props);
        this.logOut = this.logOut.bind(this);
        this.state = {
            showModeratorBoard: false,
            showAdminBoard: false,
            currentUser: undefined,
        };
        history.listen((location) => {
            props.dispatch(clearMessage()); // clear message when changing location
        });
    }

    componentDidMount() {
        const user = this.props.user;
        if (user) {
            this.setState({
                currentUser: user,
                showAdminBoard: user.roles.includes("ROLE_ADMIN"),
            });
        }
    }

    logOut() {
        this.props.dispatch(logout());
    }

    render() {
        const {currentUser, showAdminBoard} = this.state;
        return (
            <BrowserRouter location={history.location} navigator={history}>
                <div>
                    <Navbar showAdminBoard={showAdminBoard} currentUser={currentUser} onClick={this.logOut}/>
                    <div className="container mt-3">
                        <Routes>
                            <Route exact path="/" element={<Home/> }/>
                            <Route exact path="/login" element={<Login/>}/>
                            <Route exact path="/register" element={<Register/>}/>
                            <Route exact path="/profile" element={<Profile/>}/>
                            <Route path="/user" element={<BoardUser/>}/>
                        </Routes>
                    </div>
                </div>
            </BrowserRouter>
        );
    }
}

function mapStateToProps(state) {
    const {user} = state.auth;
    return {
        user,
    };
}

export default connect(mapStateToProps)(App);
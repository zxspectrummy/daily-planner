import React, {Component} from 'react';
import {Link} from "react-router-dom";

class Navbar extends Component {
    render() {
        return <nav className="navbar navbar-expand navbar-dark bg-dark">
            <Link to={"/"} className="navbar-brand">
                Daily Planner
            </Link>
            <div className="navbar-nav mr-auto">
                <li className="nav-item">
                    <Link to={"/home"} className="nav-link">
                        Home
                    </Link>
                </li>
                {this.props.showAdminBoard && (
                    <li className="nav-item">
                        <Link to={"/admin"} className="nav-link">
                            Admin Board
                        </Link>
                    </li>
                )}
                {this.props.currentUser && (
                    <li className="nav-item">
                        <Link to={"/user"} className="nav-link">
                            User
                        </Link>
                    </li>
                )}
            </div>
            {this.props.currentUser ? (
                <div className="navbar-nav ml-auto">
                    <li className="nav-item">
                        <Link to={"/profile"} className="nav-link">
                            {this.props.currentUser.username}
                        </Link>
                    </li>
                    <li className="nav-item">
                        <a href="/login" className="nav-link" onClick={this.props.onClick}>
                            LogOut
                        </a>
                    </li>
                </div>
            ) : (
                <div className="navbar-nav ml-auto">
                    <li className="nav-item">
                        <Link as={Link} to={"/login"} className="nav-link">
                            Login
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link as={Link} to={"/register"} className="nav-link">
                            Sign Up
                        </Link>
                    </li>
                </div>
            )}
        </nav>;
    }
}



export default Navbar;
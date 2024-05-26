import React, { useState } from 'react';
import { fetchGet, fetchPost } from "../utils/api";
import { useMainContext } from "../utils/Contexts";
import { useNavigate } from 'react-router-dom';
import './css/LoginPage.css'; // Import your CSS file for styling

const LoginPage = () => {
    const { login } = useMainContext();
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: ''
    });
    const [error, setError] = useState('');
    const [msg, setMsg] = useState('Hello World');
    const [showLogin, setShowLogin] = useState(true);
    const navigate = useNavigate();

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const url = `loginServlet?name=${formData.name}&password=${formData.password}`;
            const response = await fetchGet(url);
            console.log('User data:', response);
            if (response.status === "ok") {
                login(response);
                setMsg(`Hello ${formData.name}! You are logged in!`);
                setError('');
                navigate('/profile');
            } else {
                setError(response.message);
            }
        } catch (error) {
            console.error('Login failed:', error.message);
            setError('Login failed: ' + error.message);
        }
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        if (!formData.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
            setError('Please enter a valid email address.');
            return;
        }
        try {
            const body = JSON.stringify({
                email: formData.email,
                name: formData.name,
                password: formData.password
            });
            const url = "loginServlet";
            const response = await fetchPost(url, body);
            console.log('User data:', response);
            if (response.status === "ok") {
                login(response);
                setMsg(`Hello ${formData.name}! You are registered and logged in!`);
                setError('');
                navigate('/profile');
            } else {
                setError(response.message);
            }
        } catch (error) {
            console.error('Registration failed:', error.message);
            setError('Registration failed: ' + error.message);
        }
    };

    const toggleForm = () => {
        setShowLogin(!showLogin);
        setError('');
    };

    return (
        <div className="login-page">
            <h1>{msg}</h1>
            {showLogin ? (
                <div className="login-form">
                    <h2>Login</h2>
                    <form onSubmit={handleLogin}>
                        <input
                            type="text"
                            name="name"
                            placeholder="Name"
                            value={formData.name}
                            onChange={handleInputChange}
                        />
                        <input
                            type="password"
                            name="password"
                            placeholder="Password"
                            value={formData.password}
                            onChange={handleInputChange}
                        />
                        <button type="submit">Login</button>
                        {error && <p className="error">{error}</p>}
                    </form>
                    <button className="toggle-button" onClick={toggleForm}>
                        Don't have an account?   Sign up
                    </button>
                </div>
            ) : (
                <div className="register-form">
                    <h2>Register</h2>
                    <form onSubmit={handleRegister}>
                        <input
                            type="email"
                            name="email"
                            placeholder="Email"
                            value={formData.email}
                            onChange={handleInputChange}
                        />
                        <input
                            type="text"
                            name="name"
                            placeholder="Name"
                            value={formData.name}
                            onChange={handleInputChange}
                        />
                        <input
                            type="password"
                            name="password"
                            placeholder="Password"
                            value={formData.password}
                            onChange={handleInputChange}
                        />
                        <button type="submit">Register</button>
                        {error && <p className="error">{error}</p>}
                    </form>
                    <button className="toggle-button" onClick={toggleForm}>
                        Already have an account?   Login
                    </button>
                </div>
            )}
        </div>
    );
};

export default LoginPage;

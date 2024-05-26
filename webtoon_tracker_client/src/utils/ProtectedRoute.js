import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useMainContext } from './Contexts';
import { HashLoader} from 'react-spinners';
import { css } from '@emotion/react';

const ProtectedRoute = () => {
    const { userData, loading } = useMainContext();

    const override = css`
        display: block;
        margin: 0 auto;
        border-color: red;
    `;

    if (loading) {
        return <HashLoader color={'#123abc'} loading={true} css={override} size={60} />; // You can use a spinner or a loading component here
    }

    return userData ? <Outlet /> : <Navigate to="/login" />;
};

export default ProtectedRoute;

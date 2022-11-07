import {Routes,Route,BrowserRouter} from "react-router-dom";
import Login from "./components/Auth/Login";
import Dashboard from "./Dashboard";




export const RouteList = () => {
    return (
        <BrowserRouter>
        <Routes>
            <Route path = '/' element = {<Login />} />
            <Route path = '/dashboard' element = {<Dashboard />} />
            
        </Routes>
        </BrowserRouter>
    )
}
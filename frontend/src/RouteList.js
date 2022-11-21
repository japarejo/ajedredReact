import {Routes,Route,BrowserRouter} from "react-router-dom";
import Login from "./components/Auth/Login";
import Register from "./components/Auth/Register";
import CreateGame from "./components/Games/CreateGame";
import Dashboard from "./Dashboard";
import AwaitGame from "./components/Games/AwaitGame";
import Game from "./components/Games/Game";




export const RouteList = () => {
    return (
        <BrowserRouter>
        <Routes>
            <Route path = '/' element = {<Login />} />
            <Route path = '/register' element = {<Register />} />
            <Route path = '/dashboard' element = {<Dashboard />} />
            <Route path = '/games/create' element = {<CreateGame />} />
            <Route path = '/games/:id/awaitGame' element = {<AwaitGame />} />
            <Route path = '/games/:id' element = {<Game />} />
            
        </Routes>
        </BrowserRouter>
    )
}
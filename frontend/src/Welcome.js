import React from 'react';

import 'bootstrap/dist/css/bootstrap.min.css';



export default function Welcome() {

    
        return(
           
            <div className="form-signin container col-auto p-5 text-center display:flex; justify-content: center;">
              <br></br>
              <br></br>
              <br></br>
            <h1 className="h3 mb-3 font-weight-normal">Entra y disfruta del ajedrez</h1>
            <br></br>
            <br></br>
              <div className="d-flex justify-content-center bd-highlight">
              <a className="btn boton btn-info btn-lg p-2 bd-highlight" style={{'marginRight': '10px'}} href="/login">Login</a>
              <br></br>
              <br></br>
              <a className="btn boton btn-info btn-lg p-2 bd-highlight" href="/register">Registro</a>
              </div>
              </div>


            )
}


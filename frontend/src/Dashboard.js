import React from 'react';

import axios from 'axios';

class Dashboard extends React.Component{

    state = {};

   
  
  
   componentDidMount = () => {
    const token = localStorage.getItem("jwtToken")
    let url = "http://localhost:8080/players";
    axios.get(url,
        {
            headers: {
                "Authorization": `Bearer  ${token}`
            }

        })
    .then( res => console.log(res))
    ;
  };

    render(){
    return(
        <div>
            <h1>{this.state.message}</h1>
        </div>
        )

    }
}


export default Dashboard;

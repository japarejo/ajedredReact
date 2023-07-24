import axios from 'axios'

function get(route) {
  return new Promise(function (resolve, reject) {
    axios
      .get(route)
      .then(function (response) {
        resolve(response.data)
      })
      .catch(error => reject(error))
  })
}

function post(route, data, options) {
  return new Promise(function (resolve, reject) {
    axios
      .post(route, data, options)
      .then(response => {
        resolve(response.data)
      })
      .catch(error => {
        try {
          console.log(error)
        } catch (error) {
          reject(error)
        }
      })
  })
}

export { get, post }

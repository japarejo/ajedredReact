class TokenService {
  getLocalAccessToken() {
    const jwt = JSON.parse(localStorage.getItem('jwt'))
    return jwt
  }

  updateLocalAccessToken(token) {
    const jwt = JSON.stringify(token)
    window.localStorage.setItem('jwt', jwt)
  }

  removeUser() {
    window.localStorage.removeItem('jwt')
  }
}
const tokenService = new TokenService()

export default tokenService

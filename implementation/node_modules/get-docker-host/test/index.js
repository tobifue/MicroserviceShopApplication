const getDockerHost = require('../')

getDockerHost((error, result) => {
  if (result) {
    console.log(result)
    console.log(result === '172.17.0.1')
  } else if (error) {
    console.log(error)
  }
})

const childProcess = require('child_process')

const handleIpRouteResults = callback => (error, stdout, stderr) => {

  if (stdout) {
    const output = stdout
    const match = output.match(/default via ((?:[0-9]{1,3}\.){3}[0-9]{1,3})/)
    let ip = undefined
    if (Array.isArray(match) && match.length >= 2) {
      ip = match[1]
    }

    if (ip && typeof ip === 'string') {
      callback(undefined, ip)
    } else {
      callback(new Error('Unable to find ip with /sbin/ip route'), undefined)
    }
  } else if (error) {
    callback(error, undefined)
  } else if (stderr) {
    callback(new Error(stderr), undefined)
  } else {
    callback(new Error('No results or feedback given'), undefined)
  }
}

module.exports = function (callback) {
  childProcess.exec('/sbin/ip route', handleIpRouteResults(callback))
}

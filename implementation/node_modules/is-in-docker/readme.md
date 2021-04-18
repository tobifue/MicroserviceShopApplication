# is-in-docker [![Build Status](https://travis-ci.org/techgaun/is-in-docker.svg?branch=master)](https://travis-ci.org/techgaun/is-in-docker)

> Check if the process is running inside or outside of docker container


## Install

```
$ npm install --save is-in-docker
```


## Usage

```js
var isInDocker = require('is-in-docker');

if (isInDocker()) {
    console.log('I am running inside docker container');
}
```

## License

MIT © [techgaun](http://samar.techgaun.com)

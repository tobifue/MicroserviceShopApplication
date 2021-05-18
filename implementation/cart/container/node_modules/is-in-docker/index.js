'use strict';

var readFileSync = require('fs').readFileSync;

module.exports = function () {
    try {
        var re = /:\/.+/g;
        var str = readFileSync('/proc/1/cgroup', 'utf8');
        if (str.match(re) === null) {
            throw new Error('Could not match the pattern');
        }
        return true;
    } catch (e) {
        return false;
    }
};

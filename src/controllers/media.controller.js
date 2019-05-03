const { spawn } = require('child_process');
const ps = require('ps-node');

module.exports = class MediaController {
    static refresh(req, res) {
        let shouldRefresh = false;

        ps.lookup({
            command: 'node',
            arguments: 'scanner.js',
        },
        (err, resultList) => {
            if (!err) {
                if (resultList.length < 1){
                    shouldRefresh = true;
                    spawn('/opt/app/background/scanner.js');
                }
            }

            res.json({ refresh: shouldRefresh });
        });
    }
};

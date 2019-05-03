module.exports = class HomeController {
    static index(req, res) {
        res.json({ works: true });
    }
};

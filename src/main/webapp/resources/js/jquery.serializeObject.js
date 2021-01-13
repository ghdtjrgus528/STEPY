/**
 * Usage: var json = $('#form-login').serializeObject();
 * Output: {username: "admin", password: "123456"}
 * Output: {username: "admin", password: "123456", subscription: ["news","offer"]}
 * */

$.fn.serializeObject = function() {
    var obj = {};
    var arr = this.serializeArray();
    arr.forEach(function(item, index) {
        if (obj[item.name] === undefined) { // New
            obj[item.name] = item.value || '';
        } else {                            // Existing
            if (!obj[item.name].push) {
                obj[item.name] = [obj[item.name]];
            }
            obj[item.name].push(item.value || '');
        }
    });
    return obj;
};
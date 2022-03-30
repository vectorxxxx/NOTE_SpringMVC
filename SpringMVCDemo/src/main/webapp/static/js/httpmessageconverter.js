var vue = new Vue({
    el: "#app",
    methods: {
        testAxios: function (event) {
            testAxios(event.target.href);
        }
    }
});

function testAxios(url) {
    axios({
        method: "post",
        url: url,
        params: {
            username: "admin",
            password: "123456"
        }
    }).then(function (response) {
        alert(response.data);
    });
    event.preventDefault();
}
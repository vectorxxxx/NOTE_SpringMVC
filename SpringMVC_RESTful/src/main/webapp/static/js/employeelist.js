var vue = new Vue({
    el: "#employeeTable",
    methods: {
        deleteEmployee: function (event) {
            var deleteForm = document.getElementById("deleteForm");
            deleteForm.action = event.target.href;
            deleteForm.submit();
            event.preventDefault();
        }
    }
});
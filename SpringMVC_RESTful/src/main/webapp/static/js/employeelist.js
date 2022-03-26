var vue = new Vue({
    el: "#employeeTable",
    methods: {
        deleteEmployee: function (event) {
            if (confirm('确认删除吗？')) {
                var deleteForm = document.getElementById("deleteForm");
                deleteForm.action = event.target.href;
                deleteForm.submit();
            }
            event.preventDefault();
        }
    }
});
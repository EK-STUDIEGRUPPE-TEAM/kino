const employeeForm = document.getElementById("employee-form");
const employeeName = document.getElementById("employee-name");
const employeeType = document.getElementById("employee-type");

const submitEmployeeButton =
    document.getElementById("submit-employee-button");

const cancelEditButton =
    document.getElementById("cancel-edit-button");

const employeeFormTitle =
    document.getElementById("employee-form-title");

const employeeTableBody =
    document.getElementById("employee-table-body");


let employeeIdToEdit = null;


/* ---------- GET EMPLOYEES ---------- */

function getEmployees() {

    fetch("/api/employee")
        .then(response => {

            if (!response.ok) {
                throw new Error("Could not get employees");
            }

            return response.json();
        })
        .then(employees => {

            employeeTableBody.innerHTML = "";

            if (employees.length === 0) {

                const row = employeeTableBody.insertRow();

                const cell = row.insertCell();

                cell.colSpan = 4;
                cell.textContent = "No employees found";
                cell.classList.add("empty-table");

                return;
            }


            employees.forEach(employee => {

                const row = employeeTableBody.insertRow();


                /* Keep selected row highlighted */

                if (employee.id === employeeIdToEdit) {
                    row.classList.add("editing-row");
                }


                /* ID */

                const idCell = row.insertCell();
                idCell.textContent = employee.id;


                /* Name */

                const nameCell = row.insertCell();
                nameCell.textContent = employee.name;


                /* Role */

                const typeCell = row.insertCell();

                const roleBadge = document.createElement("span");

                roleBadge.classList.add("role-badge");

                roleBadge.textContent =
                    employee.type.replaceAll("_", " ");

                typeCell.appendChild(roleBadge);


                /* Handling */

                const handlingCell = row.insertCell();
                handlingCell.classList.add("action-cell");


                /* Edit button */

                const editButton = document.createElement("button");

                editButton.type = "button";
                editButton.textContent = "Edit";
                editButton.classList.add("edit-button");


                editButton.addEventListener("click", function () {

                    employeeIdToEdit = employee.id;

                    employeeName.value = employee.name;
                    employeeType.value = employee.type;

                    employeeFormTitle.textContent = "Edit Employee";

                    submitEmployeeButton.textContent = "Save Changes";

                    cancelEditButton.hidden = false;


                    document
                        .querySelectorAll("#employee-table tbody tr")
                        .forEach(tableRow => {

                            tableRow.classList.remove("editing-row");

                        });


                    row.classList.add("editing-row");

                });


                /* Delete button */

                const deleteButton = document.createElement("button");

                deleteButton.type = "button";
                deleteButton.textContent = "Delete";
                deleteButton.classList.add("delete-button");


                deleteButton.addEventListener("click", function () {

                    deleteEmployee(employee.id);

                });


                handlingCell.appendChild(editButton);
                handlingCell.appendChild(deleteButton);

            });

        })
        .catch(error => {

            console.error("Error getting employees:", error);

        });
}


/* ---------- CREATE EMPLOYEE ---------- */

function addEmployee(event) {

    event.preventDefault();


    const employee = {

        name: employeeName.value,
        type: employeeType.value

    };


    /* If an employee is being edited */

    if (employeeIdToEdit !== null) {

        updateEmployee(employeeIdToEdit, employee);

        return;
    }


    /* Otherwise create a new employee */

    fetch("/api/employee/addEmployee", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(employee)

    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Could not create employee");
            }

            return response.json();

        })
        .then(() => {

            resetEmployeeForm();

            getEmployees();

        })
        .catch(error => {

            console.error("Error creating employee:", error);

        });
}


/* ---------- UPDATE EMPLOYEE ---------- */

function updateEmployee(id, employee) {

    fetch("/api/employee/editEmployee/" + id, {

        method: "PUT",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(employee)

    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Could not update employee");
            }

            return response.json();

        })
        .then(() => {

            resetEmployeeForm();

            getEmployees();

        })
        .catch(error => {

            console.error("Error updating employee:", error);

        });
}


/* ---------- DELETE EMPLOYEE ---------- */

function deleteEmployee(id) {

    fetch("/api/employee/deleteEmployee/" + id, {

        method: "DELETE"

    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Could not delete employee");
            }


            if (employeeIdToEdit === id) {

                resetEmployeeForm();

            }


            getEmployees();

        })
        .catch(error => {

            console.error("Error deleting employee:", error);

        });
}


/* ---------- RESET FORM ---------- */

function resetEmployeeForm() {

    employeeIdToEdit = null;

    employeeForm.reset();

    employeeFormTitle.textContent = "Add Employee";

    submitEmployeeButton.textContent = "Add New Employee";

    cancelEditButton.hidden = true;


    document
        .querySelectorAll("#employee-table tbody tr")
        .forEach(row => {

            row.classList.remove("editing-row");

        });
}


/* ---------- EVENTS ---------- */

cancelEditButton.addEventListener("click", function () {

    resetEmployeeForm();

});


employeeForm.addEventListener("submit", addEmployee);


/* ---------- LOAD EMPLOYEES ---------- */

getEmployees();
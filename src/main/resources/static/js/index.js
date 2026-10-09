const nextShowing =
    document.getElementById("next-showing");

const theatreList =
    document.getElementById("theatre-list");

const employeeList =
    document.getElementById("employee-list");



// Henter de kommende forestillinger
function getShowings() {

    fetch("/api/showings/upcoming")

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Kunne ikke hente forestillinger"
                );

            }

            return response.json();

        })

        .then(showings => {

            showNextShowing(showings);

        })

        .catch(error => {

            console.error(
                "Fejl ved hentning af forestillinger:",
                error
            );


            nextShowing.innerHTML = `
                <p class="next-message">
                    Forestillingen kunne ikke hentes.
                </p>
            `;

        });

}



// Viser den næste forestilling
function showNextShowing(showings) {

    if (showings.length === 0) {

        nextShowing.innerHTML = `
            <p class="next-message">
                Ingen kommende forestillinger.
            </p>
        `;

        return;

    }


    // Liste sorteret efter dato så den tætteste er den næste
    const showing = showings[0];


    nextShowing.innerHTML = `

        <h2 class="next-title">
            ${showing.movie.title}
        </h2>


        <div class="next-information">

            <div>
                <span class="next-label">Dato</span>
                <span class="next-value">${formatDate(showing.dateTime)}</span>
            </div>

            <div>
                <span class="next-label">Tid</span>
                <span class="next-value">${formatTime(showing.dateTime)}</span>
            </div>

            <div>
                <span class="next-label">Sal</span>
                <span class="next-value">${showing.theatre.name}</span>
            </div>

        </div>

    `;

}



// Henter salene
function getTheatres() {

    fetch("/api/theatres")

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Kunne ikke hente sale"
                );

            }

            return response.json();

        })

        .then(theatres => {

            showTheatres(theatres);

        })

        .catch(error => {

            console.error(
                "Fejl ved hentning af sale:",
                error
            );


            theatreList.innerHTML = `
                <p class="side-message">
                    Salene kunne ikke hentes.
                </p>
            `;

        });

}



// Indsætter sale
function showTheatres(theatres) {

    theatreList.innerHTML = "";


    theatres.forEach(theatre => {

        const theatreBox =
            document.createElement("div");


        theatreBox.classList.add(
            "theatre-box"
        );


        theatreBox.innerHTML = `

            <span class="theatre-name">
                ${theatre.name}
            </span>


            <span class="theatre-capacity">
                ${getCapacity(theatre)}
            </span>


            <span class="theatre-size">
                ${theatre.numberOfRows} × ${theatre.seatsPerRow} sæder
            </span>

        `;


        theatreList.appendChild(theatreBox);

    });

}



// Henter medarbejdere
function getEmployees() {

    fetch("/api/employee")

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Kunne ikke hente medarbejdere"
                );

            }

            return response.json();

        })

        .then(employees => {

            showEmployees(employees);

        })

        .catch(error => {

            console.error(
                "Fejl ved hentning af medarbejdere:",
                error
            );


            employeeList.innerHTML = `
                <li class="side-message">
                    Personalet kunne ikke hentes.
                </li>
            `;

        });

}



// Indsætter medarbejdere i listen
function showEmployees(employees) {

    employeeList.innerHTML = "";


    employees.forEach(employee => {

        const employeeItem =
            document.createElement("li");


        employeeItem.innerHTML = `

            <span class="staff-name">
                ${employee.name}
            </span>

            <span class="staff-role">
                ${formatEmployeeType(employee.type)}
            </span>

        `;


        employeeList.appendChild(employeeItem);

    });

}



// Udregner antal sæder i en sal
function getCapacity(theatre) {

    return (
        theatre.numberOfRows *
        theatre.seatsPerRow
    );

}



// Formaterer dato
function formatDate(dateTime) {

    const date = new Date(dateTime);

    return date.toLocaleDateString("da-DK");

}



// Formaterer tidspunkt
function formatTime(dateTime) {

    const date = new Date(dateTime);

    return date.toLocaleTimeString("da-DK", {
        hour: "2-digit",
        minute: "2-digit"
    });

}



// Konverterer EmployeeType ENUM's til pæne navne
function formatEmployeeType(type) {

    if (type === "SALES") {
        return "Salg";
    }

    if (type === "MOVIE_OPERATOR") {
        return "Film operatør";
    }

    if (type === "TICKET_INSPECTOR") {
        return "Billet kontrollør";
    }

    return type;

}



// Kører når siden åbnes
getShowings();

getTheatres();

getEmployees();

const table = document.querySelector('#content');

const btn = document.querySelector('#btn');


const url = 'http://localhost:8080/users/darshan/todos'

btn.addEventListener('click', populate);



async function populate() {
    await fetch(url)
        .then(response => response.json())
        .then(data => {
            let item = document.createElement('tr');
            let id = document.createElement('td');
            let desc = document.createElement('td');

            id.textContent = data.id;
            desc.textContent = data.description;
            console.log(data.id)

            item.append(id, desc);

            table.appendChild(item);
        });
}
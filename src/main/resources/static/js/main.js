const usersList = document.querySelector('#users-list');
let output = '';
const tableUsers = $('#users-list');
const tableUser = $('#user-info');

const urlUserList = 'http://localhost:8080/api/userlist';
const urlUserById = 'http://localhost:8080/api/user/{id}';
const urlUpdateUser  = "http://localhost:8080/api/updateUser";

$(async function() {
    await getUsersTable();
    await getUserInfoTable();
});

async function getUsersTable(){
    tableUsers.empty()

    fetch(urlUserList)
        .then(res => res.json())
        .then(data => {
            data.forEach(user => {
                let tableWithUsers = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.lastname}</td>
                            <td>${user.age}</td>                                                 
                            <td>${user.email}</td>
                            <td>${user.authorities.map(authority => " " + authority['authority'].substring(5))}</td>
                            <td>
                                <button type="button" class="btn btn-info" data-toggle="modal" id="buttonEdit" data-action="edit" data-id="${user.id}" data-target="#edit">Edit</button>
                            </td>
                            <td>
                                <button type="button" class="btn btn-danger" data-toggle="modal" id="buttonDelete" data-action="delete" data-id="${user.id}" data-target="#delete"">Delete</button>
                            </td>
                        </tr>)`;
                tableUsers.append(tableWithUsers);
            })
        })
}

async function getUserInfoTable(){
    tableUser.empty();
    fetch("http://localhost:8080/api/user")
        .then(res => res.json())
        .then(data => {

            $('#headerUsername').append(data.email);
            let roles = data.roles.map(role => " " + role.roleName.substring(5));
            $('#headerRoles').append(roles);

            let tableWithUserInfo = `$(
           
                <td>${data.id}</td>
                <td>${data.username}</td>
                <td>${data.lastname}</td>
                <td>${data.age}</td>
                <td>${data.email}</td>
                <td>${roles}</td>
            )`;
            tableUser.append(tableWithUserInfo);
        })
}

async function getUser(id) {
    let url = "http://localhost:8080/api/user/" + id;
    let response = await fetch(url);
    return await response.json();
}

// Add New User

$(async function () {
    await newUser();
});

async function newUser() {
    await fetch("http://localhost:8080/api/roles")
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let el = document.createElement("option");
                el.text = role.roleName.substring(5);
                el.value = role.id;
                $('#newRoles')[0].appendChild(el);
            })
        })

    const form = document.forms["formNewUser"];

    form.addEventListener('submit', addNewUser)

    async function addNewUser(e) {
        e.preventDefault();
        let newUserRoles = [];
        for (let i = 0; i < form.roles.options.length; i++) {
            if (form.roles.options[i].selected) newUserRoles.push({
                id: form.roles.options[i].value,
                name: form.roles.options[i].name
            })
        }

        const response = await fetch("http://localhost:8080/api/adduser", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: form.username.value,
                lastname: form.lastname.value,
                age: form.age.value,
                email: form.email.value,
                password: form.password.value,
                roles: newUserRoles
            })
        })
        if(response.ok){
            form.reset();
            getUsersTable();
            $('#usertable').click();
        } else {
            let error = await response.json();
            // swal(error.info)
        }
    }
}

// UpdateUser

$(async function() {
    updateUser();
});

function updateUser() {
    const editForm = document.forms["formEditUser"];
    editForm.addEventListener("submit", async ev => {
        ev.preventDefault();
        let editUserRoles = [];
        for (let i = 0; i < editForm.roles.options.length; i++) {
            if (editForm.roles.options[i].selected) editUserRoles.push({
                id: editForm.roles.options[i].value,
                name: "ROLE_" + editForm.roles.options[i].text
            })
        }

        const response = await fetch("http://localhost:8080/api/updateUser/" + editForm.id.value, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: editForm.id.value,
                username: editForm.username.value,
                lastname: editForm.lastname.value,
                age: editForm.age.value,
                email: editForm.email.value,
                password: editForm.password.value,
                roles: editUserRoles
            })
        })
        if(response.ok){
            $('#editFormCloseButton').click();
            getUsersTable();
        } else {
            let error = await response.json();
            // swal(error.info)
        }
    })
}


$('#edit').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('id');
    showEditModal(id);
})

async function showEditModal(id) {
    $('#rolesEditUser').empty();
    let user = await getUser(id);
    let form = document.forms["formEditUser"];
    form.id.value = user.id;
    form.username.value = user.username;
    form.lastname.value = user.lastname;
    form.age.value = user.age;
    form.email.value = user.email;
    form.password.value = '';

    await fetch("http://localhost:8080/api/roles")
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;
                for (let i = 0; i < user.authorities.length; i++) {
                    if (user.authorities[i]['authority'] === role.roleName) {
                        selectedRole = true;
                        break;
                    }
                }
                    let el = document.createElement("option");
                    el.text = role.roleName;
                    el.value = role.id;
                    if (selectedRole) el.selected = true;
                    $('#rolesEditUser')[0].appendChild(el);
            })
        })
}

// // Delete User

$(async function() {
    deleteUser();
});

async function deleteUser(){
    const deleteForm = document.forms["formDeleteUser"];
    deleteForm.addEventListener("submit", ev)

    async function ev(e) {
        e.preventDefault();
        const response = await fetch("http://localhost:8080/api/deleteUser/" + deleteForm.id.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        if(response.ok){
            $('#deleteFormCloseButton').click();
            getUsersTable();
            $('#usertable').click();
        } else {
            console.log("FUCK!!!!")
            let error = await response.json();
            // swal(error.info)
        }
    }
}

$('#delete').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('id');
    showDeleteModal(id);
})

async function showDeleteModal(id) {
    let user = await getUser(id);
    let form = document.forms["formDeleteUser"];
    form.id.value = user.id;
    form.username.value = user.username;
    form.lastname.value = user.lastname;
    form.age.value = user.age;
    form.email.value = user.email;

    $('#rolesDeleteUser').empty();

    await fetch("http://localhost:8080/api/roles")
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                // let selectedRole = false;
                let el = document.createElement("option");
                for (let i = 0; i < user.authorities.length; i++) {
                    if (user.authorities[i]['authority'] === role.roleName) {
                        el.text = user.authorities[i]['authority'].substring(5);
                        $('#rolesDeleteUser')[0].appendChild(el);
                        break;
                    }
                }
            })
        });
}












const URL = "http://localhost:8080/admin/api/";

const TABLE_USERS = document.querySelector(".table-body")
const TABLE_USER_INFO = document.getElementById("userInfoTable")

const EDIT_FORM = document.getElementById("editForm");
const EDIT_MODAL = new bootstrap.Modal(document.getElementById("editModal"));

const DELETE_FORM = document.getElementById("deleteForm");
const DELETE_MODAL = new bootstrap.Modal(document.getElementById("deleteModal"));

const NEW_FORM = document.getElementById("newUserForm");

const ID_EDIT = document.getElementById("editId");
const USERNAME_EDIT = document.getElementById("editUsername");
const FIRSTNAME_EDIT = document.getElementById("editFirstname");
const LASTNAME_EDIT = document.getElementById("editLastname");
const EMAIL_EDIT = document.getElementById("editEmail");
const ROLES_EDIT = document.getElementById("editRole");

const ID_DELETE = document.getElementById("deleteId");
const USERNAME_DELETE = document.getElementById("deleteUsername");
const FIRSTNAME_DELETE = document.getElementById("deleteFirstname");
const LASTNAME_DELETE = document.getElementById("deleteLastname");
const EMAIL_DELETE = document.getElementById("deleteEmail");
const ROLES_DELETE = document.getElementById("deleteRole");

const USERNAME_NEW = document.getElementById("newUsername");
const PASSWORD_NEW = document.getElementById("newPassword");
const FIRSTNAME_NEW = document.getElementById("newFirstname");
const LASTNAME_NEW = document.getElementById("newLastname");
const EMAIL_NEW = document.getElementById("newEmail");
const ROLES_NEW = document.getElementById("newRole");


displayUserTable();

document.addEventListener("click", ev => modalClick(ev));

EDIT_FORM.addEventListener("submit", ev => submitEdit(ev));
DELETE_FORM.addEventListener("submit", ev => submitDelete(ev));
NEW_FORM.addEventListener("submit", ev => submitAdd(ev));


function displayUserTable() {
    fetch(URL + "getUsers")
        .then(response => response.json())
        .then(userList => userListToHtml(userList));
}

function userListToHtml(userList) {
    let display = ``;
    let displayUserInfo = ``;
    let adminIdField = document.getElementById("userInfoId");
    let adminId = adminIdField.innerHTML;
    for (let user of userList) {
        if (user.number == adminId) {
            displayUserInfo = `
                <tr>
                    <td id="userInfoId">${user.number}</td>
                    <td>${user.nameOnServer}</td>
                    <td>${user.firstname}</td>
                    <td>${user.lastname}</td>
                    <td>${user.email}</td>
                    <td>${showRoles(user.roles)}</td>
                </tr>`;
        }

        display += `
            <tr>
                <td>${user.number}</td>
                <td>${user.nameOnServer}</td>
                <td>${user.firstname}</td>
                <td>${user.lastname}</td>
                <td>${user.email}</td>
                <td>${showRoles(user.roles)}</td>
                <td><a class="btnEdit btn btn-info btn-sm">Edit</a></td>
                <td><a class="btnDelete btn btn-danger btn-sm">Delete</a></td>
            </tr>`;
    }

    TABLE_USERS.innerHTML = display;
    TABLE_USER_INFO.innerHTML = displayUserInfo;
}

let roles;
fetch(URL + "getRoles")
    .then(response => response.json())
    .then(roleList => {
            roles = roleList;
        }
    );
function showRoles(roles) {
    let rolesAsString = ``;
    let isOneRole = false;
    for (let role of roles) {
        if (isOneRole) {
            rolesAsString += `, `
        }
        rolesAsString += role.roleWithoutPrefix;
        isOneRole = true;
    }
    return rolesAsString;
}

function modalClick(ev) {
    if (ev.target.closest(".btnEdit")) {
        modalEdit(ev)
    }

    if (ev.target.closest(".btnDelete")) {
        modalDelete(ev)
    }
}

function modalEdit(ev) {
    insertValuesEdit(ev);
    EDIT_MODAL.show();

    function insertValuesEdit(ev) {
        let target = ev.target;
        let userRow = target.parentNode.parentNode;
        let userCol = userRow.children;
        ID_EDIT.value = userCol[0].innerHTML;
        USERNAME_EDIT.value = userCol[1].innerHTML;
        FIRSTNAME_EDIT.value = userCol[2].innerHTML;
        LASTNAME_EDIT.value = userCol[3].innerHTML;
        EMAIL_EDIT.value = userCol[4].innerHTML;
        insertRole(userCol[5].innerHTML);

        function insertRole(rolesUser_a) {
            let rolesOfUser = rolesUser_a.split(", ");
            let rolesOption = ``;
            for (let role of roles) {
                rolesOption += `<option value = ${role.id}`;
                if (containsRole(role, rolesOfUser)) {
                    rolesOption += ` selected`
                }
                rolesOption += `>${role.roleWithoutPrefix}</option>`;
            }
            ROLES_EDIT.innerHTML = rolesOption;

            function containsRole(role, rolesOfUser) {
                let isContains = false;
                for (let roleOfUser of rolesOfUser) {
                    isContains = isContains || (roleOfUser === role.roleWithoutPrefix);
                }
                return isContains;
            }
        }
    }
}

function modalDelete(ev) {
    insertValueDelete(ev);
    DELETE_MODAL.show();

    function insertValueDelete(ev) {
        let target = ev.target;
        let userRow = target.parentNode.parentNode;
        let userCol = userRow.children;
        ID_DELETE.value = userCol[0].innerHTML;
        USERNAME_DELETE.value = userCol[1].innerHTML;
        FIRSTNAME_DELETE.value = userCol[2].innerHTML;
        LASTNAME_DELETE.value = userCol[3].innerHTML;
        EMAIL_DELETE.value = userCol[4].innerHTML;
        insertRole(userCol[5].innerHTML);

        function insertRole(rolesUser_a) {
            let rolesOfUser = rolesUser_a.split(", ");
            let rolesOption = ``;
            for (let role of roles) {
                rolesOption += `<option value = ${role.id}`;
                if (containsRole(role, rolesOfUser)) {
                    rolesOption += ` selected`
                }
                rolesOption += `>${role.roleWithoutPrefix}</option>`;
            }
            ROLES_DELETE.innerHTML = rolesOption;

            function containsRole(role, rolesOfUser) {
                let isContains = false;
                for (let roleOfUser of rolesOfUser) {
                    isContains = isContains || (roleOfUser === role.roleWithoutPrefix);
                }
                return isContains;
            }
        }
    }
}

function submitEdit(ev) {
    ev.preventDefault();
    let user = userEditToJson();
    let adress = URL + "editUser/" + user.id;
    fetch(adress, {
        method: 'PATCH',
        mode: 'cors',
        headers: {
            'Content-Type': 'application/json'
        },
        body: user
    })
        .then(r => r.text())
        .then(r => fetch(URL + "getUsers")
            .then(response => response.json())
            .then(userList => userListToHtml(userList)))
        .then(r => EDIT_MODAL.hide());

    function userEditToJson() {
        let selectedRole = selectedRoleAfterEdit();
        return JSON.stringify({
            id: ID_EDIT.value,
            username: USERNAME_EDIT.value,
            firstname: FIRSTNAME_EDIT.value,
            lastname: LASTNAME_EDIT.value,
            email: EMAIL_EDIT.value,
            roles: selectedRole
        });

        function selectedRoleAfterEdit() {
            let roles = [];
            let options = ROLES_EDIT.options;
            for (let option of options) {
                if (option.selected) {
                    roles.push(option.value)
                }
            }
            return roles;
        }
    }
}

function submitDelete(ev) {
    ev.preventDefault();
    let adress = URL + "deleteUser/" + ID_DELETE.value;
    fetch(adress, {
        method: 'DELETE'
    })
        .then(r => r.text())
        .then(r => fetch(URL + "getUsers")
            .then(response => response.json())
            .then(userList => userListToHtml(userList)))
        .then(r => DELETE_MODAL.hide());
}

function submitAdd(ev) {
    ev.preventDefault();
    let user = newUserToJson();
    let adress = URL + "addNewUser";
    fetch(adress, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: user
    })
        .then(r => r.text())
        .then(r => fetch(URL + "getUsers")
            .then(response => response.json())
            .then(userList => userListToHtml(userList)))
        .then(r => redirect());

    function newUserToJson() {
        let selectedRole = selectedRoleAfterChose();
        return JSON.stringify({
            username: USERNAME_NEW.value,
            password: PASSWORD_NEW.value,
            firstname: FIRSTNAME_NEW.value,
            lastname: LASTNAME_NEW.value,
            email: EMAIL_NEW.value,
            roles: selectedRole
        });

        function selectedRoleAfterChose() {
            let roles = [];
            let options = ROLES_NEW.options;
            for (let option of options) {
                if (option.selected) {
                    roles.push(option.value)
                }
            }
            return roles;
        }
    }

    function redirect() {
        let navtabUserTable = document.getElementById("nav-userTable-tab");
        let navtabNewUser = document.getElementById("nav-newUser-tab");
        let navUserTable = document.getElementById("nav-userTable");
        let navNewUser = document.getElementById("nav-newUser");

        navtabUserTable.setAttribute("class", "nav-link active");
        navtabNewUser.setAttribute("class", "nav-link");
        navUserTable.setAttribute("class", "tab-pane fade active show");
        navNewUser.setAttribute("class", "tab-pane fade");
    }
}
window.onload=function(){
    document.getElementById("UpdateButton").addEventListener("click", UpdateUser);
};
function UpdateUser(event) {
    event.preventDefault();

    //parsing the logged in userID into the new javascript function//
    let id = JSON.parse(window.sessionStorage.user);
    id = JSON.stringify(id.userID);
    alert(id);

    if (document.getElementById("FirstName").value.trim() === '') {
        alert("Please provide a first name.");
        return;
    }

    if (document.getElementById("LastName").value.trim() === '') {
        alert("Please provide a last name");
        return;
    }

    if (document.getElementById("Bio").value.trim() === '') {
        alert("Please enter a bio");
        return;
    }

    if (document.getElementById("Email").value.trim() === '') {
        alert("Please enter an email");
        return;
    }

    const form = document.getElementById("UpdateInfo");
    const formData = new FormData(form);
    formData.append("UserID", id);

    let   apiPath = '/user/update';
    fetch(apiPath, {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            window.location.href = 'Profile.html';
        }
    });

}
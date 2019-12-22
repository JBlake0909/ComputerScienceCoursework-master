
function pageLoad(){

    //parsing the logged in userID into the new javascript function//
    let id = JSON.parse(window.sessionStorage.user);
    id = JSON.stringify(id.userID);
    alert(id);
    // Java script to extract data from the database and display it on the users profile page//
    let profileHTML = '<div>';
    fetch('/user/display/' +id , {method: 'get'}
    ).then(response => response.json()
    ).then(users => {
        for (let user of users) {
            profileHTML +=
                    '<b>Name:</b>' +
                    ' '+
                    `${user.firstName}` +
                    ' '+
                    `${user.lastName}` +
                '<p>' +
                    '<b>DateJoined:</b>' +
                    ' '+
                    `${user.DateJoined}` +
                '</p>'+
                '<p>' +
                    '<b>Followers:</b>' +
                    ' '+
                    `${user.Followers}` +
                '</p>'+
                '<p>' +
                    '<b>Following:</b>' +
                    ' '+
                    `${user.Following}` +
                '</p>'+
                    '<b>Bio:</b>' +
                '<br>'+
                `${user.Bio}`
        }
        profileHTML += '</div>';
        document.getElementById("listDiv").innerHTML = profileHTML;
    });
    //-------------------------------------------------------------------------------//


    }


function pageLoad(){
    //const id = JSON.stringify(JSON.parse(window.sessionStorage.user));
    //alert(id);
    let id = 1;
    let profileHTML = '<table>'
        + '<tr>'
    + '<th>Name</th>' + '<th>DateJoined</th>' + '<th>Followers</th>' + '<th>Following</th>'+ '<th>Bio</th>'+
        '</tr>';
    fetch('/user/display/' +id , {method: 'get'}
    ).then(response => response.json()
    ).then(users => {
        for (let user of users) {
            profileHTML += `<tr>` +
                `<td>${user.firstName}</td>` +
                `<td>${user.DateJoined}</td>` +
                `<td>${user.Followers}</td>` +
                `<td>${user.Following}</td>` +
                `<td>${user.Bio}</td>`
        }
        profileHTML += '</table>';

        document.getElementById("listDiv").innerHTML = profileHTML;
    });


    }

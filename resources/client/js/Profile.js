
function pageLoad(){
    let profileHTML = '<table>'
        + '<tr>'
    + '<th>Name</th>' + '<th>DateJoined</th>' + '<th>Followers</th>' + '<th>Following</th>'+ '<th>Bio</th>'+
        '</tr>';
    fetch('/user/display{id}', {method: 'get'}
    ).then(response => response.json()
    ).then(datas => {
        for (let data of datas) {
            profileHTML += `<tr>` +
                `<td>${data.name}</td>` +
                `<td>${data.DateJoined}</td>` +
                `<td>${data.Followers}</td>` +
                `<td>${data.Following}</td>` +
                `<td>${data.Bio}</td>` +
        }
        profileHTML += '</table>';

        document.getElementById("listDiv").innerHTML = profileHTML;
    });


    }


function pageLoad(event){
    const id = event.target.getAttribute("data-id");
    let profileHTML = '<table>'
        + '<tr>'
    + '<th>Name</th>' + '<th>DateJoined</th>' + '<th>Followers</th>' + '<th>Following</th>'+ '<th>Bio</th>'+
        '</tr>';
    fetch('/user/display' +id , {method: 'get'}
    ).then(response => response.json()
    ).then(datas => {
        for (let data of datas) {
            profileHTML += `<tr>` +
                `<td>${data.name}</td>` +
                `<td>${data.name}</td>` +
                `<td>${data.id}</td>` +
                `<td>${data.id}</td>` +
                `<td>${data.name}</td>`
        }
        profileHTML += '</table>';

        document.getElementById("listDiv").innerHTML = profileHTML;
    });


    }

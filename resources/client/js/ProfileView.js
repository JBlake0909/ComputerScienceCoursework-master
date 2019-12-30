
function pageLoad(){
    //parsing the logged in userID into the new javascript function//
    let id = JSON.parse(window.sessionStorage.view);
    // Java script to extract data from the database and display it on the users profile page//
    let profileHTML = '<div>';
    fetch('/user/display/' +id , {method: 'get'}
    ).then(response => response.json()
    ).then(users => {
        for (let user of users) {
            profileHTML +=
                '<br>'+
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
                '<p>' +
                `${user.Bio}`+
                '</p>'+
                '<b>Email:</b>' +
                `${user.Email}`
        }
        profileHTML += '</div>';
        document.getElementById("UserInfo").innerHTML = profileHTML;
    });

    let LoggedId = JSON.parse(window.sessionStorage.user);
    LoggedId = JSON.stringify(LoggedId.userID);
    let following = false
    fetch('/user/getFollowing/' +LoggedId , {method: 'get'}
    ).then(response => response.json()
    ).then(datas => {
        for (let data of datas) {
            let results = data.UserID;
            if(results == id){
                following = true
            }
        }
    });

    if(following == true){
        profileHTML +=
            '<button id=UnFollow>UnFollow</button>';
    }
    else{
        profileHTML +=
            '<button id=Follow>Follow</button>';
    }



    //-------------------------------------------------------------------------------//
    // extract Social Links from the database and provide an interface for creation and deletion//
    let SocialHTML = '<table>';

    fetch('/SocialLink/get/' +id , {method: 'get'}
    ).then(response => response.json()
    ).then(links => {
        for(let link of links){
            SocialHTML+=
                `<tr>` +
                `<td><a href="${link.Link}">link</a></td>`+
                `</tr>`;
        }
        SocialHTML += '</table>';
        document.getElementById("SocialLinks").innerHTML = SocialHTML;

    });

    let GenreHTML = '<table>';
    fetch('/Genre/getByID/' +id , {method: 'get'}
    ).then(response => response.json()
    ).then(genres => {
        for(let genre of genres){
            GenreHTML+=
                `<tr>` +
                `<td>${genre.Genre}</td>`+
                `</tr>`;
        }
        GenreHTML += '</table>';
        document.getElementById("Genres").innerHTML = GenreHTML;
    });
}
//---------------------------------------------------------------------------------------------------------------//






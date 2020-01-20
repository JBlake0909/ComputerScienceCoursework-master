let imageList = [];
let pageSize = 5;    // Change this as desired.

function pageLoad(){
    //parsing the logged in userID into the new javascript function//
    let id = JSON.parse(window.sessionStorage.user);
    id = JSON.stringify(id.userID);
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
                '<p>' +
                `${user.Bio}`+
                '</p>'+
                    '<b>Email:</b>' +
                    `${user.Email}`

        }
        profileHTML += '</div>';
        document.getElementById("UserInfo").innerHTML = profileHTML;

    });
    //-------------------------------------------------------------------------------//
    // extract Social Links from the database and provide an interface for creation and deletion//
    let SocialHTML = '<table>' +
        '<tr>' +
        '<th>Links <br> <button id=\'createLink\'>Create Link</button></th>' +
        '</tr>'
    ;

    fetch('/SocialLink/get/' +id , {method: 'get'}
    ).then(response => response.json()
    ).then(links => {
        for(let link of links){
            SocialHTML+=
                `<tr>` +
                    `<td><a href="${link.Link}">link</a></td>`+
                    `<td class="last">` +
                    `<button class='deleteButton' data-id='${link.LinkID}'>Delete</button>`+
                    `</td>` +
                `</tr>`;
        }
        SocialHTML += '</table>';
        document.getElementById("SocialLinks").innerHTML = SocialHTML;

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.addEventListener("click", deleteLink);
        }

        document.getElementById("createLink").addEventListener("click", createLink)
    });
    // extract genres from the database and provide an interface for creation and deletion//

    // extract Genres from the database and provide an interface for creation and deletion//
    let GenreHTML = '<table>' +
        '<tr>' +
        '<th>Genres <br> <button id=\'createGenre\'>Create Genre</button></th>' +
        '</tr>'
    ;

    fetch('/Genre/getByID/' +id , {method: 'get'}
    ).then(response => response.json()
    ).then(genres => {
        for(let genre of genres){
            GenreHTML+=
                `<tr>` +
                `<td>${genre.Genre}</td>`+
                `<td class="last">` +
                `<button class='deleteButton' data-id='${genre.GenreID}'>Delete</button>`+
                `</td>` +
                `</tr>`;
        }
        GenreHTML += '</table>';
        document.getElementById("Genres").innerHTML = GenreHTML;

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.addEventListener("click", deleteGenre);
        }

        document.getElementById("createGenre").addEventListener("click", createGenre)
    });

    //getting posts and displaying them on the profile page.

    let ImageHTML = '<table>' +
        '<tr>' +
        '<th>Posts</th>' +
        '</tr>'
    ;

    //getting the Post from

    fetch('/Post/getByUserID/' +id , {method: 'get'}
    ).then(response => response.json()
    ).then(posts => {
        for(let post of posts){
            ImageHTML+=
                `<tr>`;
                //Extracting the Image from the server //

            let FileReference = "TestImage.png";
            fetch('/image/getByName/' +FileReference, {method: 'get'},
            ).then(response => response.json()
            ).then(images => {
                if (images.hasOwnProperty('error')) {
                    alert(images.error);
                } else {
                    imageList = images;
                    displayThumbnails(0);
                }
            });


            ImageHTML+=`</tr>`;
            ImageHTML+=
                `<tr>`+
                `<td>Date Added: ${post.DateAdded}</td>`+
                `<td>Caption: ${post.Caption}</td>`+
                `</tr>`;

            ImageHTML += '</table>';
            document.getElementById("Posts").innerHTML = ImageHTML;

        }
    });

    }
//---------------------------------------------------------------------------------------------------------------//

function createLink() {
    //parsing the logged in userID into the new javascript function//
    let id = JSON.parse(window.sessionStorage.user);
    id = JSON.stringify(id.userID);

    let Link = prompt("please enter your Link",);

    if (Link != null) {
        const formData = new FormData();
        formData.append("UserID", id);
        formData.append("Link", Link);

        let apiPath = '/SocialLink/create';
        fetch(apiPath, {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {
            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {
                //document.getElementById("listDiv").style.display = 'block';
                pageLoad();
            }
        });
    }
}
    //delete social Links
function deleteLink(event) {

    const ok = confirm("Are you sure?");

    if (ok === true) {

        let id = event.target.getAttribute("data-id");
        let formData = new FormData();
        formData.append("LinkID", id);

        fetch('/SocialLink/delete', {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {

                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {
                    pageLoad();
                }
            }
        );
    }
}
//-------------------------------------------------------------------------//

function createGenre() {
    //parsing the logged in userID into the new javascript function//
    let id = JSON.parse(window.sessionStorage.user);
    id = JSON.stringify(id.userID);

    let Genre = prompt("please enter your Genre",);

    if (Genre != null) {
        const formData = new FormData();
        formData.append("UserID", id);
        formData.append("Genre", Genre);

        let apiPath = '/Genre/create';
        fetch(apiPath, {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {
            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {
                //document.getElementById("listDiv").style.display = 'block';
                pageLoad();
            }
        });
    }
}
//delete social Links
function deleteGenre(event) {

    const ok = confirm("Are you sure?");

    if (ok === true) {

        let id = event.target.getAttribute("data-id");
        let formData = new FormData();
        formData.append("GenreID", id);

        fetch('/Genre/delete', {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {

                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {
                    pageLoad();
                }
            }
        );
    }
}
//-------------------------------------------------------------------------//

function displayThumbnails(startIndex) {

    imagesHTML = '';

    let counter = 0;
    for (let image of imageList) {
        if (counter >= startIndex && counter < startIndex + pageSize) {
            imagesHTML += `<div style="display: inline-block; width: 120px; text-align: center; border: solid 1px black; margin:10px; padding:10px;">`;
            imagesHTML += `<a href="/client/img/${image}" target="_blank">`;
            imagesHTML += `<img src="/client/img/${image}" width="100px" alt="${image}">`;
            imagesHTML += `</a>`;
            imagesHTML += `</div>`;
        }
        counter++;
    }

    imagesHTML += `<div style="margin-bottom: 32px; padding-bottom: 16px; border-bottom: solid 3px silver; text-align: center">Page `;

    let n  = 0;
    while (n < imageList.length) {
        let style = '';
        if (startIndex === n) {
            style = 'background-color: yellow';
        }
        imagesHTML += `<button style="${style}" onclick="displayThumbnails(${n})">${Math.floor(n/pageSize)+1}</button> `;
        n += pageSize;
    }

    imagesHTML += `</div>`;

}
window.onload=function(){
    let LoggedId = JSON.parse(window.sessionStorage.user);
    LoggedId = JSON.stringify(LoggedId.userID);
    let ResultsHTML = '<div>';
    fetch('/user/getFollowers/' +LoggedId , {method: 'get'}
    ).then(response => response.json()
    ).then(genres => {
        for (let genre of genres) {
            ResultsHTML +=
                '<b>Name:</b>' +
                ' '+
                `${genre.firstName}` +
                ' '+
                `${genre.lastName}`+
                ' '+
                '<b>Followers:</b>' +
                ' '+
                `${genre.Followers}` +
                ' '+
                '<b>Following:</b>' +
                ' '+
                `${genre.Following}` +
                '    ';
            let rId = genre.UserID;
            if(LoggedId != rId){
                ResultsHTML +=
                    `<button class='ViewProfile' data-id='${genre.UserID}'>View Profile</button>`;
            }
            ResultsHTML += '<hr>';
        }
        ResultsHTML +=  + '</div>';
        document.getElementById("Results").innerHTML = ResultsHTML;

        let viewButtons = document.getElementsByClassName("ViewProfile");
        for (let button of viewButtons) {
            button.addEventListener("click", ViewProfile);
        }
    });
};

function ViewProfile(event){
    let id = event.target.getAttribute("data-id");
    window.sessionStorage.view = JSON.stringify(id);
    window.location.href = 'profileView.html';
}
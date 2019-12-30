window.onload=function(){
    document.getElementById("GenSearchButton").addEventListener("click", SearchByGen);
    document.getElementById("UserSearchButton").addEventListener("click", SearchByName);
};

function SearchByGen(event){
    event.preventDefault();
    let LoggedId = JSON.parse(window.sessionStorage.user);
    LoggedId = JSON.stringify(LoggedId.userID);
    const form = document.getElementById( "SearchGenresForm");
    const formData = new FormData(form);
    let id = formData.get('genres');
    let ResultsHTML = '<div>';
    fetch('/Genre/getByGen/' +id , {method: 'get'}
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
        }
        ResultsHTML += '</div>';
        document.getElementById("Results").innerHTML = ResultsHTML;

        let viewButtons = document.getElementsByClassName("ViewProfile");
        for (let button of viewButtons) {
            button.addEventListener("click", ViewProfile);
        }
    });
}

function SearchByName(event){
    event.preventDefault();
    const form = document.getElementById( "SearchUsersForm");
    const formData = new FormData(form);

}

function ViewProfile(event){
    let id = event.target.getAttribute("data-id");
    window.sessionStorage.view = JSON.stringify(id);
    window.location.href = 'profileView.html';
}
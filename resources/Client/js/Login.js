window.onload=function(){
    document.getElementById("loginButton").addEventListener("click", login);
    document.getElementById("SignUp").addEventListener("click", signUp)
};
function login(event) {
    event.preventDefault()
    const form = document.getElementById("loginForm");
    const formData = new FormData(form);
    fetch("/user/login", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            alert(responseData);
            window.location.href = '/Client/profile.html';
        }
    });
    alert("end");
}
function signUp(){
    window.location.href = 'SignUp.html';
}
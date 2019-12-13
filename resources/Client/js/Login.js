window.onload=function(){
    document.getElementById("loginButton").addEventListener("click", login);
};
function login() {
    const form = document.getElementById("loginForm");
    const formData = new FormData(form);
    fetch("/user/login", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            alert(responseData.string());
            window.location.href = '/client/index.html';
        }
    });
    alert("end");
}
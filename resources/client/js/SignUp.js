window.onload=function(){
    document.getElementById("SignUpButton").addEventListener("click", SignUp);
};
function SignUp(event) {
    event.preventDefault();
    const form = document.getElementById( "SignUpForm");
    const formData = new FormData(form);
    fetch("/user/create", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            alert(responseData);
            window.location.href = 'Login.html';
        }
    });
}

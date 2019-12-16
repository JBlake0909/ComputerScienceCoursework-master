window.onload=function(){
    document.getElementById("SignUpForm").addEventListener("click", SignUp);
};
function SignUp(event){
    event.preventDefault();
    const id = document.getElementById("SignUpForm").value;
    const form = document.getElementById("SignUpForm");
    const formData = new FormData(form);

    fetch("/user/create", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            window.location.href = 'login.html';
        }
    });

}
const signInBtn = document.getElementById("signIn");
const signUpBtn = document.getElementById("signUp");
const fistForm = document.getElementById("form1");
const secondForm = document.getElementById("form2");
const container = document.querySelector(".container");

signInBtn.addEventListener("click", () => {
    container.classList.remove("right-panel-active");
});

signUpBtn.addEventListener("click", () => {
    container.classList.add("right-panel-active");
});

fistForm.addEventListener("submit", (e) => e.preventDefault());
secondForm.addEventListener("submit", (e) => e.preventDefault());

const Toast = Swal.mixin({
    toast: true,
    position: 'center-center',
    showConfirmButton: false,
    timer: 2000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
})

function signup() {
    let username = $('#username').val();
    let password = $('#password').val();
    let email = $('#email').val();
    let nickname = $('#nickname').val();
    let introduce = $('#introduce').val();
    let adminToken = $('#admin-token').val();
    const RegExp = /^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{3,20}$/;
    const RegExp2 = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
    const RegExp3 = /^[a-zA-Z0-9]{4,10}$/;
    const num = password.search(/[0-9]/g);
    const eng = password.search(/[a-z]/ig);
    const spe = password.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

    if (username == "") {
        Swal.fire({
            icon: 'warning',
            title: '아이디 입력오류',
            text: '아이디가 공백입니다. 문자 입력해주세요.',
        });
        $('#username').focus();
        return false;
    }

    if (!RegExp3.test(username)) {
        Swal.fire({
            icon: 'warning',
            title: '아이디 입력오류',
            text: '최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9) 로 구성되어야 합니다.',
        });
        $('#username').focus();
        return false;
    }

    if (password.length < 8 || password.length > 20) {
        Swal.fire({
            icon: 'warning',
            title: '비밀번호 입력오류',
            text: '8자리 ~ 20자리 이내로 입력해주세요.',
        });
        $('#password').focus();
        return false;
    }

    if (password.search(/\s/) != -1) {
        Swal.fire({
            icon: 'warning',
            title: '비밀번호 입력오류',
            text: '비밀번호는 공백 없이 입력해주세요.',
        });
        $('#password').focus();
        return false;
    }

    if (num < 0 || eng < 0 || spe < 0) {
        Swal.fire({
            icon: 'warning',
            title: '비밀번호 입력오류',
            text: '영문,숫자, 특수문자를 혼합하여 입력해주세요.',
        });
        $('#password').focus();
        return false;
    }

    if (email == "") {
        Swal.fire({
            icon: 'warning',
            title: '이메일 입력오류',
            text: '이메일이 공백입니다. 문자 입력해주세요.',
        });
        $('#email').focus();
        return false;
    }

    if (!RegExp2.test(email)) {
        Swal.fire({
            icon: 'warning',
            title: '이메일 입력오류',
            text: '이메일 형식에 맞게  입력해주세요.',
        });
        $('#email').focus();
        return false;
    }

    if (nickname == "") {
        Swal.fire({
            icon: 'warning',
            title: '닉네임 입력오류',
            text: '닉네임이 공백입니다. 문자 입력해주세요.',
        });
        $('#nickname').focus();
        return false;
    }

    if (!RegExp.test(nickname)) {
        Swal.fire({
            icon: 'warning',
            title: '닉네임 입력오류',
            text: '닉네임을 3글자 이상 20글자 이하혹은 공백문자가 없게 입력해주세요.',
        });
        $('#nickname').focus();
        return false;
    }

    if (introduce.length <= 10 || introduce.length > 1000) {
        Swal.fire({
            icon: 'warning',
            title: '자기소개 입력오류',
            text: '자기소개를 10글자 이상 1000글자 미만으로 입력해주세요.',
        });
        $('#introduce').focus();
        return false;
    }

    $.ajax({
        type: "POST",
        url: `/api/user/signup`,
        contentType: "application/json",
        data: JSON.stringify({
            username: username, password: password, email: email,
            nickname: nickname, introduce: introduce, adminToken: adminToken
        }),
    })
        .done(function (res, status, xhr) {
            Toast.fire({
                icon: 'success',
                title: '회원가입에 성공하셨습니다.'
            }).then(function () {
                window.location.reload();
            })
        })
        .fail(function (jqXHR, textStatus, error) {
            console.log(error);
            Toast.fire({
                icon: 'error',
                title: '회원가입에 실패하였습니다.'
            })
        });
}

function onLogin() {
    let loginUsername = $('#loginUsername').val();
    let loginPassword = $('#loginPassword').val();

    $.ajax({
        type: "POST",
        url: `/api/user/login`,
        contentType: "application/json",
        data: JSON.stringify({username: loginUsername, password: loginPassword}),
    })
        .done(function (res, status, xhr) {
            const token = xhr.getResponseHeader('Authorization');

            Cookies.set('Authorization', token, {path: '/'})

            $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
                jqXHR.setRequestHeader('Authorization', token);
            });

            Toast.fire({
                icon: 'success',
                title: loginUsername + '님 환영합니다!'
            }).then(function () {
                window.location.href = '/';
            })
        })
        .fail(function (jqXHR, textStatus) {
            Toast.fire({
                icon: 'warning',
                title: '가입한 내역 여부 혹은 로그인 정보를 확인부탁드립니다.'
            })
        });
}

function onclickAdmin() {
    // Get the checkbox
    var checkBox = document.getElementById("admin-check");
    // Get the output text
    var box = document.getElementById("admin-token");

    // If the checkbox is checked, display the output text
    if (checkBox.checked == true) {
        box.style.display = "block";
    } else {
        box.style.display = "none";
    }
}
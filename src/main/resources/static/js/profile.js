const Toast = Swal.mixin({
    toast: true,
    position: 'center-center',
    showConfirmButton: false,
    timer: 5000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
})

$(document).ready(function () {
    // Cookie에서 JWT를 가져옴
    const token = Cookies.get('Authorization');
    if (token) {
        // 모든 AJAX 요청에 헤더에 JWT를 포함하기 위한 설정
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', token);
        });
        // 사용자 프로필 정보를 가져옴
        getProfile(token);
    } else {
        console.log("JWT 토큰이 없습니다. 프로필 정보를 가져오지 않습니다.");
        Toast.fire({
            icon: 'error',
            title: '토큰 오류',
            text: 'JWT 토큰이 없습니다. 잠시후 로그인화면으로 되돌아갑니다.',
        }).then(function () {
            window.location.href = "/api/user/login-page"
        });
    }
});

$('#passwordChange').click(function () {
    window.location.href = "/api/user/profile-page/pwchange";
});


$('.inputbox').prop("disabled", true);

// 프로필 정보 가져오기
function getProfile(token) {

    // console.log(token)
    // JWT 토큰 디코딩하여 페이로드 추출
    const payload = JSON.parse(atob(token.split(".")[1]));
    // console.log(payload)

    // 예시 {sub: 'qw12345611', auth: 'USER', exp: 1689745728, iat: 1689742128}
    // 그중 username을 추출해야하니 sub를 가져옴. 만약 관리자 확인이면 auth를 가져올듯.
    const username = payload.sub;

    // console.log(username)

    $.ajax({
        url: "/api/profile",
        type: "GET",
        dataType: "json",
        success: function (data) {
            // 서버로부터 받은 데이터를 처리하는 로직을 작성합니다.
            // 예시: 프로필 정보를 표시하는 함수 호출
            // console.log(data)
            const email = data['proFileResponseDto']['email'];
            const intro = data['proFileResponseDto']['introduce'];
            const nickname = data['proFileResponseDto']['nickname']; // 실제 사용자 ID값 가져오기
            const textarea_str = intro;

            $('#userName').attr('value', nickname);
            $('#email').attr('value', email);
            $('#intro').html(textarea_str);
            $('.myid').html(username);

            // 완료 버튼 클릭 이벤트 처리
            $("#changebtnsuccess").click(function () {
                const email2 = document.getElementById("email");
                const intro2 = document.getElementById("intro")
                const nickname2 = document.getElementById("userName");
                const RegExp = /^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{3,20}$/;
                const RegExp2 = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;

                if (nickname2.value == "") {
                    Swal.fire({
                        icon: 'warning',
                        title: '닉네임 입력오류',
                        text: '닉네임이 공백입니다. 문자 입력해주세요.',
                    });
                    nickname2.focus();
                    return false;
                }

                if (!RegExp.test(nickname2.value)) {
                    Swal.fire({
                        icon: 'warning',
                        title: '닉네임 입력오류',
                        text: '닉네임을 3글자 이상 20글자 이하혹은 공백문자가 없게 입력해주세요.',
                    });
                    nickname2.focus();
                    return false;
                }

                if (email2.value == "") {
                    Swal.fire({
                        icon: 'warning',
                        title: '이메일 입력오류',
                        text: '이메일이 공백입니다. 문자 입력해주세요.',
                    });
                    email2.focus();
                    return false;
                }

                if (!RegExp2.test(email2.value)) {
                    Swal.fire({
                        icon: 'warning',
                        title: '이메일 입력오류',
                        text: '이메일 형식에 맞게  입력해주세요.',
                    });
                    email2.focus();
                    return false;
                }

                if (intro2.length <= 10 || intro2.length > 1000) {
                    Swal.fire({
                        icon: 'warning',
                        title: '자기소개 입력오류',
                        text: '자기소개를 10글자 이상 1000글자 미만으로 입력해주세요.',
                    });
                    intro2.focus();
                    return false;
                }
                updateProfile(username);
            });
        },
        error: function (error) {
            console.error("프로필 정보를 가져오는 데 실패했습니다.", error);
            Toast.fire({
                icon: 'error',
                title: '토큰 오류',
                text: '프로필 정보를 가져오는 데 실패했습니다. 잠시후 로그인 화면으로 되돌아갑니다.',
            }).then(function () {
                window.location.href = "/api/user/login-page"
            });
        }
    });
}

// 프로필 정보 업데이트하기
function updateProfile(username) {
    const email = $('#email').val();
    const intro = $('#intro').val();
    const nickname = $('#userName').val();

    Swal.fire({
        title: '프로필을 수정하시겠습니까?',
        text: "확인을 누르시면 수정이 완료됩니다.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: "/api/profile",
                type: "PUT",
                dataType: "json",
                data: JSON.stringify(
                    {
                        email: email,
                        introduce: intro,
                        nickname: nickname,
                        username: username
                    }
                ),
                contentType: "application/json",
                success: function (data) {
                    // 업데이트 후 서버로부터 받은 데이터를 처리하는 로직을 작성합니다.
                    // 예시: 업데이트된 프로필 정보를 표시하는 함수 호출
                    Swal.fire({
                            icon: 'success',
                            title: '수정 성공',
                            text: '프로필 정보가 성공적으로 업데이트되었습니다.'
                        }
                    ).then(function () {
                        window.location.reload();
                    })
                },
                error: function (error) {
                    console.error("프로필 정보를 가져오는 데 실패했습니다.", error);
                    Toast.fire({
                        icon: 'error',
                        title: '토큰 오류',
                        text: '프로필 정보를 가져오는 데 실패했습니다. 잠시후 로그인 화면으로 되돌아갑니다.',
                    }).then(function () {
                        window.location.href = "/api/user/login-page"
                    });
                }
            });
        }
    })
};

// 수정 버튼 클릭 이벤트 처리
$("#changebtn").click(function () {
    $("#cancel").show();
    $("#changebtn").hide();
    $("#changebtnsuccess").show();
    $(".inputbox").prop("disabled", false);
    $("#intro").css("color", "black");
    $('#profilecheck').hide();
    $('#profilechange').show();

    const DEFAULT_HEIGHT = 30; // textarea 기본 height

    const $textarea = document.querySelector('#intro');

    $textarea.oninput = (event) => {
        const $target = event.target;

        $target.style.height = 0;
        $target.style.height = DEFAULT_HEIGHT + $target.scrollHeight + 'px';
    };
});

// 취소 버튼 클릭 이벤트 처리
$("#cancel").click(function () {
    window.location.reload();
});


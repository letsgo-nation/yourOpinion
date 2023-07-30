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

$('#cancel').click(function () {
    window.location.href = "/api/user/profile-page";
});

$(document).ready(function () {
    // Cookie에서 JWT를 가져옴
    const token = Cookies.get('Authorization');
    if (token) {
        // 모든 AJAX 요청에 헤더에 JWT를 포함하기 위한 설정
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', token);
        });

        getid(token)

        // 사용자 프로필 정보를 가져옴
        $('#pwUpdate').click(function () {
            for (let i = 1; i < 3; i++) {
                const pw = $("#memberPw" + i).val()
                const num = pw.search(/[0-9]/g);
                const eng = pw.search(/[a-z]/ig);
                const spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

                if (pw.length < 8 || pw.length > 20) {
                    Swal.fire({
                        icon: 'warning',
                        title: '비밀번호 입력오류',
                        text: '8자리 ~ 20자리 이내로 입력해주세요.',
                    });
                    return false;
                }
                if (pw.search(/\s/) != -1) {
                    Swal.fire({
                        icon: 'warning',
                        title: '비밀번호 입력오류',
                        text: '비밀번호는 공백 없이 입력해주세요.',
                    });
                    return false;
                }
                if (num < 0 || eng < 0 || spe < 0) {
                    Swal.fire({
                        icon: 'warning',
                        title: '비밀번호 입력오류',
                        text: '영문,숫자, 특수문자를 혼합하여 입력해주세요.',
                    });
                    return false;
                }
            }

            if ($('#memberPw1').val() == '') {
                Swal.fire({
                    icon: 'warning',
                    title: '비밀번호 미입력',
                    text: '현재 비밀번호를 입력해주세요.',
                });
                $("#memberPw1").focus();
                return false
            }
            if ($('#memberPw2').val() == '') {
                Swal.fire({
                    icon: 'warning',
                    title: '비밀번호 미입력',
                    text: '새로운 비밀번호를 입력해주세요.',
                });
                $("#memberPw1").focus();
                return false
            }
            if ($('#memberPw3').val() == '') {
                Swal.fire({
                    icon: 'warning',
                    title: '비밀번호 미입력',
                    text: '새로운 비밀번호 확인을 입력해주세요.',
                });
                $("#memberPw1").focus();
                return false
            }
            if ($("#memberPw2").val() != $("#memberPw3").val()) {
                Swal.fire({
                    icon: 'warning',
                    title: '비밀번호 미입력',
                    text: '변경비밀번호가 일치하지 않습니다.',
                });
                $("#memberPw3").focus();
                return false
            }
            updatepassword(token);
        });
    } else {
        Toast.fire({
            icon: 'error',
            title: '토큰 오류',
            text: '프로필 정보를 가져오는 데 실패했습니다. 잠시후 로그인 화면으로 되돌아갑니다.',
        }).then(function () {
            window.location.href = "/api/user/login-page"
        });
    }
});

const getid = (token) => {
    // JWT 토큰 디코딩하여 페이로드 추출
    const payload = JSON.parse(atob(token.split(".")[1]));
    // console.log(payload)

    // 예시 {sub: 'qw12345611', auth: 'USER', exp: 1689745728, iat: 1689742128}
    // 그중 username을 추출해야하니 sub를 가져옴. 만약 관리자 확인이면 auth를 가져올듯.
    const username = payload.sub;

    $('.myid').html(username);
}


// 프로필 정보 업데이트하기
function updatepassword(token) {
    const checkPassword = $('#memberPw1').val();
    const newPassword = $('#memberPw3').val();

    // console.log(token)
    // JWT 토큰 디코딩하여 페이로드 추출
    const payload = JSON.parse(atob(token.split(".")[1]));
    // console.log(payload)

    // 예시 {sub: 'qw12345611', auth: 'USER', exp: 1689745728, iat: 1689742128}
    // 그중 username을 추출해야하니 sub를 가져옴. 만약 관리자 확인이면 auth를 가져올듯.
    const username = payload.sub;
    // console.log(username)
    Swal.fire({
        title: '비밀번호를 수정하시겠습니까?',
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
                url: "/api/password",
                type: "PUT",
                dataType: "json",
                data: JSON.stringify(
                    {
                        checkPassword: checkPassword,
                        newPassword: newPassword,
                        username: username
                    }
                ),
                contentType: "application/json",
                success: function (data) {
                    Toast.fire({
                        icon: 'success',
                        title: '수정 성공',
                        text: '비밀번호가 성공적으로 업데이트되었습니다. 잠시후 프로필수정 페이지로 되돌아갑니다.',
                    }).then(function () {
                        window.location.href = "/api/user/profile-page"
                    })
                },
                error: function (error) {
                    if (error.responseJSON && error.responseJSON.errorMessage) {
                        const errorMessage = error.responseJSON.errorMessage;
                        console.log("에러 메시지:", errorMessage);
                        Swal.fire(
                            {
                                icon: 'warning',
                                title: '수정 실패',
                                text: errorMessage
                            }
                        )
                    } else {
                        const errorMessage = error.responseJSON.errorMessage;
                        Swal.fire(
                            {
                                icon: 'warning',
                                title: '수정 실패',
                                text: errorMessage
                            }
                        )
                        console.error("비밀번호 변경에 실패했습니다.", errorMessage);
                    }

                }
            });
        }
    })
}
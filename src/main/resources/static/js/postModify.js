$(document).ready(function () {
    const token = Cookies.get('Authorization');

    if (token) { // 모든 AJAX 요청에 헤더에 JWT를 포함하기 위한 설정
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', token);
        });
        // 사용자 프로필 정보를 가져옴
        getUser(token);
    } else {
        alert("로그인이 안 되어있습니다. 게시글 등록이 불가합니다.");
        window.location.href = "/";
        console.log("JWT 토큰이 없습니다. 게시글 등록이 불가합니다.");
    }
});

function getUser(token) {
    // JWT 토큰 디코딩하여 페이로드 추출
    const payload = JSON.parse(atob(token.split(".")[1]));
    // console.log(payload)
    // 예시 {sub: 'qw12345611', auth: 'USER', exp: 1689745728, iat: 1689742128}
    // 그중 username을 추출해야하니 sub를 가져옴. 만약 관리자 확인이면 auth를 가져올듯.
    const username = payload.sub;
    console.log(username);
    document.getElementById("username").innerText = username;
}

function updatePost() {
    let title = $('#title').val();
    let content = $('#content').val();
    let opinionA = $('#opinionA').val();
    let opinionB = $('#opinionB').val();
    console.log(title,content,opinionA,opinionB);

    if (title == "") {
        console.log("title.value null");
        Swal.fire({
            icon: 'warning',
            title: '제목 입력오류',
            text: '제목이 공백입니다. 문자 입력해주세요.',
        });
        title.focus();
        return false;
    }
    if (content == "") {
        console.log("content.value null");
        Swal.fire({
            icon: 'warning',
            title: '내용 입력오류',
            text: '내용이 공백입니다. 문자 입력해주세요.',
        });
        content.focus();
        return false;
    }
    if (opinionA == "") {
        console.log("opinionA.value null");
        Swal.fire({
            icon: 'warning',
            title: '옵션1 입력오류',
            text: '옵션1이 공백입니다. 문자 입력해주세요.',
        });
        opinionA.focus();
        return false;
    }
    if (opinionB == "") {
        console.log("opinionB.value null");
        Swal.fire({
            icon: 'warning',
            title: '옵션2 입력오류',
            text: '옵션2이 공백입니다. 문자 입력해주세요.',
        });
        opinionB.focus();
        return false;
    }
    console.log("null인 필드 없음.");
    putRequest(title,content,opinionA,opinionB);
}

function putRequest(title,content,opinionA,opinionB) {
    const token = Cookies.get('Authorization');
    console.log(title,content,opinionA,opinionB);
    $.ajax({
        type: "PUT",
        url: "/api/posts/" + postId,
        dataType: 'json',
        headers: {              // Http header
            "Content-Type": "application/json",
            "Authorization": token
        },
        data: JSON.stringify({
            "title": title,
            "content": content,
            "opinionA": opinionA,
            "opinionB": opinionB
        }),
        success: (function (res, status, xhr) {
            Swal.fire({
                    icon: 'success',
                    title: '수정 완료',
                    text: '게시글 수정이 성공적으로 완료되었습니다.'
                }
            ).then(function () {
                window.location.href = "/api/post/detail-page/" + postId;
            })
        }),
        error: (function (error) {
            console.error("게시글 수정을 실패했습니다.", error);
            Toast.fire({
                icon: 'error',
                title: '수정 실패',
                text: '게시글 수정을 실패했습니다. 다시 시도 부탁드립니다.',
            }).then(function () {
                window.location.reload();
            });
        })
    })
}
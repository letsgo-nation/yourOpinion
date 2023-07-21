console.log(logininfo)

// 서버로 부터 데이터를 받아옴
const opinionResult = {
    opinionA: opinionA,
    opinionB: opinionB,
};

// 전체 투표 수 합산
const totalOpinion = opinionResult.opinionA + opinionResult.opinionB;

// 퍼센트 계산 (반올림)
const opinionPercentA = Math.round((opinionResult.opinionA / totalOpinion) * 100);
// console.log(opinionPercentA)
const opinionPercentB = Math.round((opinionResult.opinionB / totalOpinion) * 100);
// console.log(opinionPercentB)

// NaN 검사 후 표시
$('#opinionPercentA').text(isNaN(opinionPercentA) ? "투표를 시작하세요!" : "투표수: " + opinionA + "개(" + opinionPercentA + "%" + ")");
$('#opinionPercentB').text(isNaN(opinionPercentB) ? "투표를 시작하세요!" : "투표수: " + opinionB + "개(" + opinionPercentB + "%" + ")");

// 게이지 업데이트
// $('#progressA').css('width', opinionPercentA + "%");
// $('#progressB').css('width', opinionPercentB + "%");
if (isNaN(opinionPercentA)) {
    $('#progressAdiv').hide();
} else {
    $('#progressAdiv').show();
    $('#progressA').css('width', opinionPercentA + "%");
}

if (isNaN(opinionPercentB)) {
    $('#progressBdiv').hide();
} else {
    $('#progressBdiv').show();
    $('#progressB').css('width', opinionPercentB + "%");
}

$(document).ready(function () {
    const token = Cookies.get('Authorization');
    if (token) {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', token);
        });
    }
    console.log(token)
    // JWT 토큰 디코딩하여 페이로드 추출
    const payload = JSON.parse(atob(token.split(".")[1]));
    console.log(payload)

    // 예시 {sub: 'qw12345611', auth: 'USER', exp: 1689745728, iat: 1689742128}
    // 그중 username을 추출해야하니 sub를 가져옴. 만약 관리자 확인이면 auth를 가져올듯.
    const userid = payload.sub;

    console.log(userid)

    $.ajax({
        url: "/api/profile",
        type: "GET",
        dataType: "json",
        success: function (data) {
            // 서버로부터 받은 데이터를 처리하는 로직을 작성합니다.
            // 예시: 프로필 정보를 표시하는 함수 호출
            // console.log(data)
            const nickname = data['proFileResponseDto']['nickname']; // 실제 사용자 ID값 가져오기
            $('.commentNickname').text(nickname);

        },
        error: function (error) {
            console.error("프로필 정보를 가져오는 데 실패했습니다.", error);
            Toast.fire({
                icon: 'error',
                title: '토큰 오류',
                text: '프로필 정보를 가져오는 데 실패했습니다.',
            }).then(function () {
                window.location.href = "/"
            });
        }
    });
})

// 게시글 수정 버튼 클릭 시
$('#editBtn').click(function () {
    window.location.href = "/api/post/modify-page/" + postId;
});

// 게시글 삭제 버튼 클릭 시
$('#deleteBtn').click(function () {
    const isConfirmed = confirm("정말로 삭제하시겠습니까?");
    if (isConfirmed) {
        const url = "/api/posts/" + postId;
        deletePost(url);
    }
});

// Reply 버튼 클릭 시
$('#replyBtn').click(function () {
    createReply(); // 함수 만들어야 함. 어떤 댓글인지 id 값이 필요
});

$(document).ready(function () {
    const token = Cookies.get('Authorization');

    if (token) {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', token);
        });
        getUser(token);
        //displayBtn(token);
    } else {
        console.log('로그인이 안 되어있습니다.');
    }

    const commentUrl = "/api/posts/" + postId + "/comment";
    // console.log(commentUrl)

    //아이디 암호화 함수
    function maskingName() {
        // console.log(username)
        if (username.length >= 8) {
            return (
                username.slice(0, 3) +
                "*".repeat(Math.max(0, username.length - 5)) +
                username.slice(-3)
            );
        } else {
            return (
                username.slice(0, 2) +
                "*".repeat(Math.max(0, username.length - 3)) +
                username.slice(-1)
            );
        }
    }

    const maskedUsername = maskingName(username);

    $('#userinfo').html(nickname + "(" + maskedUsername + ")")

})

function getToken() {
    const token = Cookies.get('Authorization');

    if (token) {
        return token;
    } else {

    }

}

function maskingName(username) {
    if (username.length >= 8) {
        return (
            username.slice(0, 3) +
            "*".repeat(Math.max(0, username.length - 5)) +
            username.slice(-3)
        );
    } else {
        return (
            username.slice(0, 2) +
            "*".repeat(Math.max(0, username.length - 3)) +
            username.slice(-1)
        );
    }
}

const maskedUsername = maskingName(username);

$('#userinfo').text(nickname + " (" + maskedUsername + ")");


function getUser(token) {
    // console.log(token);
    const editBtn = document.getElementById('editBtn');
    const deleteBtn = document.getElementById('deleteBtn');

    const payload = JSON.parse(atob(token.split(".")[1]));
    const loginUsername = payload.sub; // 로그인한 id
    const writerUsername = username;
    // console.log(writerUsername);

    $('#loginUserName').text(loginUsername)

    // 게시글 작성자일 경우
    if (loginUsername === writerUsername) {
        // 수정, 삭제 버튼 활성화
        editBtn.style.display = 'block';
        deleteBtn.style.display = 'block';
    } else {
        // 수정, 삭제 버튼 활성화
        editBtn.style.display = 'none';
        deleteBtn.style.display = 'none';
    }
}


function formatDate(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// 게시글 삭제하기
function deletePost(url) {
    const token = Cookies.get('Authorization');
    $.ajax({
        url: url,
        type: "DELETE",
        dataType: "json",
        headers: {              // Http header
            "Content-Type": "application/json",
            "Authorization": token
        },
        success: function (data) {
            Swal.fire({
                    icon: 'success',
                    title: '삭제 성공',
                    text: '게시글 삭제가 성공적으로 완료되었습니다.'
                }
            ).then(function () {
                window.location.href = "/";
            })
        },
        error: function (error) {
            Toast.fire({
                icon: 'error',
                title: '삭제 실패',
                text: '작성자가 맞는지 확인 부탁드립니다.',
            }).then(function () {
                window.location.reload();
            });
        }
    });
}

console.log("post", post);

for (var i = 0; i < post['commentResponseDtoList'].length; i++) {
    const dateString = post['commentResponseDtoList'][i].modifiedAt;
    const formattedDate = formatDate(dateString);
    let temp_html =
        `<div class="grid1_of_2" id="${post['commentResponseDtoList'][i].id}">
                            <div class="grid_img">
                                <a href=""><img src="" alt=""></a>
                            </div>
                            <div class="grid_text">
                                <h4 class="style1 list"><a href="#">${post['commentResponseDtoList'][i].username}</a></h4>
                                <h3 class="style">${formattedDate}</h3>
                                <p class="para top"> ${post['commentResponseDtoList'][i].content}</p>
                                <a id="testid" class="btn1" onclick="updateComment(${post['commentResponseDtoList'][i].id})">수정</a>
                                <a class="btn1" onclick="deleteComment(${post['commentResponseDtoList'][i].id})">삭제</a>
                            </div>
                            <div class="clear"></div>
                 </div>`;
    $('#comment').append(temp_html);
}

function insert() {
    const token = Cookies.get('Authorization');
    // 댓글 내용 입력 안 했을 경우
    let userComment = document.getElementById('userComment').value;

    if (userComment.trim() === "") {
        Swal.fire({
            icon: 'warning',
            title: '댓글 내용 입력 오류',
            text: '댓글 내용을 입력해주세요.',
        }).then(function () {
            userComment.focus();
            return false;
        })
    }

    $.ajax({
        type: "POST",
        url: `/api/posts/${post.id}/comment`,
        headers: {              // Http header
            "Content-Type": "application/json",
            "X-HTTP-Method-Override": "POST",
            "Authorization": token
        },
        data: JSON.stringify({content: $('#userComment').val()}),
    })
        .done(function (response, status, xhr) {
            console.log("resultL", response['result']);
            // 댓글 작성 성공 안내창
            Swal.fire({
                icon: 'success',
                title: '작성 성공',
                text: '댓글 작성이 성공적으로 완료되었습니다.'
            }).then(function () {
                window.location.href = `/api/post/detail-page/${post.id}`;
            })


            const dateString = response.modifiedAt;
            const formattedDate = formatDate(dateString);

            let temp_html =
                `  <div class="grid1_of_2" id="${response['result'].id}" >
                            <div class="grid_img">
                                <a href=""><img src="" alt=""></a>
                            </div>
                            <div class="grid_text">
                                <h4 class="style1 list"><a href="#">${response['result'].username}</a></h4>
                                <h3 class="style">${formattedDate}</h3>
                                <p class="para top"> ${response['result'].content}</p>
                                <a id="testid" class="btn1" onclick="updateComment(${response['result'].id})">수정</a>
                                <a class="btn1" onclick="deleteComment(${response['result'].id})">삭제</a>
                            </div>
                            <div class="clear"></div>
                        </div>`;

            $('#comment').append(temp_html);
            $('#userComment').val('');
        })
        .fail(function (jqXHR, textStatus) {
        });
}

function deleteComment(id) {
    const token = Cookies.get('Authorization');

    const isConfirmed = confirm("정말로 삭제하시겠습니까?");
    if (isConfirmed) {
        $.ajax({
            type: "DELETE",
            url: `/api/comments/${id}`,
            headers: {              // Http header
                "Content-Type": "application/json",
                "Authorization": token
            },
        })
            .done(function (response, status, xhr) {
                Swal.fire({
                    icon: 'success',
                    title: '삭제 성공',
                    text: '댓글 삭제가 성공적으로 완료되었습니다.'
                }).then(function () {
                    window.location.reload();
                })
            })
            .fail(function (jqXHR, textStatus) {
                Swal.fire({
                    icon: 'error',
                    title: '삭제 실패',
                    text: '본인이 작성한 댓글만 삭제 부탁드립니다.',
                }).then(function () {
                    window.location.reload();
                });
            });
    }
}

function updateComment(id) {
    const token = Cookies.get('Authorization');
    //다시 불러오기
    $.ajax({
        type: "GET",
        url: `/api/post/detail-page/${post.id}`,
        headers: {              // Http header
            "Content-Type": "application/json",
            "Authorization": token
        },
    })
        .done(function (response, status, xhr) {

            for (var i = 0; i < post['commentResponseDtoList'].length; i++) {

                const dateString = post['commentResponseDtoList'][i].modifiedAt;
                const formattedDate = formatDate(dateString);

                if (post['commentResponseDtoList'][i].id == id) {

                    //작성자 확인
                    if (`${post.username}` != `${post['commentResponseDtoList'][i].username}`) {
                        Swal.fire({
                            icon: 'error',
                            title: '수정 불가',
                            text: '본인이 작성한 댓글만 수정 부탁드립니다.',
                        }).then(function () {
                            window.location.reload();
                            return false;
                        });
                    }

                    //비우기
                    const node = document.getElementById(`${id}`);
                    node.remove();
                    // 수정 append
                    let temp_html =
                        `  <div class="grid1_of_2" id="${post['commentResponseDtoList'][i].id}" >
                                    <div class="grid_img">
                                        <a href=""><img src="" alt=""></a>
                                    </div>
                                    <div class="grid_text">
                                        <h4 class="style1 list"><a href="#">${post['commentResponseDtoList'][i].username}</a></h4>
                                        <h3 class="style">${formattedDate}"</h3>
                                        <p class="para top"><input id="input" value="${post['commentResponseDtoList'][i].content}"/></p>
                                        <a class="btn1" onclick="update(${post['commentResponseDtoList'][i].id}, $('#input').val())">수정</a>
                                        <a class="btn1" onclick="deleteComment(${post['commentResponseDtoList'][i].id})">삭제</a>
                                    </div>
                                    <div class="clear"></div>
                                </div>`;
                    $('#comment').find($("a[id^='testid']")).empty();
                    $('#comment').append(temp_html);
                }
            }

        })
        .fail(function (jqXHR, textStatus) {
            Swal.fire({
                icon: 'error',
                title: '수정 불가',
                text: '본인이 작성한 댓글만 수정 부탁드립니다.',
            }).then(function () {
                window.location.reload();
            });
        });
}

function update(id, value) {
    const token = Cookies.get('Authorization');
    // 댓글 내용 입력 안 했을 경우
    let userComment = document.getElementById('userComment').value;

    if (value.trim() === "") {
        Swal.fire({
            icon: 'warning',
            title: '댓글 내용 입력 오류',
            text: '댓글 내용을 입력해주세요.',
        }).then(function () {
            value.focus();
            return false;
        });
    }

    $.ajax({
        type: "PUT",
        url: `/api/comments/${id}`,
        headers: {              // Http header
            "Content-Type": "application/json",
            "X-HTTP-Method-Override": "PUT",
            "Authorization": token
        },
        data: JSON.stringify({content: value}),
    })
        .done(function (response, status, xhr) {
            Swal.fire({
                icon: 'success',
                title: '수정 성공',
                text: '댓글 수정이 성공적으로 완료되었습니다.'
            }).then(function () {
                window.location.reload();
            })
        })
        .fail(function (jqXHR, textStatus) {

        });
}
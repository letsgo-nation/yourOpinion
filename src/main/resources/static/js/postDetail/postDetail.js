
// alert 창 표시를 위한 변수 선언
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
        // JWT 토큰 디코딩하여 페이로드 추출
        // 예시 {sub: 'qw12345611', auth: 'USER', exp: 1689745728, iat: 1689742128}
        // 그중 username을 추출해야하니 sub를 가져옴. 만약 관리자 확인이면 auth를 가져올듯.
        const payload = JSON.parse(atob(token.split(".")[1]));
        const userid = payload.sub;
    }
    voteBtnDisplay();
})

function removeToken(){
    Cookies.remove('Authorization', {path: '/'});
    Swal.fire({
        icon: 'warning',
        title: '로그인 만료',
        text: '인증이 만료되어 재로그인 부탁드립니다.',
    }).then(function () {
        window.location.href = "/";
    })
}

// 투표 데이터에 따라 버튼 활성화, 비활성화
function voteBtnDisplay() {
    const url = `/api/posts/${postId}/opinions`;
    $.ajax({
        url: url,
        type: "GET",
        dataType: "json",
        success: function (data) {
            const opinionA = data['opinionAresult']; // 투표할 경우 true, 투표 안하거나 취소한 경우 false
            const opinionB = data['opinionBresult']; // 투표할 경우 true, 투표 안하거나 취소한 경우 false

            if (opinionA) { // 옵션1이 true 일 경우
                document.getElementById('inOpinionA').style.display = 'none';
                document.getElementById('deOpinionA').style.display = 'block';
                document.getElementById('inOpinionB').style.display = 'none';
                document.getElementById('deOpinionB').style.display = 'none';
            } else if (opinionB) { // 옵션1은 false고, 옵션2가 true이면
                document.getElementById('inOpinionA').style.display = 'none';
                document.getElementById('deOpinionA').style.display = 'none';
                document.getElementById('inOpinionB').style.display = 'none';
                document.getElementById('deOpinionB').style.display = 'block';
            } else { // 투표 정보가 없는 사용자의 경우 결과를 보여줄지 말지
            }
        },
        error: function (error) {
            Swal.fire({
                icon: 'warning',
                title: '투표 불가',
                text: '투표를 위해 로그인 부탁드립니다.',
            }).then(function () {
                document.getElementById('inOpinionA').style.display = 'none';
                document.getElementById('deOpinionA').style.display = 'none';
                document.getElementById('inOpinionB').style.display = 'none';
                document.getElementById('deOpinionB').style.display = 'none';
            });
        }
    });
}// end of voteBtnDisplay();

// 투표1 증가 시
function increaseOpinionA() {
    const token = Cookies.get('Authorization');
    const url = `/api/posts/${postId}/opinionA`;
    // 옵션1 투표
    $.ajax({
        type: "POST",
        url: url,
        headers: {              // Http header
            "Content-Type": "application/json",
            "X-HTTP-Method-Override": "POST",
            "Authorization": token
        },
        data: {},
    })
        .done(function (res, status, xhr) {
            Swal.fire({
                icon: 'success',
                title: '투표 성공',
                text: nickname + '님, 투표 완료되었습니다.'
            }).then(function () {
                window.location.reload();
            })
        })
        .fail(function (jqXHR, textStatus) {
            Toast.fire({
                icon: 'warning',
                title: '투표를 실패하였습니다.'
            })
        });
}// end of increaseOpinionA();

// 투표1 감소 시
function decreaseOpinionA() {
    const token = Cookies.get('Authorization');
    const url = `/api/posts/${postId}/opinionA`;
    // 옵션1 투표 취소
    $.ajax({
        type: "PUT",
        url: url,
        headers: {              // Http header
            "Content-Type": "application/json",
            "X-HTTP-Method-Override": "PUT",
            "Authorization": token
        },
        data: {},
    })
        .done(function (res, status, xhr) {
            Swal.fire({
                icon: 'success',
                title: '투표 취소 성공',
                text: nickname + '님, 투표가 취소되었습니다.'
            }).then(function () {
                window.location.reload();
            })
        })
        .fail(function (jqXHR, textStatus) {
            Toast.fire({
                icon: 'warning',
                title: '투표 취소를 실패하였습니다.'
            })
        });
}// end of decreaseOpinionA();

// 투표2 증가 시
function increaseOpinionB() {
    const token = Cookies.get('Authorization');
    const url = `/api/posts/${postId}/opinionB`;
    // 옵션2 투표
    $.ajax({
        type: "POST",
        url: url,
        headers: {              // Http header
            "Content-Type": "application/json",
            "X-HTTP-Method-Override": "POST",
            "Authorization": token
        },
        data: {},
    })
        .done(function (res, status, xhr) {
            Swal.fire({
                icon: 'success',
                title: '투표 성공',
                text: nickname + '님, 투표 완료되었습니다.'
            }).then(function () {
                window.location.reload();
            })
        })
        .fail(function (jqXHR, textStatus) {
            Toast.fire({
                icon: 'warning',
                title: '투표를 실패하였습니다.'
            })
        });
}// end of increaseOpinionB();

// 투표2 감소 시
function decreaseOpinionB() {
    const token = Cookies.get('Authorization');
    const url = `/api/posts/${postId}/opinionB`;
    // 옵션2 투표 취소
    $.ajax({
        type: "PUT",
        url: url,
        headers: {              // Http header
            "Content-Type": "application/json",
            "X-HTTP-Method-Override": "PUT",
            "Authorization": token
        },
        data: {},
    })
        .done(function (res, status, xhr) {
            Swal.fire({
                icon: 'success',
                title: '투표 취소 성공',
                text: nickname + '님, 투표가 취소되었습니다.'
            }).then(function () {
                window.location.reload();
            })
        })
        .fail(function (jqXHR, textStatus) {
            Toast.fire({
                icon: 'warning',
                title: '투표 취소를 실패하였습니다.'
            })
        });
}// end of decreaseOpinionB();


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
    showComments();
    const token = Cookies.get('Authorization');

    if (token) {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', token);
        });
        getUser(token);
    }

    //아이디 암호화 함수
    maskingName(username);

    const maskedUsername = maskingName(username);
    $('#userinfo').html(nickname + "(" + maskedUsername + ")")

    // 게시글 날짜 표시
    const formattedDate = setDateFormat(modifiedAt);
    const postDate = document.getElementById('postDate');
    postDate.textContent = formattedDate;

})

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

function getUser(token) {
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

function setDateFormat(inputDate){
    const date = new Date(inputDate);

    return date.toLocaleString("ko-KR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
    });
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

function showComments() {
    console.log("post", post);
    for (var i = 0; i < post['commentResponseDtoList'].length; i++) {
        const dateString = post['commentResponseDtoList'][i].modifiedAt;
        const formattedDate = setDateFormat(dateString);
        let temp_html =
            `<div class="grid1_of_2" id="${post['commentResponseDtoList'][i].id}">
                            <div class="grid_img">
                                <a href=""><img src="" alt=""></a>
                            </div>
                            <div class="grid_text">
                                <hr style="border-top: 1px solid #6c757d;">
                                <h4 class="style1 list" style="font-family: 'Jua', sans-serif;"><a href="#" style="font-family: 'Jua', sans-serif;"
                                >${post['commentResponseDtoList'][i].username}</a></h4>
                                <p class="para top" style="font-family: 'Jua', sans-serif;">${formattedDate}</p>
                                <h4 class="style1 list"> ${post['commentResponseDtoList'][i].content}</h4>
                                <a id="testid" class="btn1" style="font-family: 'Jua', sans-serif;"
                                onclick="updateComment(${post['commentResponseDtoList'][i].id})">수정</a>
                                <a class="btn1" style="font-family: 'Jua', sans-serif;"
                                onclick="deleteComment(${post['commentResponseDtoList'][i].id})">삭제</a>
                            </div>
                            <div class="clear"></div>
                 </div>`;
        $('#comment').append(temp_html);
    }
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

    console.log(postId);

    $.ajax({
        type: "POST",
        url: `/api/posts/${postId}/comment`,
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
                window.location.href = `/api/post/detail-page/${postId}`;
            })


            const dateString = response.modifiedAt;
            const formattedDate = setDateFormat(dateString);

            let temp_html =
                `  <div class="grid1_of_2" id="${response['result'].id}" >
                            <div class="grid_img">
                                <a href=""><img src="" alt=""></a>
                            </div>
                            <div class="grid_text">
                                <hr style="border-top: 1px solid #6c757d;">
                                <h4 class="style1 list" style="font-family: 'Jua', sans-serif;"><a href="#" 
                                 style="font-family: 'Jua', sans-serif;">${response['result'].username}</a></h4>
                                <p class="para top" style="font-family: 'Jua', sans-serif;">${formattedDate}</p>
                                <h4 class="style1 list">${response['result'].content}</h4>
                                <a id="testid" class="btn1" style="font-family: 'Jua', sans-serif;"
                                onclick="updateComment(${response['result'].id})">수정</a>
                                <a class="btn1" style="font-family: 'Jua', sans-serif;"
                                onclick="deleteComment(${response['result'].id})">삭제</a>
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
        url: `/api/post/detail-page/${postId}`,
        headers: {              // Http header
            "Content-Type": "application/json",
            "Authorization": token
        },
    })
        .done(function (response, status, xhr) {

            for (var i = 0; i < post['commentResponseDtoList'].length; i++) {

                const dateString = post['commentResponseDtoList'][i].modifiedAt;
                const formattedDate = setDateFormat(dateString);

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
                                        <hr style="border-top: 1px solid #6c757d;">
                                        <h4 class="style1 list" style="font-family: 'Jua', sans-serif;"><a href="#"
                                         style="font-family: 'Jua', sans-serif;">${post['commentResponseDtoList'][i].username}</a></h4>
                                        <p class="para top" style="font-family: 'Jua', sans-serif;">${formattedDate}</p>
                                        <h4 class="style1 list"><input id="input" value="${post['commentResponseDtoList'][i].content}"/></h4>
                                        <a class="btn1" style="font-family: 'Jua', sans-serif;"
                                        onclick="update(${post['commentResponseDtoList'][i].id}, $('#input').val())">수정</a>
                                        <a class="btn1" style="font-family: 'Jua', sans-serif;"
                                        onclick="deleteComment(${post['commentResponseDtoList'][i].id})">삭제</a>
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
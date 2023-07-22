addEventListener("load", function () {
    setTimeout(hideURLbar, 0);
}, false);

function hideURLbar() {
    window.scrollTo(0, 1);
}

var $ = jQuery.noConflict();
$(function () {
    $('#activator').click(function () {
        $('#box').animate({'top': '0px'}, 500);
    });
    $('#boxclose').click(function () {
        $('#box').animate({'top': '-700px'}, 500);
    });
});
$(document).ready(function () {
    //Hide (Collapse) the toggle containers on load
    $(".toggle_container").hide();
    //Switch the "Open" and "Close" state per click then slide up/down (depending on open/close state)
    $(".trigger").click(function () {
        $(this).toggleClass("active").next().slideToggle("slow");
        return false; //Prevent the browser jump to the link anchor
    });

});

$(document).ready(function () {
    const apiUrl = "/api/posts";

    // 쿠키값 가져오기
    let token = Cookies.get('Authorization');
    if (token) {
        //쿠키 있을때
        getUser(token)
        $("#login").hide();
    } else {
        //쿠키 없을때
        $("#profile").hide();
        $(".dropdown").hide();
        removeToken();
    }

    $.ajax({
        url: apiUrl,
        type: "GET",
        dataType: "json",
        success: function (data) {
            // $("#loadingMessage").hide();
            // 서버에서 데이터를 가져오면 로딩 메시지를 숨기고 게시물을 동적으로 생성합니다.
            displayPosts(data.result); // 서버에서 받은 데이터로 게시물 생성
        },
        error: function (error) {
            // 데이터 가져오기에 실패한 경우 에러 메시지를 출력합니다.
            console.error("Error fetching posts:", error);
            $("#loadingMessage").text("게시물을 불러오는 데 실패했습니다.");
        },
    });
});

function getUser(token) {
    const payload = JSON.parse(atob(token.split(".")[1]));
    const username = payload.sub;
    if(username ===""){
        removeToken();
    }
    console.log(payload);
    $("#profile").text(username + "님 환영합니다!");
}

function removeToken() {
    Cookies.remove('Authorization', {path: '/'});
}


function displayPosts(posts) {
    const $tiles = $('#tiles'); // #tiles 요소를 찾습니다.
    let postElements = '';
    posts.forEach((post) => {
        postElements += createPostElement(post);
    });
    $tiles.html(postElements); // #tiles 요소에 게시물 요소들을 추가합니다.

    (function ($) {
        var $tiles = $('#tiles'),
            $handler = $('li', $tiles),
            $main = $('#main'),
            $window = $(window),
            $document = $(document),
            options = {
                autoResize: true, // This will auto-update the layout when the browser window is resized.
                container: $main, // Optional, used for some extra CSS styling
                offset: 20, // Optional, the distance between grid items
                itemWidth: 280 // Optional, the width of a grid item
            };

        /**
         * Reinitializes the wookmark handler after all images have loaded
         */
        function applyLayout() {
            $tiles.imagesLoaded(function () {
                // Destroy the old handler
                if ($handler.wookmarkInstance) {
                    $handler.wookmarkInstance.clear();
                }

                // Create a new layout handler.
                $handler = $('li', $tiles);
                $handler.wookmark(options);
            });
        }

        /**
         * When scrolled all the way to the bottom, add more tiles
         */
        // 스크롤 이벤트를 처리하는 함수
        function onScroll() {
            // 스크롤이 끝에 도달하고, 이미 스크롤 이벤트가 처리 중인 경우에만 실행
            var winHeight = window.innerHeight ? window.innerHeight : $window.height(),
                closeToBottom = ($window.scrollTop() + winHeight > $document.height() - 100);

            if (closeToBottom && !scrollEventProcessing) {
                // 스크롤 이벤트가 처리 중인 상태를 저장
                scrollEventProcessing = true;
                // 새로운 게시물을 추가하는 비동기 요청 수행
                loadMorePosts(function () {
                    // 비동기 요청이 완료된 후에 스크롤 이벤트 처리 상태를 초기화
                    scrollEventProcessing = false;
                });
            }
        }

        // 새로운 게시물을 추가하는 비동기 함수 (여기서는 예시로 빈 함수로 처리)
        function loadMorePosts(callback) {
            // TODO: 실제로 서버에서 새로운 게시물을 가져와서 $tiles에 추가하는 로직을 구현해야 합니다.
            // 이 함수가 비동기 요청이므로, 콜백 함수를 이용해 스크롤 이벤트 처리 상태를 초기화.
            // 즉, loadMorePosts 함수가 완료되면 콜백 함수가 실행.
            // 콜백 함수를 사용하여 스크롤 이벤트 처리 상태를 초기화.
            callback();
        }

        // Call the layout function for the first time
        applyLayout();

        // Capture scroll event.
        $window.bind('scroll.wookmark', onScroll);
    })(jQuery);
}

function createPostElement(post) {
    const username = post.username;

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

    const createdAt = post.createdAt;

    const date = new Date(createdAt);

    const formattedDate = date.toLocaleString("ko-KR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
    });

    return `
      <li onclick="detailPost(${post.id})">
        <div class="post-info">
          <div class="post-basic-info">
            <h3><a href="#" style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${post.title}</a></h3>
            <span>
                <a href="#">작성자:${post.nickname}</a>
                <p>(${maskedUsername})</p>
            </span>
            <p style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${formattedDate}</p>
          </div>
          <div class="post-info-rate-share">
            <div>
              <p class="txt-center">댓글수 : ${post.commentList}</p>
            </div>
            <div class="clear"></div>
          </div>
        </div>
      </li>
    `;
}

function detailPost(postId) {
    window.location.href = `/api/post/detail-page/${postId}`;
}

function logout() {
    Swal.fire({
        title: '로그아웃 하시겠습니까?',
        text: "확인을 누르시면 로그아웃됩니다.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
    }).then(result => {
        if (result.isConfirmed) {
            Swal.fire({
                icon: 'success',
                title: '로그아웃 완료',
                text: '로그아웃이 완료되었습니다.',
            }).then(function () {
                Cookies.remove('Authorization', {path: '/'})
                window.location.href = '/'
            });

        }

    })

}
// 모달 버튼과 모달 창 요소를 가져옵니다.
var modalBtn = document.getElementById("modalButton");
var modal = document.getElementById("myModal");

// 모달 버튼을 클릭하면 모달 창을 보여줍니다.
modalBtn.onclick = function() {
    modal.style.display = "block";
}

// 모달 창 닫기 버튼을 클릭하면 모달 창을 닫습니다.
var closeBtn = document.getElementsByClassName("close")[0];
closeBtn.onclick = function() {
    modal.style.display = "none";
}

// 모달 외부 영역을 클릭하면 모달 창을 닫습니다.
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }


}


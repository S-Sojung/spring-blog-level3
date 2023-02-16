<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ include file="../layout/header.jsp" %>
        <div class="container my-3">


            <c:if test="${principal.id == boardDto.userId}">
                <div class="mb-3">
                    <a href="/board/${boardDto.id}/updateForm" class="btn btn-warning">수정</a>
                    <button onClick="deleteById(${boardDto.id})" class="btn btn-danger">삭제</button>
                </div>
            </c:if>

            <div class="mb-2 d-flex justify-content-end">
                글 번호 :
                <span id="id" class="me-3">
                    <i>${boardDto.id}</i>
                </span>
                작성자 :
                <span class="me-3">
                    <i>${boardDto.username} </i>
                </span>
            </div>


            <div>
                <h1><b>${boardDto.title}</b></h1>
            </div>
            <hr />
            <div>
                <div>${boardDto.content}</div>
            </div>
            <hr />
            <div>
                <i id="heart" class="fa-regular ${mylove.love} fa-heart fa-lg" value="${mylove.id}" onClick="heartClick(${mylove.id})"></i>
            </div>

            <div class="card mt-3">
                <form action="/reply" method="post">
                    <input type="hidden" name="boardId" value="${boardDto.id}">
                    <div class="card-body">
                        <textarea name="comment" id="reply-comment" class="form-control" rows="1"></textarea>
                    </div>
                    <div class="card-footer">
                        <button type="submit" id="btn-reply-save" class="btn btn-primary">등록</button>
                    </div>
                </form>
            </div>
            <br />
            <div class="card">
                <div class="card-header">댓글 리스트</div>
                <ul id="reply-box" class="list-group">
                <c:forEach items="${replyDtos}" var="reply">
                    <li id="reply-${reply.id}" class="list-group-item d-flex justify-content-between">
                        <div>${reply.comment}</div>
                        <div class="d-flex">
                            <div class="font-italic">작성자 : ${reply.username} &nbsp;</div>  
                            <c:if test="${principal.id==reply.userId}" >
                            <button onClick="deleteByReplyId(${reply.id})" class="badge bg-secondary">삭제</button>
                            </c:if>
                        </div>
                    </li>
                </c:forEach>
                </ul>
            </div>
        </div>
        <script>
        //여기서 insert delete 하고 love controller 작성해주기
                // $("#heart").click(() => { //하트가 클릭됐을때 색칠이 안되어있다면 insert
                function heartClick(id){
                    // console.log(id);
                    if (!$("#heart").hasClass("fa-solid")) { //if id가 0
                        let data={ 
                            "love" : "fa-solid",
                            "boardId": ${boardDto.id}
                        };

                        $.ajax({
                            type: "post",
                            url: "/love",
                            data: JSON.stringify(data),
                            contentType : "application/json; charset=utf-8",
                            dataType: "json" 
                        })
                            .done(res => { 
                                alert(res.msg);
                                $("#heart").addClass("fa-solid");
                                $("#heart").attr("value",res.data);
                                $("#heart").attr("onClick","heartClick("+res.data+")");
                            })
                            .fail(err => { 
                                alert(err.responseJSON.msg);
                            });
                        


                    } else {//색칠이 되어있으면 delete 실행 
                        $.ajax({
                            type: "delete",
                            url: "/love/" + id,
                            dataType: "json"
                        })
                            .done(res => { 
                                alert(res.msg);

                                $("#heart").removeClass("fa-solid");
                                $("#heart").attr("value",res.data);
                                $("#heart").attr("onClick","heartClick("+res.data+")");
                            })
                            .fail(err => { 
                                alert(err.responseJSON.msg);
                            });
                    }   
                };

            function deleteByReplyId(id){
                //$("#reply-"+id).remove(); ajax가 done 이 되었을때
                //location.reload() 도 가능하지만 위가 더 낫다 .
                 $.ajax({
                    type: "delete",
                    url: "/reply/" + id,
                    dataType: "json"
                })
                    .done(res => { //20X 일때
                        alert(res.msg);
                        //location.reload();
                        $("#reply-"+id).remove();
                    })
                    .fail(err => { //40X , 50X 일때
                        // console.log(err.responseJSON);
                        alert(err.responseJSON.msg);
                    });
            }
        </script>

        <script>
            function deleteById(id) {
                //이 안에서 el 표현식으로 하면 안됨. script파일을 따로 빼는 순간 문제가 생김
                //아작스 요청은 전부 DTO 로 응답, customApiExcetion 으로 
                $.ajax({
                    type: "delete",
                    url: "/board/" + id,
                    dataType: "json"
                })
                    .done(res => { //20X 일때
                        alert(res.msg);
                        location.href = "/";
                    })
                    .fail(err => { //40X , 50X 일때
                        // console.log(err.responseJSON);
                        alert(err.responseJSON.msg);

                    });
            }
        </script>
        <%@ include file="../layout/footer.jsp" %>